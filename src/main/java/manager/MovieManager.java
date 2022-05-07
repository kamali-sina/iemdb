package manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import exception.CommandException;
import exception.ErrorType;
import main.Actor;
import main.Comment;
import main.Movie;
import main.Rating;
import repository.ConnectionPool;

import java.sql.*;
import java.util.*;

public class MovieManager {
    public static final HashMap<Integer, Movie> movies = new HashMap<>();
    public static final ArrayList<String> filters = new ArrayList<>(Arrays.asList("sortByImdb", "sortByDate"));

    public static String query = "";
    public static String filter = "sortByImdb";

    public static Set<String> sortedBySet = new HashSet<>(Arrays.asList("sortByDate", "sortByImdb"));


    public static String getQuery() {
        return query;
    }

    public static void setQuery(String inputQuery) {
        query = inputQuery;
    }

    public static void clearQuery() {
        query = "";
    }

    public static String getFilter() {
        return filter;
    }

    public static void setFilter(String inputFilter) {
        if (filters.contains(inputFilter)) {
            filter = inputFilter;
        }
    }

    public static ArrayList<String> getGenres(Integer movieId) {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from MovieGenres where movieId = \"" + movieId + "\"");

            ArrayList<String> Genres = new ArrayList<>();
            while (result.next()) {
                Genres.add(result.getString("genre"));
            }
            result.close();
            stmt.close();
            con.close();

            return Genres;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Integer> getCast(Integer movieId) {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from ActorMovies where movieId = \"" + movieId + "\"");

            ArrayList<Integer> cast = new ArrayList<>();
            while (result.next()) {
                cast.add(result.getInt("actorId"));
            }
            result.close();
            stmt.close();
            con.close();

            return cast;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Movie> getMovies() {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from movies");

            ArrayList<Movie> movies = new ArrayList<>();
            while (result.next()) {
                Integer movieId = result.getInt("id");
                ArrayList<String> writers = new ArrayList<>(Arrays.asList(result.getString("writers").split("\\s*,\\s*")));
                ArrayList<String> genres = getGenres(movieId);
                ArrayList<Integer> cast = getCast(movieId);

                Movie movie = new Movie(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("summery"),
                        result.getString("releaseDate"),
                        result.getString("director"),
                        writers,
                        genres,
                        cast,
                        result.getFloat("imdbRate"),
                        result.getInt("duration"),
                        result.getInt("ageLimit"),
                        result.getString("image"),
                        result.getString("coverImage")
                );

                movies.add(movie);
            }
            result.close();
            stmt.close();
            con.close();

            return movies;
        }
        catch (SQLException | CommandException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Movie getMovie(Integer movieId) throws CommandException {
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Movies where id = \"" + movieId + "\"");

            if (result.next()) {
                ArrayList<String> writers = new ArrayList<>(Arrays.asList(result.getString("writers").split("\\s*,\\s*")));
                ArrayList<String> genres = getGenres(movieId);
                ArrayList<Integer> cast = getCast(movieId);

                Movie movie = new Movie(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("summery"),
                        result.getString("releaseDate"),
                        result.getString("director"),
                        writers,
                        genres,
                        cast,
                        result.getFloat("imdbRate"),
                        result.getInt("duration"),
                        result.getInt("ageLimit"),
                        result.getString("image"),
                        result.getString("coverImage")
                );
                result.close();
                stmt.close();
                con.close();

                return movie;
            }
        }
        catch (SQLException | CommandException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addMovie(Movie movie) throws CommandException {
        String response = "\"movie added successfully\"";

        for (Integer actorId : movie.getCast()) {
            ActorManager.getActor(actorId);
        }

        if (MovieManager.movies.containsKey(movie.getId())) {
            response = "\"movie updated successfully\"";
        }

        MovieManager.movies.put(movie.getId(), movie);
        return response;
    }

    public static String addMovies(List<Movie> movies) throws CommandException {
        for (Movie movie : movies) {
            MovieManager.addMovie(movie);
        }
        return "\"movies added successfully\"";
    }

    public static String addComment(Comment comment) throws CommandException {
        UserManager.getUser(comment.getUserEmail());
        Movie movie = getMovie(comment.getMovieId());

        Integer commentId = movie.addComment(comment);
        return "\"comment with id " + commentId.toString() + " added successfully\"";
    }

    public static String addComments(List<Comment> comments) throws CommandException {
        for (Comment comment : comments) {
            MovieManager.addComment(comment);
        }
        return "\"comments added successfully\"";
    }

    public static String addRating(Rating rating) throws CommandException {
        UserManager.getUser(rating.getUserEmail());
        Movie movie = getMovie(rating.getMovieId());

        movie.addRating(rating);
        return "\"movie rated successfully\"";
    }

    public static ArrayList<Movie> getMoviesByGenre(String genre) {

        ArrayList<Movie> moviesByGenre = new ArrayList<>();
        for (Movie movie : MovieManager.movies.values()) {
            if (movie.getGenres().contains(genre)) {
                moviesByGenre.add(movie);
            }
        }

        return moviesByGenre;
    }

    public static ArrayList<Movie> getMoviesByReleaseYear(Integer startYear, Integer endYear) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (Movie movie : MovieManager.movies.values()) {
            Integer year = movie.calculateReleaseYear();
            if (year == -1) {
                continue;
            }
            if (startYear <= year && year <= endYear) {
                movies.add(movie);
            }
        }

        return movies;
    }

    public static ArrayList<Movie> getActorMovies(Integer actorId) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (Movie movie : MovieManager.movies.values()) {
            if (movie.getCast().contains(actorId)) {
                movies.add(movie);
            }
        }

        return movies;
    }

    public static ArrayList<Movie> searchMovies(String input) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (Movie movie : MovieManager.movies.values()) {
            if (movie.getName().contains(input)) {
                movies.add(movie);
            }
        }

        return movies;
    }

    public static ArrayList<Movie> sortMovies(ArrayList<Movie> movies, String filter) {
        if (filter.equals("sortByImdb")) {
            movies.sort(Comparator.comparing(Movie::getImdbRate).reversed());
        } else if (filter.equals("sortByDate")) {
            movies.sort(Comparator.comparing(Movie::calculateLocalReleaseDate).reversed());
        }

        return movies;
    }

    public static ArrayList<Movie> getSearchMoviesResultSorted(String input, String filter) {
        return sortMovies(searchMovies(input), filter);
    }

    public static Integer countNumberOfSameGenres(Movie movie_1, Movie movie_2) {
        Integer count = 0;
        for (String genre_1 : movie_1.getGenres()) {
            for (String genre_2 : movie_2.getGenres()) {
                if (genre_1.equals(genre_2)) {
                    count += 1;
                    break;
                }
            }
        }
        return count;
    }

    public static Comment findComment(Integer commentId) {
        for (Movie movie: MovieManager.movies.values()) {
            Comment foundComment = movie.findComment(commentId);
            if (foundComment != null) {
                return foundComment;
            }
        }
        return null;
    }

    public static ArrayList<Actor> getMovieActors(Integer movieId) throws CommandException {
        Movie movie = MovieManager.getMovie(movieId);

        ArrayList<Actor> actors = new ArrayList<>();
        for (Integer actorId : movie.getCast()) {
            actors.add(ActorManager.getActor(actorId));
        }
        return actors;
    }

    public static ArrayList<Comment> getMovieComments(Integer movieId) throws CommandException {
        Movie movie = MovieManager.getMovie(movieId);

        HashMap<String, ArrayList<Comment>> comments = movie.getComments();
        ArrayList<Comment> movieComments = new ArrayList<>();
        for (String key : comments.keySet()) {
            movieComments.addAll(comments.get(key));
        }
        return movieComments;
    }
}
