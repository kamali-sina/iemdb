import java.util.Scanner;

public class InputHandler {
    public static void handleInput() {
        while (true) {
            try {
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

                CommandHandler.handleCommand(commandName, commandData);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
