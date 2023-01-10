import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Parser {

    private Grammar grammar;
    private ParsingTable table;
    Stack<Object> workingStack = new Stack<>();
    Stack<String> inputStack = new Stack();
    Stack<Integer> outputStack = new Stack<>();
    List<Node> tree = new ArrayList<>();

    private void enhanceGrammar() {
        grammar.enhance();
    }

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    private List<Set<Item>> states = new ArrayList<>();

    public Set<Item> closure(Set<Item> items) {
        var closure = new HashSet<>(items);
        var changed = true;
        while (changed) {
            changed = false;
            var oldClosure = new HashSet<>(closure);
            for (var item : oldClosure) {
                if (!item.getInput().isEmpty()) {
                    var symbol = item.getInput().get(0);
                    if (grammar.isNonTerminal(symbol.getLabel())) {
                        var oldSize = closure.size();
                        closure.addAll(grammar.getProductionsForNonTerminal(symbol.getLabel())
                                .stream()
                                .map(Item::new)
                                .collect(Collectors.toSet()));
                        changed = changed | oldSize != closure.size();
                    }
                }
            }
        }
        return closure;
    }

    public Set<Item> goTo(Set<Item> state, Symbol symbol) {
        Set<Item> itemsForClosure = new HashSet<>();

        for (var item : state) {
            if (item.isSymbolFirstInInput(symbol)) {
                itemsForClosure.add(item.moveDot());
            }
        }

        return closure(itemsForClosure);
    }

    public void generateCanonicalCollection() {
        this.enhanceGrammar();

        this.states.add(this.closure(Set.of(
                new Item(grammar.getInitialSymbol(),
                        List.of(),
                        grammar.getProductionsForNonTerminal(
                                grammar.getInitialSymbol().getLabel()).get(0).getRight()))));

        var changed = true;
        while (changed) {
            changed = false;
            var newStates = new ArrayList<>(this.states);
            for (var state : this.states) {
                for (Symbol symbol : this.grammar.getAllSymbols()) {
                    var goToResult = this.goTo(state, symbol);
                    if (goToResult.isEmpty() || states.contains(goToResult))
                        continue;

                    changed = true;
                    newStates.add(goToResult);
                }
            }
            this.states = newStates;
        }
    }

    private Integer findState(Set<Item> state) {
        for (var index = 0; index < this.states.size(); index++) {
            if (this.states.get(index).equals(state)) {
                return index;
            }
        }
        return -1;
    }

    public void generateParsingTable() throws ParsingTableConflictException {
        this.generateCanonicalCollection();
        this.table = new ParsingTable(grammar.getAllSymbols(), this.states.size());
        for (var index = 0; index < states.size(); index++) {
            var state = states.get(index);
            var actions = new HashMap<Item, String>();
            for (var item : state) {
                if (item.getInput().isEmpty()) {
                    if (item.getLeft().equals(grammar.getInitialSymbol())) {
                        actions.put(item, "accept");
                    } else {
                        for (int i = 0; i < grammar.getProductions().size(); i++) {
                            var prod = grammar.getProductions().get(i);
                            if (prod.getLeftForCFG().equals(item.getLeft()) && prod.right.equals(item.getWork())) {
                                actions.put(item, "reduce" + (i + 1));
                                break;
                            }
                        }
                    }
                } else {
                    actions.put(item, "shift");
                }
            }
            var action = actions.entrySet().iterator().next().getValue();
            for (var a : actions.keySet()) {
                if (!actions.get(a).equals(action)) {
                    throw new ParsingTableConflictException(state + "has action " + actions.get(a) + "on item" + a.toString() + " conflicts " + action + "\n");
                }
            }
            table.getRows().get(index).setAction(action);

            for (var symbol : grammar.getAllSymbols()) {
                var goToResult = goTo(state, symbol);
                var goToState = this.findState(goToResult);
                if (goToState != -1) {
                    table.setGoToSymbol(index, symbol, goToState);
                }
            }
        }
    }

    private void actionShift() {
        var currentState = workingStack.peek();
        var currentSymbol = inputStack.peek();
        var goToState = table.getRows().get(currentState).getGoToState(currentSymbol);
        inputStack.pop();
        workingStack.push(currentSymbol);
        workingStack.push(goToState);
    }

    private void actionReduce() {
        var currentState = workingStack.peek();
        var productionPos = Integer.parseInt(table.getRows().get(currentState).getAction().substring(6));
        var production = grammar.getProductions().get(productionPos - 1);
        var symbols = new ArrayList<>(production.getRight());
        var symbolsStack = new Stack<Symbol>();
        symbolsStack.addAll(symbols);
        while (!symbolsStack.isEmpty()) {
            try {
                var currentS = workingStack.pop();
                if (currentS.equals(symbolsStack.peek().getLabel())) {
                    symbolsStack.pop();
                }
            } catch (Exception e) {
                System.out.println("e");
            }

        }
        var newState = (int) workingStack.peek();
        var newSymbol = production.getLeftForCFG().getLabel();
        var goToState = table.getRows().get(newState).getGoToState(newSymbol);
        System.out.println("Reduce newstate " + newState + " go to: " + goToState + " new symbol: " + newSymbol);
        workingStack.push(newSymbol);

        workingStack.push(goToState);
        outputStack.push(productionPos);
    }

    public void printTree(String filename) {
        this.createTree();
        var stringBuilder = new StringBuilder();
        for (var node : tree) {
            System.out.println(node);
            stringBuilder.append(node).append("\n");
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(stringBuilder.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private void createTree() {
        var output = new ArrayList<>(outputStack);
        var productions = new Stack<Integer>();
        productions.addAll(output);
        var startingSymbol = grammar.getProductions().get(0).getLeftForCFG();
        tree.add(Node.builder().index(1).info(startingSymbol.getLabel()).parent(0).rightSibling(0).build());
        while (!productions.isEmpty()) {
            var pos = productions.pop();
            var symbols = new ArrayList<>(grammar.getProductions().get(pos - 1).getRight());
            var lastNonTerminal = 0;
            for (var node : tree) {
                if (grammar.isNonTerminal(node.getInfo())) {
                    lastNonTerminal = node.getIndex();
                }
            }
            Collections.reverse(symbols);
            var index = tree.size() + 1;
            var isFirst = true;
            for (var s : symbols) {
                tree.add(Node.builder().index(index).info(s.getLabel()).parent(lastNonTerminal).rightSibling(isFirst ? 0 : index - 1).build());
                index += 1;
                isFirst = false;
            }
        }
    }

    public void parse(List<String> sequence) throws ParsingTableConflictException {
        this.generateParsingTable();
        var state = 0;
        Collections.reverse(sequence);
        inputStack.addAll(sequence);
        workingStack.push(state);
        var done = false;
        do {
            try {
                state = (int) workingStack.peek();
            } catch (Exception e) {
                System.out.println("e");
            }
            if (Objects.equals(table.getRows().get(state).getAction(), "shift")) {
                actionShift();
            } else {
                if (table.getRows().get(state).getAction().startsWith("reduce")) {
                    actionReduce();
                } else {
                    if (Objects.equals(table.getRows().get(state).getAction(), "accept")) {
                        System.out.println("success!");
                        var output = new ArrayList<>(outputStack);
                        Collections.reverse(output);
                        System.out.println(output);
                        done = true;
                    } else {
                        System.out.println("erorr!");
                        done = true;
                    }
                }
            }
        } while (!done);

    }

    public void parsePIF(String filepath) {
        List<String> sequence = new ArrayList<>();

        Path file = Paths.get(Objects.requireNonNull(Menu.class.getResource(filepath)).getFile());
        Scanner fileScanner;
        List<String> lines = new ArrayList<>();
        try {
            fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                sequence.add(fileScanner.nextLine().split(" ")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parse(sequence);
        } catch (ParsingTableConflictException e) {
            throw new RuntimeException(e);
        }
    }

    public void printStatesString() {

        for (int i = 0; i < this.states.size(); i++) {
            System.out.print("s" + i + "= {");
            System.out.print(states.get(i).stream().map(Item::toString).collect(Collectors.joining(" ")) + "}\n");

        }
    }

}
