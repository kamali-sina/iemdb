import exception.CommandException;
import input.GetMoviesByGenreInput;
import main.Movie;
import main.Rating;
import manager.MovieManager;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieManagerTest {
    private static void createMovie(Integer movieId, String movieName, ArrayList<String> genres) throws CommandException {
        Movie movie = new Movie(movieId, movieName,
                "", "2010-01-01",
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

        createMovie(0, "Avengers", avengersMovieGenres);
        createMovie(1, "Lord of The Rings", lordOfTheRingsMovieGenres);
    }

    @ParameterizedTest(name = "find movie(s) by {0} genre")
    @MethodSource("provideMovies")
    @DisplayName("Should return movies by genre")
    public void shouldReturnMoviesByGenre(String genre, ArrayList<Movie> movies) throws IOException, CommandException {
        GetMoviesByGenreInput getMoviesByGenreInput = new GetMoviesByGenreInput(genre);

        assertEquals(MovieManager.serializeMoviesListByGenre(movies), MovieManager.getMoviesByGenre(getMoviesByGenreInput));
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

        createMovie(0, "Avengers", avengersMovieGenres);
        createMovie(1, "Lord of The Rings", lordOfTheRingsMovieGenres);

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

        String movies = MovieManager.getMoviesByGenre(getMoviesByGenreInput);

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

    @AfterEach
    public void tearDown() {
        MovieManager.movies.clear();
    }
}
