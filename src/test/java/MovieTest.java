import exception.CommandException;
import main.Movie;
import main.Rating;
import manager.MovieManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieTest {
    private static void createMovie(Integer movieId, String movieName) throws CommandException {
        Movie movie = new Movie(movieId, movieName,
                "", "2010-01-01",
                "", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 8.2f, 150, 10, "", "");
        MovieManager.addMovie(movie);
    }

    @BeforeEach
    public void setUp() throws CommandException {
        createMovie(0, "Avengers");
        createMovie(1, "Lord of The Rings");
    }

    @Test
    @DisplayName("Should increase rating count if user has not rated the movie before")
    public void shouldIncreaseRatingCountIfUserHasNotRatedTheMovieBefore() throws CommandException {
        Movie movie = MovieManager.movies.get(1);
        Integer ratingCountBeforeUserVote = movie.getRatingCount();

        Rating rating = new Rating("saman@ut.ac.ir", 1, 10);

        movie.addRating(rating);

        assertEquals(ratingCountBeforeUserVote + 1, movie.getRatingCount());
    }

    @Test
    @DisplayName("Should not increase rating count if user has rated the movie before")
    public void shouldNotIncreaseRatingCountIfUserHasRatedTheMovieBefore() throws CommandException {
        Movie movie = MovieManager.movies.get(1);

        Rating rating = new Rating("saman@ut.ac.ir", 1, 10);

        movie.addRating(rating);
        Integer ratingCountBeforeUserVote = movie.getRatingCount();
        movie.addRating(rating);

        assertEquals(ratingCountBeforeUserVote, movie.getRatingCount());
    }

    @Test
    @DisplayName("Should throw an exception if rating score is not between acceptable min and max score")
    public void ShouldThrowAExceptionIfRatingScoreIsNotBetweenAcceptableMinAndMaxScore() throws CommandException {
        Movie movie = MovieManager.movies.get(1);

        Rating rating = new Rating("saman@ut.ac.ir", 1, -1);

        assertThrows(CommandException.class, () -> movie.addRating(rating));
    }

    @Test
    @DisplayName("Should calculate average rating correctly")
    public void shouldCalculateAverageRatingCorrectly() throws CommandException {
        Movie movie = MovieManager.movies.get(1);
        Integer ratingCountBeforeUserVote = movie.getRatingCount();

        Rating samanRating = new Rating("saman@ut.ac.ir", 1, 10);

        Rating saraRating = new Rating("sara@ut.ac.ir", 1, 7);

        movie.addRating(samanRating);
        movie.addRating(saraRating);

        Double expectedAverageRating = (samanRating.getScore() + saraRating.getScore())
                / movie.getRatingCount().doubleValue();

        assertEquals(expectedAverageRating, movie.calculateAverageRating());
    }

    @AfterEach
    public void tearDown() {
        MovieManager.movies.clear();
    }
}
