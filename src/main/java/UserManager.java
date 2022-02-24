import exception.CommandException;
import exception.ErrorType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {
    static final HashMap<String, User> users = new HashMap<>();

    public static User getUser(String userEmail) throws CommandException {
        User user = UserManager.users.get(userEmail);
        if (user == null) {
            throw new CommandException(ErrorType.UserNotFound);
        }
        return user;
    }

    public static boolean doesUserExist(String userEmail) {
        return UserManager.users.containsKey(userEmail);
    }

    public static String addUser(User user) throws CommandException {
        if (UserManager.doesUserExist(user.getEmail())) {
            throw new CommandException(ErrorType.UserAlreadyExists);
        }

        UserManager.users.put(user.getEmail(), user);
         return  "user added successfully";
    }

    public static String addToWatchList(WatchList watchList) throws CommandException {
        User user = getUser(watchList.getUserEmail());
        Movie movie = MovieManager.getMovie(watchList.getMovieId());

        user.addToWatchList(movie);
        return "movie added to watchlist successfully";
    }

    public static String removeFromWatchList(WatchList watchList) throws CommandException {
        User user = getUser(watchList.getUserEmail());
        Movie movie = MovieManager.getMovie(watchList.getMovieId());

        user.removeFromWatchList(movie);
        return "movie removed from watchlist successfully";
    }

    public static String addVote(Vote vote) throws CommandException {
        UserManager.getUser(vote.getUserEmail());

        for (Movie movie : MovieManager.movies.values()) {
            for (ArrayList<Comment> userComments : movie.getComments().values()) {
                for (Comment comment : userComments) {
                    if (comment.getId() == Integer.parseInt(vote.getCommentId())) {
                        return comment.addVote(vote);
                    }
                }
            }
        }
        throw new CommandException(ErrorType.CommentNotFound);
    }

    public static String getWatchList(ShowWatchListInput showWatchListInput) throws CommandException, IOException {
        User user = UserManager.getUser(showWatchListInput.getUserEmail());
        return user.getSerializedWatchlist();
    }
}
