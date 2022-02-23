import java.util.HashMap;

public class MovieManager {
    static final HashMap<Integer, Movie> movies = new HashMap<>();

    public static Movie getMovie(Integer movieId) {
        return MovieManager.movies.get(movieId);
    }

    public static void addMovie(Movie movie) {
        MovieManager.movies.put(movie.getId(), movie);
    }
}
