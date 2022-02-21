import java.util.Scanner;

public class IEMDB {
    public static void main(String[] args) {
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();

                int commandLength = line.indexOf(" ");
                String command = line.substring(0, commandLength);
                String JSONData = line.substring(commandLength).trim();
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
