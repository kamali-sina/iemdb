import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandHandler {
    private static final String EMPTY_STRING = "";

    public static void handleCommand(String name, String data) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        switch (name) {
            case EMPTY_STRING:
                break;
            case "addActor":
                Actor new_actor = objectMapper.readValue(data, Actor.class);
                new_actor.printData();
                break;
            case "addMovie":
                // code block
                break;
            case "addUser":
                User new_user = objectMapper.readValue(data, User.class);
                new_user.printData();
                break;
            case "addComment":
                // code block
            case "rateMovie":
                // code block
                break;
            case "voteComment":
                // code block
                break;
            case "addToWatchList":
                // code block
                break;
            case "removeFromWatchList":
                // code block
                break;
            case "getMoviesList":
                // code block
                break;
            case "getMovieById":
                // code block
                break;
            case "getMoviesByGenre":
                // code block
                break;
            case "getWatchList":
                // code block
                break;
            default:
                throw new Exception("Command not found");
        }
    }
}
