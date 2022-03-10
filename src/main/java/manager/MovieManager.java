package manager;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import exception.CommandException;
import exception.ErrorType;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import main.*;
import input.*;

public class MovieManager {
    public static final HashMap<Integer, Movie> movies = new HashMap<>();
    public static final ArrayList<String> filters = new ArrayList<>(Arrays.asList("sortByImdb", "sortByDate"));

    public static String query = "";
    public static String filter = "sortByImdb";


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

    public static Movie getMovie(Integer movieId) throws CommandException {
        Movie movie = MovieManager.movies.get(movieId);
        if (movie == null) {
            throw new CommandException(ErrorType.MovieNotFound);
        }
        return movie;
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

    public static String getMoviesList() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeArrayFieldStart("MoviesList");
        Collection<Movie> allMovies = MovieManager.movies.values();
        for (Movie movie : allMovies) {
            jsonGenerator.writeRawValue(movie.getSerializedMovieSummary());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
    }

    public static String getMovieById(GetMovieByIdInput getMovieByIdInput) throws IOException, CommandException {
        Integer movieId = getMovieByIdInput.getMovieId();
        return MovieManager.getMovie(movieId).getSerializedMovieWithDetails();
    }

    public static String serializeMoviesListByGenre(ArrayList<Movie> movies) throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeArrayFieldStart("MoviesListByGenre");
        for (Movie movie : movies) {
            jsonGenerator.writeRawValue(movie.getSerializedMovieSummary());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
    }

    public static ArrayList<Movie> getMoviesByGenre(GetMoviesByGenreInput getMoviesByGenreInput) {
        String genre = getMoviesByGenreInput.getGenre();

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
            Integer year = movie.getReleaseYear();
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
            movies.sort(Comparator.comparing(Movie::getLocalReleaseDate).reversed());
        }

        return movies;
    }

    public static ArrayList<Movie> getSearchMoviesResultSorted(String input, String filter) {
        return sortMovies(searchMovies(input), filter);
    }
}
