import java.io.*;
public class Evaluator {
    public static void main(String[] args) throws Exception {

        File file = new File("/Users/alicjakrupczynska/IdeaProjects/task12/src/example.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                //delete '=' from each equation
                line=line.substring(0, line.length()-1);
                String command="print(eval('"+line+"'))";

                //evaluating the equations - used python3 since it works on MacOS
                Process process = new ProcessBuilder("python3", "-c", command).start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    System.out.println(reader.readLine());
                }
            }
        }
    }
}