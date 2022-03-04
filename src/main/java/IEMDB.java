import app.Application;
import handler.CommandHandler;
import handler.InputHandler;
import handler.OutputHandler;
import input.DataReader;

import java.util.ArrayList;

public class IEMDB {
    private static final String externalServicesUrl = "http://138.197.181.131:5000";

    public static void main(String[] args) {
        try {
            DataReader.readDataFromUrl(externalServicesUrl);
            Application.initialize();

            while (true) {
                ArrayList<String> commandArgs = InputHandler.handleInput();
                String commandName = commandArgs.get(0);
                String commandData = commandArgs.get(1);

                ArrayList<String> results =
                        CommandHandler.handleCommand(commandName, commandData);
                OutputHandler.handleOutput(results);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
