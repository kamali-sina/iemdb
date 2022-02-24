import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import exception.CommandException;
import exception.ErrorType;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;

public class MovieManager {
    static final HashMap<Integer, Movie> movies = new HashMap<>();

    public static Movie getMovie(Integer movieId) throws CommandException {
        Movie movie =  MovieManager.movies.get(movieId);
        if (movie == null) {
            throw new CommandException(ErrorType.MovieNotFound);
        }
        return movie;
    }

    public static String addMovie(Movie movie) throws CommandException {
        String response = "movie added successfully";

        for (Integer actorId : movie.getCast()) {
            ActorManager.getActor(actorId);
        }

        if (MovieManager.movies.containsKey(movie.getId())) {
            response = "movie updated successfully";
        }

        MovieManager.movies.put(movie.getId(), movie);
        return response;
    }

    public static String addComment(Comment comment) throws CommandException {
        UserManager.getUser(comment.getUserEmail());
        Movie movie = getMovie(comment.getMovieId());

        Integer commentId = movie.addComment(comment);
        return "comment with id " + commentId.toString() + " added successfully";
    }

    public static String addRating(Rating rating) throws CommandException {
        UserManager.getUser(rating.getUserEmail());
        Movie movie = getMovie(rating.getMovieId());

        movie.addRating(rating, movie);
        return "movie rated successfully";
    }

    public static String getMoviesList() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeArrayFieldStart("MoviesList");
        Collection<Movie> allMovies = MovieManager.movies.values();
        for (Movie movie: allMovies) {
            //TODO: get summery
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
    }

    public static String getMoviesByGenre() throws IOException {
        String genre = ""; //TODO: get this
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeArrayFieldStart("MoviesListByGenre");
        Collection<Movie> allMovies = MovieManager.movies.values();
        for (Movie movie: allMovies) {
            if (movie.getGenres().contains(genre)) {
                //TODO: get summery
//                jsonGenerator.writeRawValue();
            }
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return jsonObjectWriter.toString();
    }
}
