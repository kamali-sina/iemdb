import exception.CommandException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    private static void createMovie(Integer movieId, String movieName) throws CommandException {
        Movie movie = new Movie(movieId, movieName,
                "", "2010-01-01",
                "", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 8.2, 150, 10);
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

        Rating rating = new Rating();
        rating.setUserEmail("saman@ut.ac.ir");
        rating.setMovieId(1);
        rating.setScore(10);

        movie.addRating(rating);

        assertEquals(ratingCountBeforeUserVote + 1, movie.getRatingCount());
    }

    @Test
    @DisplayName("Should not increase rating count if user has rated the movie before")
    public void shouldNotIncreaseRatingCountIfUserHasRatedTheMovieBefore() throws CommandException {
        Movie movie = MovieManager.movies.get(1);

        Rating rating = new Rating();
        rating.setUserEmail("saman@ut.ac.ir");
        rating.setMovieId(1);
        rating.setScore(10);

        movie.addRating(rating);
        Integer ratingCountBeforeUserVote = movie.getRatingCount();
        movie.addRating(rating);

        assertEquals(ratingCountBeforeUserVote, movie.getRatingCount());
    }

    @Test
    @DisplayName("Should throw an exception if rating score is not between acceptable min and max score")
    public void ShouldThrowAExceptionIfRatingScoreIsNotBetweenAcceptableMinAndMaxScore() {
        Movie movie = MovieManager.movies.get(1);

        Rating rating = new Rating();
        rating.setUserEmail("saman@ut.ac.ir");
        rating.setMovieId(1);
        rating.setScore(-1);

        assertThrows(CommandException.class, () -> {movie.addRating(rating);});
    }

    @Test
    @DisplayName("Should calculate average rating correctly")
    public void shouldCalculateAverageRatingCorrectly() throws CommandException {
        Movie movie = MovieManager.movies.get(1);
        Integer ratingCountBeforeUserVote = movie.getRatingCount();

        Rating samanRating = new Rating();
        samanRating.setUserEmail("saman@ut.ac.ir");
        samanRating.setMovieId(1);
        samanRating.setScore(10);

        Rating saraRating = new Rating();
        saraRating.setUserEmail("sara@ut.ac.ir");
        saraRating.setMovieId(1);
        saraRating.setScore(7);

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
