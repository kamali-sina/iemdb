import exception.CommandException;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MovieManagerTest {
    private static void createMovie(Integer movieId, String movieName, ArrayList<String> genres) throws CommandException {
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setName(movieName);
        movie.setGenres(genres);
        movie.setCast(new ArrayList<>());
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
    public void shouldReturnMoviesByGenre(String genre, ArrayList<Movie> movies) throws IOException {
        GetMoviesByGenreInput getMoviesByGenreInput = new GetMoviesByGenreInput();
        getMoviesByGenreInput.setGenre(genre);

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
    public void shouldReturnMoviesInDramaGenreWhenNoMoviesExistInThatGenre() throws IOException {
        GetMoviesByGenreInput getMoviesByGenreInput = new GetMoviesByGenreInput();
        getMoviesByGenreInput.setGenre("drama");

        String movies = MovieManager.getMoviesByGenre(getMoviesByGenreInput);

        String expectedMoviesInAdventureGenre = "{\"MoviesListByGenre\":[]}";

        assertEquals(expectedMoviesInAdventureGenre, movies);
    }

    @AfterEach
    public void tearDown() {
        MovieManager.movies.clear();
    }
}
