import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
                    throw new ParsingTableConflictException(a.toString() + "has action " + actions.get(a) + " conflicts " + action);
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

    }

    private void actionReduce() {

    }


    public void parse(List<String> sequence) throws ParsingTableConflictException {
        this.generateParsingTable();
        var state = 0;
        Collections.reverse(sequence);
        inputStack.addAll(sequence);
        workingStack.push(state);
        var done = false;
        do {
            if (Objects.equals(table.getRows().get(state).getAction(), "shift")) {
                actionShift();
            } else {
                if (table.getRows().get(state).getAction().startsWith("reduce")) {
                    actionReduce();
                } else {
                    if (Objects.equals(table.getRows().get(state).getAction(), "accept")) {
                        System.out.println("success!");
                        System.out.println(outputStack);
                        done = true;
                    } else {
                        System.out.println("erorr!");
                        done = true;
                    }
                }
            }
        } while (!done);


    }

    public void printStatesString() {

        for (int i = 0; i < this.states.size(); i++) {
            System.out.print("s" + i + "= {");
            System.out.print(states.get(i).stream().map(Item::toString).collect(Collectors.joining(" ")) + "}\n");

        }
    }

}
