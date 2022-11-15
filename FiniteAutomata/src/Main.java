import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {

    public static void printMenu() {
        System.out.println("""
                1 - load FA from file
                2 - check DFA
                3 - print states
                4 - print alphabet
                5 - print initial state
                6 - print final states
                7 - print transitions
                8 - check sequence
                0 - exit
                >>>""");
    }



    public static void main(String[] args) {
	    FA fa = new FA();
        fa.readFromFile("FA");
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
            switch (option){
                case "1":{
                    System.out.println("file >> ");
                    String file = "";
                    try {
                        file = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fa.readFromFile(file);
                    break;
                }
                case "2":{
                    System.out.println("is DFA? " + fa.isDFA());
                    break;
                }
                case "3":{
                    System.out.println(fa.getStates());
                    break;
                }
                case "4":{
                    System.out.println(fa.getAlphabet());
                    break;
                }
                case "5":{
                    System.out.println(fa.getInitialState());
                    break;
                }
                case "6":{
                    System.out.println(fa.getFinalStates());
                    break;
                }
                case "7":{
                    System.out.println(fa.getTransitions());
                    break;
                }
                case "8":{
                    System.out.println("sequence >> ");
                    String sequence = "";
                    try {
                        sequence = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("is accepted: " + fa.isAccepted(sequence));
                    break;
                }
            }
        }
    }
}
