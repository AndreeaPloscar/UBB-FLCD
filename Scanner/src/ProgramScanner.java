import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class ProgramScanner {

    private final PIF pif = new PIF();
    private final ST st = new ST(2);
    private final Pattern stringConstant = Pattern.compile("^\"[a-zA-Z.0-9]*\"$");
    private final Pattern charConstant = Pattern.compile("^\'[a-zA-Z.0-9]\'$");
    private final Pattern integerConstant = Pattern.compile("^[+-]?[1-9][0-9]*|0$");
    private final Pattern identifier = Pattern.compile("^~[a-zA-Z0-9]+$");
    private final List<String> declaredTokens = new ArrayList<>();
    private final List<String> delimiters = new ArrayList<>();
    private StringBuilder message = new StringBuilder();

    private void readTokens() {
        try {
            File tokensFile = new File("token.in");
            Scanner fileScanner = new Scanner(tokensFile);
            while (fileScanner.hasNextLine()) {
                String token = fileScanner.nextLine();
                if (token.trim().equals("")) {
                    this.delimiters.addAll(declaredTokens);
                } else {
                    declaredTokens.add(token.trim());
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println(declaredTokens);
    }

    private List<String> splitTokens(String line) {
        List<String> splitted = new ArrayList<>();
        List<String> white = List.of(" ", "\n", "\t");
        String delimitersString = this.delimiters.stream().reduce(String::concat).orElse("");
        StringTokenizer tokenizer = new StringTokenizer(line, " " + delimitersString, true);
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken();
            if (!white.contains(token)) {
                splitted.add(token.trim());
            }
        }
        var i = 0;
        while(i < splitted.size()){
            if(Objects.equals(splitted.get(i), "=") && i < splitted.size() - 1 && Objects.equals(splitted.get(i+1), "=")){
                splitted.add(i, "==");
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            if(Objects.equals(splitted.get(i), ":") && i < splitted.size() - 1 && Objects.equals(splitted.get(i+1), ":")){
                splitted.add(i, "::");
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            if(Objects.equals(splitted.get(i), ":") && i < splitted.size() - 1 && Objects.equals(splitted.get(i+1), "=")){
                splitted.add(i, ":=");
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            if(Objects.equals(splitted.get(i), "<") && i < splitted.size() - 1 && Objects.equals(splitted.get(i+1), "=")){
                splitted.add(i, "<=");
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            if(Objects.equals(splitted.get(i), ">") && i < splitted.size() - 1 && Objects.equals(splitted.get(i+1), "=")){
                splitted.add(i, ">=");
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            if(Objects.equals(splitted.get(i), "!") && i < splitted.size() - 1 && Objects.equals(splitted.get(i+1), "=")){
                splitted.add(i, "!=");
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            if(Objects.equals(splitted.get(i), "-") && i < splitted.size() - 2 && Objects.equals(splitted.get(i+1), ":") && Objects.equals(splitted.get(i+2), "=")){
                splitted.add(i, "-:=");
                splitted.remove(i+1);
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            if(Objects.equals(splitted.get(i), "+") && i < splitted.size() - 2 && Objects.equals(splitted.get(i+1), ":") && Objects.equals(splitted.get(i+2), "=")){
                splitted.add(i, "+:=");
                splitted.remove(i+1);
                splitted.remove(i+1);
                splitted.remove(i+1);
            }
            i+=1;
        }
        return splitted;
    }

    private void scanLine(String line, int lineNumber) throws Exception {
        List<String> splitted = this.splitTokens(line);
        System.out.println(splitted);
        for (var tokenPos = 0; tokenPos < splitted.size(); tokenPos++) {
            var token = splitted.get(tokenPos);
            if (declaredTokens.contains(token)) {
                this.pif.addToken(token, new AbstractMap.SimpleEntry<>(-1, -1));
            } else {
                if (charConstant.matcher(token).matches()) {
                    var result = this.st.addElement(token);
                    if (result.getValue() != null) {
                        this.pif.reorganize(result.getValue());
                    }
                    this.pif.addToken("CONST", result.getKey());
                } else {
                    if (integerConstant.matcher(token).matches()) {
                        var result = this.st.addElement(token);
                        if (result.getValue() != null) {
                            this.pif.reorganize(result.getValue());
                        }
                        this.pif.addToken("CONST", result.getKey());
                    } else {
                        if (stringConstant.matcher(token).matches()) {
                            var result = this.st.addElement(token);
                            if (result.getValue() != null) {
                                this.pif.reorganize(result.getValue());
                            }
                            this.pif.addToken("CONST", result.getKey());
                        } else {
                            if (identifier.matcher(token).matches()) {
                                var result = this.st.addElement(token);
                                if (result.getValue() != null) {
                                    this.pif.reorganize(result.getValue());
                                }
                                this.pif.addToken("ID", result.getKey());
                            } else {
                                throw new LexicalError(lineNumber, tokenPos);
                            }
                        }
                    }
                }
            }
        }
    }

    private void outputFiles(String program) {
        try {
            FileWriter myWriter = new FileWriter(new File(program, "ST.out"));
            myWriter.write("ST structure: hash table\n");
            myWriter.write(this.st.toString());
            myWriter.close();
            System.out.println("Successfully wrote to ST file.");
            myWriter = new FileWriter(new File(program, "PIF.out"));
            myWriter.write(this.pif.toString());
            myWriter.close();
            System.out.println("Successfully wrote to PIF file.");
            myWriter = new FileWriter(new File(program, "message.out"));
            myWriter.write(this.message.toString());
            myWriter.close();
            System.out.println("Successfully wrote to message file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void scan(String program) {
        this.readTokens();
        try {
            File tokensFile = new File(program + ".txt");
            Scanner fileScanner = new Scanner(tokensFile);
            int lineNumber = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                try {
                    this.scanLine(line, lineNumber);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    message.append(e.getMessage()).append("\n");
                }
                lineNumber += 1;
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if(message.isEmpty()){
            message.append("Lexically correct");
        }
        this.outputFiles(program);
    }
}
