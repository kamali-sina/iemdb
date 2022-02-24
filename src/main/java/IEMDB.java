import java.util.ArrayList;

public class IEMDB {
    private static final ActorManager actorManager = new ActorManager();
    private static final MovieManager movieManager = new MovieManager();
    private static final UserManager userManager = new UserManager();

    public static void main(String[] args) {
        try {
            while (true) {
                ArrayList<String> commandArgs = InputHandler.handleInput();
                String commandName = commandArgs.get(0);
                String commandData = commandArgs.get(1);

                ArrayList<String> results =
                        CommandHandler.handleCommand(actorManager, movieManager, userManager, commandName, commandData);
                OutputHandler.handleOutput(results);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
