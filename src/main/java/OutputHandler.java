import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class OutputHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void handleOutput(ArrayList<String> results) throws JsonProcessingException {
        Output output = new Output(results.get(0), results.get(1));
        System.out.println(objectMapper.writeValueAsString(output));
    }
}
