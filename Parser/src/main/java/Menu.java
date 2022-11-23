import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Menu {

    public static void printMenu() {
        System.out.println("""
                1 - read grammar from file
                2 - print non-terminals
                3 - print terminals
                4 - print productions
                5 - print productions for terminal
                6 - check CFG
                0 - exit
                >>>""");
    }


    public static void main(String[] args) {
        var grammar = new Grammar();
        try {
            grammar.readFromFile("g1.txt");
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
                    System.out.println("file >> ");
                    String file = "";
                    try {
                        file = reader.readLine();
                        grammar.readFromFile(file);
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
                    System.out.println(grammar.getProductions());
                }
                case "5" -> {
                    System.out.println("non-terminal >> ");
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
