import java.util.ArrayList;
import java.util.Scanner;

public class InputHandler {
    public static ArrayList<String> handleInput() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        String commandName = "";
        String commandData = "";

        if (line.contains(" ")) {
            int commandLength = line.indexOf(" ");
            commandName = line.substring(0, commandLength);
            commandData = line.substring(commandLength).trim();
        } else {
            commandName = line;
        }

        ArrayList<String> commandArgs = new ArrayList<>();
        commandArgs.add(commandName);
        commandArgs.add(commandData);
        return commandArgs;
    }
}
