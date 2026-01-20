import java.io.*;
import java.nio.file.AccessDeniedException;

public class Evaluator {

    //validating expressions
    private static void validateExpression(String line) throws SyntaxException, LexicalException {
        if(!line.matches("[0-9a-zA-Z+\\-*/()=\\s.]+")) {
            throw new LexicalException("illegal characters");
        }
        if(!line.trim().endsWith("=")) {
            throw new SyntaxException("= expected");
        }

        int openParenthesis=0;
        for(char c : line.toCharArray()) {
            if(c=='(') {
                openParenthesis++;
            } else if(c==')') {
                openParenthesis--;
            }
            if(openParenthesis<0) {
                throw new SyntaxException(") unexpected");
            }
        }
        if(openParenthesis>0) {
            throw new SyntaxException(") expected");
        }

        //input number integer overflow
        String[] tokens=line.split("[+\\-*/()=\\s]+");
        for(String token : tokens) {
            if(!token.isEmpty() && token.matches("\\d+")) {
                try {
                    long number = Long.parseLong(token);
                    if(number>Integer.MAX_VALUE) {
                        throw new MyRuntimeException("integer limit exceeded");
                    }
                } catch (NumberFormatException e) {
                    throw new MyRuntimeException("integer limit exceeded");
                }
            }
        }
    }

    //validating results
    private static void validateResult(String result) throws MyRuntimeException {
        try {
            double d=Double.parseDouble(result);
            //division by zero
            if(Double.isInfinite(d) || Double.isNaN(d)) {
                throw new RuntimeException("division by zero");
            }
        } catch(NumberFormatException e) {
            throw new MyRuntimeException("number format error");
        }
    }

    //processor
    private static void processFile(File file) throws InputOutputException, EngineException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    //validate line
                    validateExpression(line);

                    //delete '=' from each equation
                    line=line.substring(0, line.length()-1);
                    String command="print(eval('"+line+"'))";

                    //evaluating the equations - used python3 since it works on MacOS
                    Process process = new ProcessBuilder("python3", "-c", command).start();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                         BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String result=reader.readLine();

                        //since Python errors are multiline
                        StringBuilder errorBuilder=new StringBuilder();
                        String errorLine;
                        while((errorLine=errorReader.readLine())!=null) {
                            errorBuilder.append(errorLine);
                        }
                        String error=errorBuilder.toString();
                        if(!error.isEmpty()) {
                            if(error.contains("ZeroDivisionError")) {
                                System.err.println("Runtime error: ZeroDivisionError");
                            } else if(error.contains("NameError")) {
                                System.err.println("Runtime error: undefined variable (NameError)");
                            } else {
                                System.err.println("Python error: "+error);
                            }
                        } else if(result!=null) {
                            validateResult(result);
                            System.out.println(result);
                        }
                    }
                } catch (LexicalException | SyntaxException | MyRuntimeException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    throw new EngineException("Python engine error: "+e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new InputOutputException("file not found");
        } catch (AccessDeniedException e) {
            throw new InputOutputException("file cannot be opened due to insufficient permission");
        } catch (IOException e) {
            throw new InputOutputException("Error reading file: "+e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        File file=new File("example.txt");
        try {
            processFile(file);
        } catch(InputOutputException e) {
            System.err.println("I/O error: "+e.getMessage());
            System.exit(1);
        } catch(EngineException e) {
            System.err.println("Engine error: "+e.getMessage());
            System.exit(1);
        }
    }
}

