package manager;

import exception.CommandException;
import exception.ErrorType;
import main.*;
import repository.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class UserManager {
    public static User loggedInUser = null;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logInUser(User user) {
        loggedInUser = user;
    }

    public static void logOutUser() {
        loggedInUser = null;
    }

    public static User getUser(String email) throws CommandException {
        User user = null;
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Users where email = \"" + email + "\"");
            while (result.next()) {
                user = new User(
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("nickname"),
                        result.getString("name"),
                        result.getString("birthDate")
                );
            }
            result.close();
            stmt.close();
            con.close();
            if (user == null) {
                throw new CommandException(ErrorType.UserNotFound);
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User getUser(String email, String password) throws CommandException {
        User user = null;
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Users where email = \"" + email + "\" and password = \"" + password + "\"");
            while (result.next()) {
                user = new User(
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("nickname"),
                        result.getString("name"),
                        result.getString("birthDate")
                );
            }
            result.close();
            stmt.close();
            con.close();
            if (user == null) {
                throw new CommandException(ErrorType.UserNotFound);
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}
