import java.io.IOException;
import java.util.ArrayList;

public class OutputHandler {
    public static void handleOutput(ArrayList<String> results) throws IOException {
        Output output = new Output(results.get(0), results.get(1));
        OutputHandler.printOutput(output);
    }

    public static void printOutput(Output output) throws IOException {
        System.out.println(output.getSerializedOutput());
    }
}
