import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
public class Grammar {

    private NonTerminal initialSymbol;
    private List<NonTerminal> nonTerminals;
    private List<Terminal> terminals;
    private List<Production> productions = new ArrayList<>();


    private Pair<List<String>, List<String>> splitProduction(String production) {
        List<String> splittedProduction = List.of(production.split("->"));
        return new Pair<>(Arrays.stream(splittedProduction.get(0).strip().split(" ")).collect(Collectors.toList()),
                Arrays.stream(splittedProduction.get(1).strip().split(" ")).collect(Collectors.toList()));

    }

    public void enhance() {
        var oldInitial = this.initialSymbol;
        this.initialSymbol = new NonTerminal(this.initialSymbol.getLabel() + "'");
        this.productions.add(new Production(List.of(initialSymbol), List.of(oldInitial)));
        this.nonTerminals.add(this.initialSymbol);
    }

    public boolean isNonTerminal(String label) {
        return this.nonTerminals.stream().map(NonTerminal::getLabel).anyMatch(l -> Objects.equals(label, l));
    }

    private void loadProductions(List<String> productions) throws ReadGrammarException {
        String production;
        Pair<List<String>, List<String>> splittedProduction;
        for (var i = 0; i < productions.size(); i++) {
            production = productions.get(i);
            splittedProduction = splitProduction(production);
            for (String leftSymbol : splittedProduction.getFirst()) {
                if (!isNonTerminal(leftSymbol)) {
                    throw new ReadGrammarException("Terminal in left side of production.");
                }
            }
            this.productions.add(new Production(splittedProduction.getFirst().stream().map(NonTerminal::new).collect(Collectors.toList()), splittedProduction.getSecond().stream().map(Symbol::new).collect(Collectors.toList())));

        }
    }

    private List<String> splitLine(String line) throws ReadGrammarException {
        List<String> splitted = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(line, " ", false);
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken().trim();
            splitted.add(token.trim());
        }
        return splitted;
    }

    public void readFromFile(String fileName) throws ReadGrammarException {
        this.nonTerminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new ArrayList<>();
        Path file = Paths.get(fileName);
        Scanner fileScanner;
        List<String> lines = new ArrayList<>();
        try {
            fileScanner = new Scanner(file);
            try {
                while (fileScanner.hasNextLine()) {
                    lines.add(fileScanner.nextLine());
                }
                this.terminals = splitLine(lines.get(0)).stream().map(Terminal::new).collect(Collectors.toList());
                this.nonTerminals = splitLine(lines.get(1)).stream().map(NonTerminal::new).collect(Collectors.toList());
                this.initialSymbol = new NonTerminal(splitLine(lines.get(2)).get(0));
                System.out.println(lines.subList(3, lines.size()));
                loadProductions(lines.subList(3, lines.size()));
            } catch (ReadGrammarException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isCFG() {
        return productions.stream().allMatch(p -> p.getLeft().size() == 1);
    }

    public List<Production> getProductionsForNonTerminal(String nonTerminalValue) {
        var searchValue = nonTerminals.stream()
                .filter(x -> x.getLabel().equals(nonTerminalValue)).findFirst();
        if (searchValue.isEmpty())
            throw new RuntimeException("Provided value is not a valid non-terminal");

        var nonTerminal = searchValue.get();

        return productions.stream().filter(p -> p.getLeftForCFG().equals(nonTerminal)).collect(Collectors.toList());
    }

    public List<Symbol> getAllSymbols() {
        List<Symbol> symbols = new ArrayList<>(this.nonTerminals);
        symbols.addAll(this.terminals);
        return symbols;
    }
}
