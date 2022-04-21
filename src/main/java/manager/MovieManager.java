package manager;

import exception.CommandException;
import exception.ErrorType;
import main.Comment;
import main.Movie;
import main.Rating;

import java.util.*;

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
}
