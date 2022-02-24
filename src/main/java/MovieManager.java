import java.util.HashMap;

public class MovieManager {
    static final HashMap<Integer, Movie> movies = new HashMap<>();

    public static Movie getMovie(Integer movieId) {
        return MovieManager.movies.get(movieId);
    }

    public static void addMovie(Movie movie) {
        MovieManager.movies.put(movie.getId(), movie);
        // TODO: Add response
        // return "movie added successfully";
    }

    public static void addComment(Comment comment) {
        // TODO: If the movie did not exist the corresponding error message must be shown.
        Movie movie = getMovie(comment.getMovieId());
        movie.addComment(comment);
        // return "comment with id " + comment.getCommentId().toString() + " added successfully";
    }

    public static void addRating(Rating rating) {
        // TODO: If the movie did not exist the corresponding error message must be shown.
        Movie movie = getMovie(rating.getMovieId());
        movie.addRating(rating);
    }
}
