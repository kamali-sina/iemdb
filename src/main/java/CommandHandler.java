import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class CommandHandler {
    private static final String EMPTY_STRING = "";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ArrayList<String> handleCommand(ActorManager actorManager, MovieManager movieManager,
                                                  UserManager userManager, String name, String data) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> results = new ArrayList<>();

        switch (name) {
            case EMPTY_STRING:
                break;
            case "addActor":
                Actor new_actor = objectMapper.readValue(data, Actor.class);
                new_actor.printData();
                break;
            case "addMovie":
                Movie movie = objectMapper.readValue(data, Movie.class);
                // TODO: If the actors did not exist the corresponding error message must be shown and movie should not be added.
                MovieManager.addMovie(movie);
                break;
            case "addUser":
                User new_user = objectMapper.readValue(data, User.class);
                new_user.printData();
                break;
            case "addComment":
                Comment comment = objectMapper.readValue(data, Comment.class);
                // TODO: If the user did not exist the corresponding error message must be shown.
                MovieManager.addComment(comment);
                break;
            case "rateMovie":
                Rating rating = objectMapper.readValue(data, Rating.class);
                // TODO: Rating should be added to corresponding movie.
                // TODO: Check rating value to be between 1 and 10
                // TODO: If value was wrong the corresponding error message must be shown.
                // TODO: Recalculate movie's average rating
                // TODO: Check if user has rated the movie before, replace rating and update average rating
                // TODO: If the movie did not exist the corresponding error message must be shown.
                // TODO: If the actor did not exist the corresponding error message must be shown.
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

        return results;
    }
}
