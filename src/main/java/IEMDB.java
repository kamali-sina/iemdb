import app.Application;
import handler.CommandHandler;
import handler.InputHandler;
import handler.OutputHandler;
import input.DataReader;

import java.util.ArrayList;

public class IEMDB {

    public static void main(String[] args) {
        DataReader.readDataFromUrl("http://138.197.181.131:5000");
        Application.initialize();
        try {
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
