import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import exception.*;

public class CommandHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ArrayList<String> handleCommand(String CommandName, String CommandData) throws Exception {
        ArrayList<String> results = new ArrayList<>();

        try {
            switch (CommandName) {
                case "addActor" -> {
                    Actor new_actor = objectMapper.readValue(CommandData, Actor.class);
                    results.add(ActorManager.addActor(new_actor));
                }
                case "addMovie" -> {
                    Movie movie = objectMapper.readValue(CommandData, Movie.class);
                    results.add(MovieManager.addMovie(movie));
                }
                case "addUser" -> {
                    User new_user = objectMapper.readValue(CommandData, User.class);
                    results.add(UserManager.addUser(new_user));
                }
                case "addComment" -> {
                    Comment comment = objectMapper.readValue(CommandData, Comment.class);
                    results.add(MovieManager.addComment(comment));
                }
                case "rateMovie" -> {
                    Rating rating = objectMapper.readValue(CommandData, Rating.class);
                    results.add(MovieManager.addRating(rating));
                }
                case "voteComment" -> {
                    Vote vote = objectMapper.readValue(CommandData, Vote.class);
                    results.add(UserManager.addVote(vote));
                }
                case "addToWatchList" -> {
                    WatchList watchListItem = objectMapper.readValue(CommandData, WatchList.class);
                    results.add(UserManager.addToWatchList(watchListItem));
                }
                case "removeFromWatchList" -> {
                    WatchList watchListItemToRemove = objectMapper.readValue(CommandData, WatchList.class);
                    results.add(UserManager.removeFromWatchList(watchListItemToRemove));
                }
                case "getMoviesList" -> results.add(MovieManager.getMoviesList());
                case "getMovieById" -> {
                    GetMovieByIdInput getMovieByIdInput = objectMapper.readValue(CommandData, GetMovieByIdInput.class);
                    results.add(MovieManager.getMovieById(getMovieByIdInput));
                }
                case "getMoviesByGenre" -> {
                    GetMoviesByGenreInput getMoviesByGenreInput =
                            objectMapper.readValue(CommandData, GetMoviesByGenreInput.class);
                    results.add(MovieManager.getMoviesByGenre(getMoviesByGenreInput));
                }
                case "getWatchList" -> {
                    GetWatchListInput getWatchListInput = objectMapper.readValue(CommandData, GetWatchListInput.class);
                    results.add(UserManager.getWatchList(getWatchListInput));
                }
                default -> throw new CommandException(ErrorType.InvalidCommand);
            }
        } catch (Exception exception) {
            results.add("false");
            results.add(exception.getMessage());
            return results;
        }
        results.add(0, "true");
        return results;
    }
}
