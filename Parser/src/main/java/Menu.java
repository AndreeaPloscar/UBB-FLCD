import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class Menu {

    public static void printMenu() {
        System.out.print("""
                1 - read grammar from file
                2 - print non-terminals
                3 - print terminals
                4 - print productions
                5 - print productions for non-terminal
                6 - check CFG
                0 - exit
                >>>\040""");
    }



    public static void main(String[] args) {
        var grammar = new Grammar();
        try {
            grammar.readFromFile(Objects.requireNonNull(Menu.class.getResource("g1.txt")).getFile());
        } catch (ReadGrammarException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        String option = "";
        while (!Objects.equals(option, "0")) {
            printMenu();
            try {
                option = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (option) {
                case "1" -> {
                    System.out.print("file >> ");
                    String file = "";
                    try {
                        file = reader.readLine();
                        grammar.readFromFile(Objects.requireNonNull(Menu.class.getResource(file)).getFile());
                    } catch (IOException | ReadGrammarException e) {
                        e.printStackTrace();
                    }
                }
                case "2" -> {
                    System.out.println(grammar.getNonTerminals());
                }
                case "3" -> {
                    System.out.println(grammar.getTerminals());
                }
                case "4" -> {
                    System.out.println(grammar.getProductions().stream().map(Production::toString).collect(Collectors.joining("\n")));
                }
                case "5" -> {
                    System.out.print("non-terminal >> ");
                    String terminal = "";
                    try {
                        terminal = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("productions: " + grammar.getProductionsForNonTerminal(terminal));
                }

                case "6" -> {
                    System.out.println("is CFG? " + grammar.isCFG());
                }

            }
        }
    }

    public void Run() {

    }
}
