import input.DataReader;

import java.util.ArrayList;

public class IEMDB {
    private static final String externalServicesUrl = "http://138.197.181.131:5000";

    public static void main(String[] args) {
        try {
            DataReader.readDataFromUrl(externalServicesUrl);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
