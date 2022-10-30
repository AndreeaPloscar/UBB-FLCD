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
    private final Pattern charConstant = Pattern.compile("^\'[a-zA-Z.0-9]\'*$");
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
        String delim = this.delimiters.stream().reduce(String::concat).orElse("");
        StringTokenizer tokenizer = new StringTokenizer(line, " " + delim, true);
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken();
            if (!white.contains(token)) {
                splitted.add(token.trim());
            }
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
                    this.pif.addToken("const", result.getKey());
                } else {
                    if (integerConstant.matcher(token).matches()) {
                        var result = this.st.addElement(token);
                        if (result.getValue() != null) {
                            this.pif.reorganize(result.getValue());
                        }
                        this.pif.addToken("const", result.getKey());
                    } else {
                        if (stringConstant.matcher(token).matches()) {
                            var result = this.st.addElement(token);
                            if (result.getValue() != null) {
                                this.pif.reorganize(result.getValue());
                            }
                            this.pif.addToken("const", result.getKey());
                        } else {
                            if (identifier.matcher(token).matches()) {
                                var result = this.st.addElement(token);
                                if (result.getValue() != null) {
                                    this.pif.reorganize(result.getValue());
                                }
                                this.pif.addToken("id", result.getKey());
                            } else {
                                throw new LexicalError(lineNumber, tokenPos);
                            }
                        }
                    }
                }
            }
        }
    }

    private void outputFiles() {
        try {
            FileWriter myWriter = new FileWriter("ST.out");
            myWriter.write("ST structure: hash table\n");
            myWriter.write(this.st.toString());
            myWriter.close();
            System.out.println("Successfully wrote to ST file.");
            myWriter = new FileWriter("PIF.out");
            myWriter.write(this.pif.toString());
            myWriter.close();
            System.out.println("Successfully wrote to PIF file.");
            myWriter = new FileWriter("message.out");
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
            int number = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                try {
                    this.scanLine(line, number);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    message.append(e.getMessage()).append("\n");
                }
                number += 1;
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if(message.isEmpty()){
            message.append("Lexically correct");
        }
        this.outputFiles();
    }
}
