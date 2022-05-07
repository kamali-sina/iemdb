package input;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.Actor;
import main.Comment;
import main.Movie;
import main.User;
import manager.ActorManager;
import manager.MovieManager;
import manager.UserManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public class DataReader {
    static final String MOVIESENDPOINT = "/api/v2/movies";
    static final String ACTORSENDPOINT = "/api/v2/actors";
    static final String USERSENDPOINT = "/api/users";
    static final String COMMENTSENDPOINT = "/api/comments";

    static final String url = "http://138.197.181.131:5000";

    public static List<User> readUsers() {
        try {
            Document doc = Jsoup.connect(url + USERSENDPOINT).ignoreContentType(true).get();
            List<User> users = null;
            ObjectMapper objectMapper = new ObjectMapper();
            users = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
            return users;
        } catch (Exception exception) {
            return null;
        }
    }

    public static List<Actor> readActors() {
        try {
            Document doc = Jsoup.connect(url + ACTORSENDPOINT).ignoreContentType(true).get();
            List<Actor> actors = null;
            ObjectMapper objectMapper = new ObjectMapper();
            actors = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Actor.class));
            return actors;
        } catch (Exception exception) {
            return null;
        }
    }

    public static List<Movie> readMovies() {
        try {
            Document doc = Jsoup.connect(url + MOVIESENDPOINT).ignoreContentType(true).get();
            List<Movie> movies = null;
            ObjectMapper objectMapper = new ObjectMapper();
            movies = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Movie.class));
            return movies;
        } catch (Exception exception) {
            return null;
        }
    }

    public static List<Comment> readComments() {
        try {
            Document doc = Jsoup.connect(url + COMMENTSENDPOINT).ignoreContentType(true).get();
            List<Comment> comments = null;
            ObjectMapper objectMapper = new ObjectMapper();
            comments = objectMapper.readValue(doc.wholeText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Comment.class));
            return comments;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
