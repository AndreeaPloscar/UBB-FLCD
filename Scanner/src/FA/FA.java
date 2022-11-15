package FA;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class FA {


    private List<String> states = new ArrayList<>();
    private String initialState;
    private List<String> finalStates = new ArrayList<>();
    private List<String> alphabet = new ArrayList<>();
    private final List<Transition> transitions = new ArrayList<>();

    public List<String> getStates() {
        return states;
    }

    public String getInitialState() {
        return initialState;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    private List<String> splitLine(String line) throws FaTokenException {
        List<String> splitted = new ArrayList<>();
        Pattern faToken = Pattern.compile("^[a-zA-Z0-9]$");
        StringTokenizer tokenizer = new StringTokenizer(line, " ", false);
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken().trim();
            if (faToken.matcher(token).matches()) {
                splitted.add(token.trim());
            } else {
                throw new FaTokenException(token);
            }
        }
        return splitted;
    }

    private List<String> splitUnchecked(String line) throws FaTokenException {
        List<String> splitted = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(line, " ", false);
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken().trim();
            splitted.add(token.trim());
        }
        return splitted;
    }

    private void loadTransitions(List<String> transitions) throws FaTokenException {
        String transition = "";
        List<String> splittedTransition;
        for (var i = 0 ;i < transitions.size(); i++) {
            transition = transitions.get(i);
            splittedTransition = splitUnchecked(transition);
            if(splittedTransition.size() != 3){
                throw new FaTokenException("transition " + i);
            }
            if(!this.alphabet.contains(splittedTransition.get(1))){
                throw new FaTokenException("element in transition " + i + "is not in the alphabet");
            }
            if(!this.states.contains(splittedTransition.get(0)) || !this.states.contains(splittedTransition.get(2))){
                throw new FaTokenException("state in transition " + i + "is not a state of this FA");
            }
            this.transitions.add(new Transition(splittedTransition));
        }
    }

    public boolean isDFA() {
        for(var transition1: transitions){
            for(var transition2: transitions){
                if(transition1 != transition2 && transition1.source.equals(transition2.source) && transition1.element.equals(transition2.element)){
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean isAccepted(String sequence) {
        var currentState = this.initialState;
        var i = 0;
        char currentChar;
        var found = false;
        if(sequence.length() == 0){
            return finalStates.contains(initialState);
        }
        while(i < sequence.length()){
            currentChar = sequence.charAt(i);
            found = false;
            for(var transition: transitions){
                if(transition.element.equals(String.valueOf(currentChar)) && transition.source.equals(currentState)){
                    found = true;
                    currentState = transition.destination;
                    break;
                }
            }
            if(!found){
                return false;
            }
            i ++;
        }
        return finalStates.contains(currentState);
    }


    public void readFromFile(String fileName) {
        Path file = Paths.get(fileName + ".in");
        Scanner fileScanner = null;
        List<String> lines;
        try {
            fileScanner = new Scanner(file);
            try {
                if(fileScanner.hasNextLine()) {
                    lines = new ArrayList<>(List.of(fileScanner.nextLine().split(";")));
                }
                else throw new FaTokenException("File is empty");
                this.states = splitLine(lines.get(0));
                this.initialState = splitLine(lines.get(1)).get(0);
                this.finalStates = splitLine(lines.get(2));
                this.alphabet = splitUnchecked(lines.get(3));
                loadTransitions(lines.subList(4, lines.size()));
            } catch (FaTokenException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
