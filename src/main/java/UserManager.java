import exception.CommandException;
import exception.ErrorType;

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
}
