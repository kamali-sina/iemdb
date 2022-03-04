import exception.CommandException;
import input.GetMoviesByGenreInput;
import main.Movie;
import main.Rating;
import main.User;
import manager.MovieManager;
import manager.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MovieManagerTest {
    private static void createMovie(Integer movieId, String movieName, ArrayList<String> genres, Integer releaseYear) throws CommandException {
        Movie movie = new Movie(movieId, movieName,
                "", releaseYear.toString() + "-01-01",
                "", new ArrayList<>(), genres,
                new ArrayList<>(), 8.2, 150, 10);
        MovieManager.addMovie(movie);
    }

    @BeforeEach
    public void setUp() throws CommandException {
        ArrayList<String> avengersMovieGenres = new ArrayList<>();
        avengersMovieGenres.add("Action");
        avengersMovieGenres.add("Adventure");
        avengersMovieGenres.add("Superhero");

        ArrayList<String> lordOfTheRingsMovieGenres = new ArrayList<>();
        lordOfTheRingsMovieGenres.add("Action");
        lordOfTheRingsMovieGenres.add("Adventure");
        lordOfTheRingsMovieGenres.add("Fantasy");

        createMovie(0, "Avengers", avengersMovieGenres, 2012);
        createMovie(1, "Lord of The Rings", lordOfTheRingsMovieGenres, 2001);

        User young_user = new User("young@ut.ir", "young pass", "young", "Jane Doe"
                , "2010-01-01");
        UserManager.addUser(young_user);

        User old_user = new User("old@ut.ir", "old pass", "old", "John Doe"
                , "2010-01-01");
        UserManager.addUser(old_user);
    }

    @ParameterizedTest(name = "find movie(s) by {0} genre")
    @MethodSource("provideMovies")
    @DisplayName("Should return movies by genre")
    public void shouldReturnMoviesByGenre(String genre, ArrayList<Movie> movies) throws IOException, CommandException {
        GetMoviesByGenreInput getMoviesByGenreInput = new GetMoviesByGenreInput(genre);

        ArrayList<Movie> moviesByGenre = MovieManager.getMoviesByGenre(getMoviesByGenreInput);
        String moviesListByGenre = MovieManager.serializeMoviesListByGenre(moviesByGenre);

        assertEquals(MovieManager.serializeMoviesListByGenre(movies), moviesListByGenre);
    }

    public static Collection<Object[]> provideMovies() throws CommandException {
        ArrayList<Movie> superheroMovies = new ArrayList<>();
        ArrayList<Movie> fantasyMovies = new ArrayList<>();
        ArrayList<Movie> actionMovies = new ArrayList<>();

        ArrayList<String> avengersMovieGenres = new ArrayList<>();
        avengersMovieGenres.add("Action");
        avengersMovieGenres.add("Adventure");
        avengersMovieGenres.add("Superhero");

        ArrayList<String> lordOfTheRingsMovieGenres = new ArrayList<>();
        lordOfTheRingsMovieGenres.add("Action");
        lordOfTheRingsMovieGenres.add("Adventure");
        lordOfTheRingsMovieGenres.add("Fantasy");

        createMovie(0, "Avengers", avengersMovieGenres, 2012);
        createMovie(1, "Lord of The Rings", lordOfTheRingsMovieGenres, 2001);

        for (Movie movie : MovieManager.movies.values()) {
            if (movie.getGenres().contains("Superhero")) {
                superheroMovies.add(movie);
            }
            if (movie.getGenres().contains("Fantasy")) {
                fantasyMovies.add(movie);
            }
            if (movie.getGenres().contains("Action")) {
                actionMovies.add(movie);
            }
        }

        return Arrays.asList(new Object[][]{{"Superhero", superheroMovies},
                {"Fantasy", fantasyMovies}, {"Action", actionMovies}});
    }

    @Test
    @DisplayName("Should return movies in drama genre when no movies exist in that genre")
    public void shouldReturnMoviesInDramaGenreWhenNoMoviesExistInThatGenre() throws IOException, CommandException {
        GetMoviesByGenreInput getMoviesByGenreInput = new GetMoviesByGenreInput("drama");

        ArrayList<Movie> moviesByGenre = MovieManager.getMoviesByGenre(getMoviesByGenreInput);
        String movies = MovieManager.serializeMoviesListByGenre(moviesByGenre);

        String expectedMoviesInAdventureGenre = "{\"MoviesListByGenre\":[]}";

        assertEquals(expectedMoviesInAdventureGenre, movies);
    }

    @Test
    @DisplayName("Should throw an exception if movie which is being rated does not exist")
    public void shouldThrowAnExceptionIfMovieWhichIsBeingRatedDoesNotExist() throws CommandException {
        Rating rating = new Rating("saman@ut.ac.ir", -1, 5);

        assertThrows(CommandException.class, () -> MovieManager.addRating(rating));
    }

    @Test
    @DisplayName("Should throw an exception if user who is rating movie does not exist")
    public void shouldThrowAnExceptionIfUserWhoIsRatingMovieDoesNotExist() throws CommandException {
        Rating rating = new Rating("john@ut.ac.ir", 1, 5);

        assertThrows(CommandException.class, () -> MovieManager.addRating(rating));
    }

    @Test
    @DisplayName("Should return null for average rating when a movie is not rated")
    public void shouldReturnNullForAverageRatingWhenAMovieIsNotRated() throws CommandException {
        Integer movieId = 1;

        assertNull(MovieManager.getMovie(movieId).getAverageRatingRate());
    }

    @Test
    @DisplayName("Should return correct average rating when a movie is rated")
    public void shouldReturnCorrectAverageRatingWhenAMovieIsRated() throws CommandException {
        Integer movieId = 1;
        Double score = 8.0;

        Rating rating = new Rating("young@ut.ir", movieId, score.intValue());
        MovieManager.addRating(rating);

        assertEquals(MovieManager.getMovie(movieId).getAverageRatingRate(), score);
    }

    @Test
    @DisplayName("Should return correct average rating when a movie is rated by multiple users")
    public void shouldReturnCorrectAverageRatingWhenAMovieIsRatedByMultipleUsers() throws CommandException {
        Integer movieId = 1;

        Double youngUserRatingScore = 3.0;
        Rating youngUserRating = new Rating("young@ut.ir", movieId, youngUserRatingScore.intValue());
        MovieManager.addRating(youngUserRating);

        Double oldUserRatingScore = 9.0;
        Rating oldUserRating = new Rating("old@ut.ir", movieId, oldUserRatingScore.intValue());
        MovieManager.addRating(oldUserRating);

        Double averageScore = (youngUserRatingScore + oldUserRatingScore) / 2;

        assertEquals(MovieManager.getMovie(movieId).getAverageRatingRate(), averageScore);
    }

    @Test
    @DisplayName("Should return correct average rating when a movie is rated again by user")
    public void shouldReturnCorrectAverageRatingWhenAMovieIsRatedAgainByAUser() throws CommandException {
        Integer movieId = 1;
        Double firstScore = 8.0;
        Double secondScore = 6.0;

        Rating firstRating = new Rating("young@ut.ir", movieId, firstScore.intValue());
        MovieManager.addRating(firstRating);

        Rating secondRating = new Rating("young@ut.ir", movieId, secondScore.intValue());
        MovieManager.addRating(secondRating);

        assertEquals(MovieManager.getMovie(movieId).getAverageRatingRate(), secondScore);
    }

    @Test
    @DisplayName("Should return correct number of movies between provided years")
    public void shouldReturnCorrectNumberOfMoviesBetweenProvidedYears() {
        ArrayList<Movie> movieList = MovieManager.getMoviesByReleaseYear(2000, 2013);

        assertEquals(movieList.size(), 2);
    }

    @Test
    @DisplayName("Should return no movies between provided years")
    public void shouldReturnNoMoviesBetweenProvidedYears() {
        ArrayList<Movie> movieList = MovieManager.getMoviesByReleaseYear(1800, 1900);

        assertEquals(movieList.size(), 0);
    }

    @AfterEach
    public void tearDown() {
        MovieManager.movies.clear();
        UserManager.users.clear();
    }
}
