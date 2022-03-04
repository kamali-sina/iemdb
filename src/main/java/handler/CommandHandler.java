package handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.CommandException;
import exception.ErrorType;
import manager.ActorManager;
import manager.MovieManager;
import manager.UserManager;

import java.util.ArrayList;

import main.*;
import input.*;

public class CommandHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ArrayList<String> handleCommand(String CommandName, String CommandData) {
        ArrayList<String> results = new ArrayList<>();

        try {
            switch (CommandName) {
                case "addActor" -> {
                    Actor actor = objectMapper.readValue(CommandData, Actor.class);
                    results.add(ActorManager.addActor(actor));
                }
                case "addMovie" -> {
                    Movie movie = objectMapper.readValue(CommandData, Movie.class);
                    results.add(MovieManager.addMovie(movie));
                }
                case "addUser" -> {
                    User user = objectMapper.readValue(CommandData, User.class);
                    results.add(UserManager.addUser(user));
                }
                case "addComment" -> {
                    Comment comment = objectMapper.readValue(CommandData, Comment.class);
                    results.add(MovieManager.addComment(comment));
                }
                case "rateMovie" -> {
                    Rating rating = objectMapper.readValue(CommandData, Rating.class);
                    results.add(MovieManager.addRating(rating));
                }
                case "voteComment" -> {
                    Vote vote = objectMapper.readValue(CommandData, Vote.class);
                    results.add(UserManager.addVote(vote));
                }
                case "addToWatchList" -> {
                    WatchListItem watchListItem = objectMapper.readValue(CommandData, WatchListItem.class);
                    results.add(UserManager.addToWatchList(watchListItem));
                }
                case "removeFromWatchList" -> {
                    WatchListItem watchListItemToRemove = objectMapper.readValue(CommandData, WatchListItem.class);
                    results.add(UserManager.removeFromWatchList(watchListItemToRemove));
                }
                case "getMoviesList" -> results.add(MovieManager.getMoviesList());
                case "getMovieById" -> {
                    GetMovieByIdInput getMovieByIdInput = objectMapper.readValue(CommandData, GetMovieByIdInput.class);
                    results.add(MovieManager.getMovieById(getMovieByIdInput));
                }
                case "getMoviesByGenre" -> {
                    GetMoviesByGenreInput getMoviesByGenreInput =
                            objectMapper.readValue(CommandData, GetMoviesByGenreInput.class);
                    ArrayList<Movie> moviesByGenre = MovieManager.getMoviesByGenre(getMoviesByGenreInput);
                    results.add(MovieManager.serializeMoviesListByGenre(moviesByGenre));
                }
                case "getWatchList" -> {
                    GetWatchListInput getWatchListInput = objectMapper.readValue(CommandData, GetWatchListInput.class);
                    results.add(UserManager.getWatchList(getWatchListInput));
                }
                default -> throw new CommandException(ErrorType.InvalidCommand);
            }
        } catch (JsonProcessingException jsonProcessingException) {
            results.add("false");
            results.add(new CommandException(ErrorType.InvalidCommand).getMessage());
            return results;
        } catch (Exception exception) {
            results.add("false");
            results.add(exception.getMessage());
            return results;
        }
        results.add(0, "true");
        return results;
    }
}
