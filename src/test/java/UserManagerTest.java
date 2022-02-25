import exception.CommandException;
import exception.ErrorMessages;
import exception.ErrorType;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    @BeforeEach
    public void setUp() throws CommandException {
        User young_user = new User();
        young_user.setEmail("young@ut.ir");
        young_user.setNickname("young");
        young_user.setPassword("young pass");
        young_user.setBirthDate("2010-01-01");
        UserManager.addUser(young_user);

        User old_user = new User();
        old_user.setEmail("old@ut.ir");
        old_user.setNickname("old");
        old_user.setPassword("old pass");
        old_user.setBirthDate("1980-01-01");
        UserManager.addUser(old_user);


        Comment comment1 = new Comment(young_user.getEmail(),1, "I no like movie");

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
    public void shouldAddToWatchListWhenGivenAValidWatchListInput() throws IOException, CommandException {
        WatchList watchListItem = new WatchList();
        watchListItem.setMovieId(1);
        watchListItem.setUserEmail("young@ut.ir");

        String response = UserManager.addToWatchList(watchListItem);

        String expectedResponse = "\"movie added to watchlist successfully\"";

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("should throw age limit error when user age does not meet age limit")
    public void ShouldThrowAgeLimitErrorWhenUserAgeDoesNotMeetAgeLimit() throws IOException, CommandException {
        WatchList watchListItem = new WatchList();
        watchListItem.setMovieId(2);
        watchListItem.setUserEmail("young@ut.ir");

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addToWatchList(watchListItem));

        String expectedResponse = new CommandException(ErrorType.AgeLimitError).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw user not found when watch list's user email is not available")
    public void ShouldThrowUserNotFoundWhenWatchListsUserEmailIsNotValid() throws IOException, CommandException {
        WatchList watchListItem = new WatchList();
        watchListItem.setMovieId(2);
        watchListItem.setUserEmail("notAva@ut.ir");

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addToWatchList(watchListItem));

        String expectedResponse = new CommandException(ErrorType.UserNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw movie not found when watch list's movie is not available")
    public void shouldThrowMovieNotFoundWhenWatchListsMovieIsNotAvailable() throws IOException, CommandException {
        WatchList watchListItem = new WatchList();
        watchListItem.setMovieId(3);
        watchListItem.setUserEmail("young@ut.ir");

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addToWatchList(watchListItem));

        String expectedResponse = new CommandException(ErrorType.MovieNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should add vote to comment when given a valid input")
    public void shouldAddVoteToCommentWhenGivenAValidVoteInput() throws IOException, CommandException {
        Vote userVote = new Vote();
        userVote.setVote(1);
        userVote.setUserEmail("young@ut.ir");
        userVote.setCommentId(1);

        String response = UserManager.addVote(userVote);

        Comment comment = MovieManager.movies.get(1).findComment(userVote.getCommentId());

        String expectedResponse = "\"comment voted successfully.\"";

        assertEquals(expectedResponse, response);
        assertEquals(1, comment.getNumberOfLikes());
        assertEquals(0, comment.getNumberOfDislikes());
    }

    @Test
    @DisplayName("should change user's vote when they submit valid vote on comment again")
    public void shouldChangeUsersVoteWhenTheySubmitValidVoteOnCommentAgain() throws IOException, CommandException {
        Vote userVote = new Vote();
        userVote.setVote(1);
        userVote.setUserEmail("young@ut.ir");
        userVote.setCommentId(1);

        String response = UserManager.addVote(userVote);
        String expectedResponse = "\"comment voted successfully.\"";
        assertEquals(expectedResponse, response);

        Vote userVote2 = new Vote();
        userVote2.setVote(-1);
        userVote2.setUserEmail("young@ut.ir");
        userVote2.setCommentId(1);

        response = UserManager.addVote(userVote2);

        Comment comment = MovieManager.movies.get(1).findComment(userVote.getCommentId());

        expectedResponse = "\"comment vote updated successfully.\"";

        assertEquals(expectedResponse, response);
        assertEquals(0, comment.getNumberOfLikes());
        assertEquals(1, comment.getNumberOfDislikes());
    }

    @Test
    @DisplayName("should throw invalid vote value when vote value is outside of defined range")
    public void shouldThrowInvalidVoteValueWhenVoteValueIsOutsideOfDefinedRange() throws IOException, CommandException {
        Vote userVote = new Vote();
        userVote.setVote(10);
        userVote.setUserEmail("young@ut.ir");
        userVote.setCommentId(1);

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addVote(userVote));

        String expectedResponse = new CommandException(ErrorType.InvalidVoteValue).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw comment not found when comment id does not exist")
    public void shouldThrowCommentNotFoundWhenCommentIdDoesNotExist() throws IOException, CommandException {
        Vote userVote = new Vote();
        userVote.setVote(1);
        userVote.setUserEmail("young@ut.ir");
        userVote.setCommentId(22);

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addVote(userVote));

        String expectedResponse = new CommandException(ErrorType.CommentNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @Test
    @DisplayName("should throw comment not found when comment id does not exist")
    public void shouldThrowUserNotFoundWhenUserEmailIsNotValid() throws IOException, CommandException {
        Vote userVote = new Vote();
        userVote.setVote(1);
        userVote.setUserEmail("notAvailable@ut.ir");
        userVote.setCommentId(1);

        Exception exception = assertThrows(CommandException.class, () -> UserManager.addVote(userVote));

        String expectedResponse = new CommandException(ErrorType.UserNotFound).getMessage();

        assertEquals(expectedResponse, exception.getMessage());
    }

    @AfterEach
    public void tearDown() {
        UserManager.users.clear();
        MovieManager.movies.clear();
        Comment.setCount(1);
    }
}
