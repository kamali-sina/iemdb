package input;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.CommandException;
import main.Actor;
import main.Comment;
import main.Movie;
import main.User;
import manager.ActorManager;
import manager.MovieManager;
import manager.UserManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class DataReader {
    static final String MOVIESENDPOINT = "/api/movies";
    static final String ACTORSENDPOINT = "/api/actors";
    static final String USERSENDPOINT = "/api/users";
    static final String COMMENTSENDPOINT = "/api/comments";

    public static void readDataFromUrl(String url) {
        try {
            readUsers(url);
            readActors(url);
            readMovies(url);
            readComments(url);
            System.out.println("All data loaded successfully!");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void readUsers(String url) throws IOException, CommandException {
        Document doc = Jsoup.connect(url + USERSENDPOINT).ignoreContentType(true).get();
        List<User> users = null;
        ObjectMapper objectMapper = new ObjectMapper();
        users = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        System.out.println(UserManager.addUsers(users));
    }

    private static void readActors(String url) throws IOException, CommandException {
        Document doc = Jsoup.connect(url + ACTORSENDPOINT).ignoreContentType(true).get();
        List<Actor> actors = null;
        ObjectMapper objectMapper = new ObjectMapper();
        actors = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Actor.class));
        System.out.println(ActorManager.addActors(actors));
    }

    private static void readMovies(String url) throws IOException, CommandException {
        Document doc = Jsoup.connect(url + MOVIESENDPOINT).ignoreContentType(true).get();
        List<Movie> movies = null;
        ObjectMapper objectMapper = new ObjectMapper();
        movies = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Movie.class));
        System.out.println(MovieManager.addMovies(movies));
    }

    private static void readComments(String url) throws IOException, CommandException {
        Document doc = Jsoup.connect(url + COMMENTSENDPOINT).ignoreContentType(true).get();
        List<Comment> comments = null;
        ObjectMapper objectMapper = new ObjectMapper();
        comments = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Comment.class));
        System.out.println(MovieManager.addComments(comments));
    }
}
