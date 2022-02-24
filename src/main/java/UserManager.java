import java.util.HashMap;

public class UserManager {
    static final HashMap<String, User> users = new HashMap<>();

    public static User getUser(String userEmail) {
        User user = UserManager.users.get(userEmail);
        if (user == null) {
//            TODO: throw
        }
        return user;
    }

    public static boolean doesUserExist(String userEmail) {
        return UserManager.users.containsKey(userEmail);
    }

    public static String addUser(User user) {
        String response = "user added successfully";
        if (UserManager.doesUserExist(user.getEmail())) {
            response = "user updated successfully";
        }
        UserManager.users.put(user.getEmail(), user);
         return response;
    }

    public static String addToWatchList(WatchList watchList) {
        User user = getUser(watchList.getUserEmail());
        Movie movie = MovieManager.getMovie(watchList.getMovieId());
        user.addToWatchList(movie);
        return "movie added to watchlist";
    }

    public static String removeFromWatchList(WatchList watchList) {
        User user = getUser(watchList.getUserEmail());
        Movie movie = MovieManager.getMovie(watchList.getMovieId());
        user.removeFromWatchList(movie);
        return "movie removed from watchlist";
    }


}
