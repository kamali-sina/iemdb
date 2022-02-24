import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import exception.*;

public class CommandHandler {
    private static final String EMPTY_STRING = "";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ArrayList<String> handleCommand(ActorManager actorManager, MovieManager movieManager,
                                                  UserManager userManager, String name, String data) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> results = new ArrayList<>();

        try {
            switch (name) {
                case EMPTY_STRING:
                    break;
                case "addActor":
                    Actor new_actor = objectMapper.readValue(data, Actor.class);
                    results.add(ActorManager.addActor(new_actor));
                    break;
                case "addMovie":
                    Movie movie = objectMapper.readValue(data, Movie.class);
                    results.add(MovieManager.addMovie(movie));
                    break;
                case "addUser":
                    User new_user = objectMapper.readValue(data, User.class);
                    results.add(UserManager.addUser(new_user));
                    break;
                case "addComment":
                    Comment comment = objectMapper.readValue(data, Comment.class);
                    results.add(MovieManager.addComment(comment));
                    break;
                case "rateMovie":
                    Rating rating = objectMapper.readValue(data, Rating.class);
                    results.add(MovieManager.addRating(rating));
                    break;
                case "voteComment":
                    Vote vote = objectMapper.readValue(data, Vote.class);
                    results.add(UserManager.addVote(vote));
                    break;
                case "addToWatchList":
                    WatchList watchListItem = objectMapper.readValue(data, WatchList.class);
                    results.add(UserManager.addToWatchList(watchListItem));
                    break;
                case "removeFromWatchList":
                    WatchList watchListItemToRemove = objectMapper.readValue(data, WatchList.class);
                    results.add(UserManager.removeFromWatchList(watchListItemToRemove));
                    break;
                case "getMoviesList":
                    results.add(MovieManager.getMoviesList());
                    break;
                case "getMovieById":
                    // code block
                    break;
                case "getMoviesByGenre":
                    // code block
                    break;
                case "getWatchList":
                    ShowWatchListInput showWatchListInput = objectMapper.readValue(data, ShowWatchListInput.class);
                    results.add(UserManager.getWatchList(showWatchListInput));
                    break;
                default:
                    throw new CommandException(ErrorType.InvalidCommand);
            }
        } catch (CommandException commandException) {
            results.add("false");
            results.add(commandException.getMessage());
            return results;
        }
        results.add(0, "true");
        return results;
    }
}
