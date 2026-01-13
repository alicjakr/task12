import java.io.*;
public class Evaluator {
    public static void main(String[] args) throws Exception {
        Process process = new ProcessBuilder(
                "python3",
                "-c",
                "print(eval('1+(2*3)'))"
        ).start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            System.out.println(reader.readLine());
        }
    }
}