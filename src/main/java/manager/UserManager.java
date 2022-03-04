package manager;

import exception.CommandException;
import exception.ErrorType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import main.*;
import input.*;

public class UserManager {
    public static final HashMap<String, User> users = new HashMap<>();

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
        return "\"user added successfully\"";
    }

    public static String addUsers(List<User> users) throws CommandException {
        for (User user : users) {
            UserManager.addUser(user);
        }
        return "\"users added successfully\"";
    }

    public static String addToWatchList(WatchListItem watchListItem) throws CommandException {
        User user = getUser(watchListItem.getUserEmail());
        Movie movie = MovieManager.getMovie(watchListItem.getMovieId());

        user.addToWatchList(movie);
        return "\"movie added to watchlist successfully\"";
    }

    public static String removeFromWatchList(WatchListItem watchListItem) throws CommandException {
        User user = getUser(watchListItem.getUserEmail());
        Movie movie = MovieManager.getMovie(watchListItem.getMovieId());

        user.removeFromWatchList(movie);
        return "\"movie removed from watchlist successfully\"";
    }

    public static String addVote(Vote vote) throws CommandException {
        UserManager.getUser(vote.getUserEmail());

        for (Movie movie : MovieManager.movies.values()) {
            Comment comment = movie.findComment(vote.getCommentId());
            if (comment != null) {
                return comment.addVote(vote);
            }
        }
        throw new CommandException(ErrorType.CommentNotFound);
    }

    public static String getWatchList(GetWatchListInput showWatchListInput) throws CommandException, IOException {
        User user = UserManager.getUser(showWatchListInput.getUserEmail());
        return user.getSerializedWatchlist();
    }
}
