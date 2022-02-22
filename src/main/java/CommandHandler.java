public class CommandHandler {
    private static final String EMPTY_STRING = "";

    public static void handleCommand(String name, String data) throws Exception {
        switch (name) {
            case EMPTY_STRING:
                break;
            case "addActor":
                // code block
                break;
            case "addMovie":
                // code block
                break;
            case "addUser":
                // code block
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
