import exception.CommandException;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MovieManagerTest {
    @BeforeEach
    public void setUp() throws CommandException {
        ArrayList<String> avengersMovieGenres = new ArrayList<>();
        avengersMovieGenres.add("Action");
        avengersMovieGenres.add("Adventure");
        avengersMovieGenres.add("SuperHero");

        ArrayList<String> lordOfTheRingsMovieGenres = new ArrayList<>();
        lordOfTheRingsMovieGenres.add("Action");
        lordOfTheRingsMovieGenres.add("Adventure");
        lordOfTheRingsMovieGenres.add("Fantasy");

        Movie avengers = new Movie();
        avengers.setId(0);
        avengers.setName("Avengers");
        avengers.setGenres(avengersMovieGenres);
        avengers.setCast(new ArrayList<>());
        MovieManager.addMovie(avengers);

        Movie lordOfTheRings = new Movie();
        lordOfTheRings.setId(1);
        lordOfTheRings.setName("Lord of The Rings");
        lordOfTheRings.setGenres(lordOfTheRingsMovieGenres);
        lordOfTheRings.setCast(new ArrayList<>());
        MovieManager.addMovie(lordOfTheRings);
    }

    @Test
    @DisplayName("Should return movies in adventure genre when two movies exist")
    public void shouldReturnMoviesInAdventureGenreWhenTwoMoviesExist() throws IOException {
        GetMoviesByGenreInput getMoviesByGenreInput = new GetMoviesByGenreInput();
        getMoviesByGenreInput.setGenre("Adventure");

        String movies = MovieManager.getMoviesByGenre(getMoviesByGenreInput);

        String expectedMoviesInAdventureGenre = "{\"MoviesListByGenre\":[{\"movieId\":0,\"name\":\"Avengers\"," +
                "\"director\":null,\"genres\":[\"Action\",\"Adventure\",\"SuperHero\"],\"rating\":null}," +
                "{\"movieId\":1,\"name\":\"Lord of The Rings\",\"director\":null,\"genres\":[\"Action\"," +
                "\"Adventure\",\"Fantasy\"],\"rating\":null}]}";

        assertEquals(expectedMoviesInAdventureGenre, movies);
    }

    @AfterEach
    public void tearDown() {
        MovieManager.movies.clear();
    }
}
