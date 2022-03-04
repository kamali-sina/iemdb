import exception.CommandException;
import exception.ErrorType;
import input.GetWatchListInput;
import main.*;
import manager.MovieManager;
import manager.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    @BeforeEach
    public void setUp() throws CommandException {
        User young_user = new User("young@ut.ir", "young pass", "young", "Jane Doe"
                , "2010-01-01");
        UserManager.addUser(young_user);

        User old_user = new User("old@ut.ir", "old pass", "old", "John Doe"
                , "1990-01-01");
        UserManager.addUser(old_user);


        Comment comment1 = new Comment(young_user.getEmail(), 1, "I no like movie");

        Comment comment2 = new Comment(old_user.getEmail(), 2, "good movie");

        Comment comment3 = new Comment(young_user.getEmail(), 3, "very very bad movie");

        Movie avengers = new Movie(1, "Avengers",
                "", "2010-01-01",
                "", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 8.2, 150, 5);
        avengers.setComments(new HashMap<>());
        avengers.addComment(comment1);
        avengers.addComment(comment2);
        MovieManager.addMovie(avengers);

        Movie lordOfTheRings = new Movie(2, "Lord of The Rings",
                "", "2010-01-01",
                "", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 8.2, 150, 18);
        lordOfTheRings.setComments(new HashMap<>());
        lordOfTheRings.addComment(comment3);
        MovieManager.addMovie(lordOfTheRings);
    }

    @Test
    @DisplayName("should add to watch list when given a valid watch list input")
    public void shouldAddToWatchListWhenGivenAValidWatchListInput() throws CommandException {
        WatchListItem watchListItem = new WatchListItem(1, "young@ut.ir");

        String response = UserManager.addToWatchList(watchListItem);

        String expectedResponse = "\"movie added to watchlist successfully\"";

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("should throw age limit error when user age does not meet age limit")
    public void ShouldThrowAgeLimitErrorWhenUserAgeDoesNotMeetAgeLimit() throws CommandException {
        WatchListItem watchListItem = new WatchListItem(2, "young@ut.ir");

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addToWatchList(watchListItem));

        String expectedResponse = new CommandException(ErrorType.AgeLimitError).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw user not found when watch list's user email is not available")
    public void ShouldThrowUserNotFoundWhenWatchListsUserEmailIsNotValid() throws CommandException {
        WatchListItem watchListItem = new WatchListItem(2, "notAva@ut.ir");

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addToWatchList(watchListItem));

        String expectedResponse = new CommandException(ErrorType.UserNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw movie not found when watch list's movie is not available")
    public void shouldThrowMovieNotFoundWhenWatchListsMovieIsNotAvailable() throws CommandException {
        WatchListItem watchListItem = new WatchListItem(3, "young@ut.ir");

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addToWatchList(watchListItem));

        String expectedResponse = new CommandException(ErrorType.MovieNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should add vote to comment when given a valid input")
    public void shouldAddVoteToCommentWhenGivenAValidVoteInput() throws CommandException {
        Vote userVote = new Vote("young@ut.ir", 1, 1);

        String response = UserManager.addVote(userVote);

        Comment comment = MovieManager.movies.get(1).findComment(userVote.getCommentId());

        String expectedResponse = "\"comment voted successfully.\"";

        assertEquals(expectedResponse, response);
        assertEquals(1, comment.getNumberOfLikes());
        assertEquals(0, comment.getNumberOfDislikes());
    }

    @Test
    @DisplayName("should change user's vote when they submit valid vote on comment again")
    public void shouldChangeUsersVoteWhenTheySubmitValidVoteOnCommentAgain() throws CommandException {
        Vote userVote = new Vote("young@ut.ir", 1, 1);

        String response = UserManager.addVote(userVote);
        String expectedResponse = "\"comment voted successfully.\"";
        assertEquals(expectedResponse, response);

        Vote userVote2 = new Vote("young@ut.ir", 1, -1);

        response = UserManager.addVote(userVote2);

        Comment comment = MovieManager.movies.get(1).findComment(userVote.getCommentId());

        expectedResponse = "\"comment vote updated successfully.\"";

        assertEquals(expectedResponse, response);
        assertEquals(0, comment.getNumberOfLikes());
        assertEquals(1, comment.getNumberOfDislikes());
    }

    @Test
    @DisplayName("should throw invalid vote value when vote value is outside of defined range")
    public void shouldThrowInvalidVoteValueWhenVoteValueIsOutsideOfDefinedRange() throws CommandException {
        Vote userVote = new Vote("young@ut.ir", 1, 10);

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addVote(userVote));

        String expectedResponse = new CommandException(ErrorType.InvalidVoteValue).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw comment not found when comment id does not exist")
    public void shouldThrowCommentNotFoundWhenCommentIdDoesNotExist() throws CommandException {
        Vote userVote = new Vote("young@ut.ir", 22, 1);

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addVote(userVote));

        String expectedResponse = new CommandException(ErrorType.CommentNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw comment not found when comment id does not exist")
    public void shouldThrowUserNotFoundWhenUserEmailIsNotValid() throws CommandException {
        Vote userVote = new Vote("notAvailable@ut.ir", 1, 1);

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addVote(userVote));

        String expectedResponse = new CommandException(ErrorType.UserNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should return complete watch list when watch list is not empty")
    public void shouldReturnCompleteWatchListWhenWatchListIsNotEmpty() throws CommandException, IOException {
        String userEmail = "old@ut.ir";
        WatchListItem watchListItem = new WatchListItem(1, userEmail);

        String response = UserManager.addToWatchList(watchListItem);
        watchListItem.setMovieId(2);

        response = UserManager.addToWatchList(watchListItem);

        Collection<Movie> watchList = UserManager.getUser(userEmail).getWatchList().values();

        assertEquals(2, watchList.size());
        assertTrue(watchList.contains(MovieManager.getMovie(1)));
        assertTrue(watchList.contains(MovieManager.getMovie(2)));
    }

    @Test
    @DisplayName("should return empty array when watch list is empty")
    public void shouldReturnEmptyArrayWhenUserHasEmptyWatchlist() throws CommandException, IOException {
        String userEmail = "old@ut.ir";

        Collection<Movie> watchList = UserManager.getUser(userEmail).getWatchList().values();

        assertEquals(0, watchList.size());
    }

    @AfterEach
    public void tearDown() {
        UserManager.users.clear();
        MovieManager.movies.clear();
        Comment.setCount(1);
    }
}
