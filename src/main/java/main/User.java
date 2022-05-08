package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;
import manager.MovieManager;
import manager.UserManager;
import repository.ConnectionPool;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class User {
    public static final int RECOMMENDATION_COUNT = 3;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String birthDate;

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("nickname") String nickname,
                @JsonProperty("name") String name,
                @JsonProperty("birthDate") String birthDate) throws CommandException {
        if (email == null ||
                password == null ||
                nickname == null ||
                name == null ||
                birthDate == null) {
            throw new CommandException(ErrorType.InvalidCommand);
        }
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public ArrayList<Movie> getWatchList() {
        ArrayList<Movie> watchlist = new ArrayList<>();
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Movies inner join WatchlistItems on Movies.id = WatchlistItems.movieId and WatchlistItems.userEmail = \"" + UserManager.loggedInUser.getEmail() + "\"");

            while (result.next()) {
                Movie movie = new Movie(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("summery"),
                        result.getString("releaseDate"),
                        result.getString("director"),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        result.getFloat("imdbRate"),
                        result.getInt("duration"),
                        result.getInt("ageLimit"),
                        result.getString("image"),
                        result.getString("coverImage")
                );
                watchlist.add(movie);
            }
            result.close();
            stmt.close();
            con.close();
            return watchlist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Movie> getMoviesNotInWatchlist() {
        ArrayList<Movie> movies_not_in_watchlist = new ArrayList<>();
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Movies inner join WatchlistItems on Movies.id = WatchlistItems.movieId and WatchlistItems.userEmail != \"" + UserManager.loggedInUser.getEmail() + "\"");

            while (result.next()) {
                Movie movie = new Movie(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("summery"),
                        result.getString("releaseDate"),
                        result.getString("director"),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        result.getFloat("imdbRate"),
                        result.getInt("duration"),
                        result.getInt("ageLimit"),
                        result.getString("image"),
                        result.getString("coverImage")
                );
                movies_not_in_watchlist.add(movie);
            }
            result.close();
            stmt.close();
            con.close();
            return movies_not_in_watchlist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    public int getAge() {
        LocalDate dateOfBirth = LocalDate.parse(getBirthDate());
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }

    public void addToWatchList(Integer movieId) throws CommandException, SQLException {
        Movie movie = MovieManager.getMovie(movieId);
        if (UserManager.loggedInUser.getAge() < movie.getAgeLimit()) {
            throw new CommandException(ErrorType.AgeLimitError);
        }
        for (Movie watchlistMovie : UserManager.loggedInUser.getWatchList()) {
            if (watchlistMovie.getId() == movieId) {
                throw new CommandException(ErrorType.MovieAlreadyExists);
            }
        }
        Connection con = ConnectionPool.getConnection();
        PreparedStatement stmt = con.prepareStatement("INSERT INTO WatchlistItems VALUES (?, ?) on duplicate key update movieId = movieId, userEmail = userEmail");
        stmt.setString(1, UserManager.loggedInUser.getEmail());
        stmt.setInt(2, movieId);
        stmt.addBatch();
        int[] result = stmt.executeBatch();
        stmt.close();
    }

    public void removeFromWatchList(Integer movieId) throws CommandException {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from WatchlistItems where movieId = \"" + movieId + "\" and userEmail = \"" + UserManager.getLoggedInUser().getEmail() + "\"");
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
            throw new CommandException(ErrorType.MovieNotFound);
        }
    }

    private Float getMovieGenreSimilarity(Movie movie) {
        Float score = movie.getImdbRate();
        for (Movie watchlist_movie : getWatchList()) {
            score += MovieManager.countNumberOfSameGenres(movie, watchlist_movie);
        }
        return score;
    }

    private Float getMovieRecommendationScore(Movie movie) {
        Float score = movie.getImdbRate();
        if (movie.getAverageRatingRate() != null) {
            score += movie.getAverageRatingRate();
        }
        score += 3 * getMovieGenreSimilarity(movie);
        return score;
    }

    public List<Movie> getWatchlistRecommendations() {
        ArrayList<Movie> movies_not_in_watchlist = getMoviesNotInWatchlist();
        for (Movie movie : movies_not_in_watchlist) {
            movie.setRecommendationScore(getMovieRecommendationScore(movie));
        }
        movies_not_in_watchlist.sort(Comparator.comparing(Movie::getRecommendationScore).reversed());
        if (movies_not_in_watchlist.size() < RECOMMENDATION_COUNT) {
            return  movies_not_in_watchlist;
        }
        return movies_not_in_watchlist.subList(0, RECOMMENDATION_COUNT);
    }

    public void addVoteToComment(Integer commentId, Integer vote) throws CommandException {
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Votes VALUES (?, ?, ?) on duplicate key update vote = vote");
            stmt.setInt(1, commentId);
            stmt.setString(2, UserManager.loggedInUser.getEmail());
            stmt.setInt(3, vote);
            stmt.addBatch();
            int[] result = stmt.executeBatch();
            stmt.close();
        } catch (Exception e) {
            throw new CommandException(ErrorType.CommentNotFound);
        }
    }
}
