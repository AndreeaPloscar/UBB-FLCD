import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
public class Grammar {

    private List<NonTerminal> initialSymbol;
    private List<NonTerminal> nonTerminals;
    private List<Terminal> terminals;
    private List<Production> productions;


    private Pair<List<String>, List<String>> splitProduction(String production) {
        List<String> splittedProduction = List.of(production.split("-"));
        return new Pair<>(Arrays.stream(splittedProduction.get(0).split("")).collect(Collectors.toList()),
                Arrays.stream(splittedProduction.get(1).split("")).collect(Collectors.toList()));

    }

    private void loadProductions(List<String> productions) throws ReadGrammarException {
        String production = "";
        Pair<List<String>, List<String>> splittedProduction;
        for (var i = 0; i < productions.size(); i++) {
            production = productions.get(i);
            splittedProduction = splitProduction(production);
            for (String leftSymbol: splittedProduction.getFirst()) {
                if(!this.nonTerminals.contains(leftSymbol)){
                    throw new ReadGrammarException("Nonterminal in left side of profuction.");
                }
            }
            this.productions.add(new Production(splittedProduction.getFirst(), splittedProduction.getSecond());
        }
    }

    private <T extends Symbol> List<T> splitLine(String line) throws ReadGrammarException {
        List<T> splitted = new ArrayList<>();
        Pattern symbol = Pattern.compile("^[a-zA-Z]+$");
        StringTokenizer tokenizer = new StringTokenizer(line, " ", false);
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken().trim();
            if (symbol.matcher(token).matches()) {
                splitted.add((T) new Symbol(token.trim()));
            } else {
                throw new ReadGrammarException(token);
            }
        }
        return splitted;
    }

    public Boolean checkContextFree(){
        for (Production production: this.productions) {
            if(production.left.size() != 1){
                return false;
            }
        }
        return true;
    }

    public void readFromFile(String fileName) throws ReadGrammarException {
        Path file = Paths.get(fileName);
        Scanner fileScanner;
        List<String> lines = new ArrayList<>();
        try {
            fileScanner = new Scanner(file);
            try {
                while (fileScanner.hasNextLine()) {
                    lines.add(fileScanner.nextLine());
                }
                this.terminals = splitLine(lines.get(0));
                this.nonTerminals = splitLine(lines.get(1));
                this.initialSymbol = splitLine(lines.get(2));
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
}
