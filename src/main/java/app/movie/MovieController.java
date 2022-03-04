package app.movie;

import exception.CommandException;
import input.GetMoviesByGenreInput;
import io.javalin.core.validation.ValidationException;
import io.javalin.core.validation.Validator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import main.Comment;
import main.Movie;
import main.Rating;
import manager.ActorManager;
import manager.MovieManager;
import manager.UserManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MovieController {
    public static String getMovieHtmlString(Movie movie) throws IOException, CommandException {

        File input = new File("src/main/resources/templates/movie.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        String htmlString = doc.html();

        String commentTableLine = "      <tr>\n" +
                "        <td>$name</td>\n" +
                "        <td>$comment</td>\n" +
                "        <td>$like</td>\n" +
                "        <td>$disLike</td>\n" +
                "      </tr>\n";

        htmlString = htmlString.replace("$name", movie.getName());
        htmlString = htmlString.replace("$summary", movie.getSummary());
        htmlString = htmlString.replace("$releaseDate", movie.getReleaseDate());
        htmlString = htmlString.replace("$director", movie.getDirector());
        htmlString = htmlString.replace("$writers", movie.getWriters().toString());
        htmlString = htmlString.replace("$genres", movie.getGenres().toString());

        String cast = "";

        for (Integer actorId : movie.getCast()) {
            cast += ActorManager.getActor(actorId).getName();
            cast += ", ";
        }

        htmlString = htmlString.replace("$cast", cast);
        htmlString = htmlString.replace("$imdbRate", movie.getImdbRate().toString());
        htmlString = htmlString.replace("$rating", String.valueOf(movie.getAverageRatingRate()));
        htmlString = htmlString.replace("$duration", movie.getDuration().toString());
        htmlString = htmlString.replace("$ageLimit", movie.getAgeLimit().toString());

        String comments = "";

        for (ArrayList<Comment> userComment : movie.getComments().values()) {
            for (Comment comment : userComment) {
                comments += commentTableLine;
                comments = comments.replace("$name", comment.getUserEmail());
                comments = comments.replace("$comment", comment.getText());
                comments = comments.replace("$like", comment.getNumberOfLikes().toString());
                comments = comments.replace("$disLike", comment.getNumberOfDislikes().toString());
            }
        }

        htmlString = htmlString.replace("$comments", comments);

        return htmlString;
    }

    public static Handler fetchAllMovies = ctx -> {
        try {
            File input = new File("src/main/resources/templates/movies.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            String htmlString = doc.html();

            String movieTableLine = "<tr>\n" +
                    "            <td>$name</td>\n" +
                    "            <td>$summary</td> \n" +
                    "            <td>$releaseDate</td>\n" +
                    "            <td>$director</td>\n" +
                    "            <td>$writers</td>\n" +
                    "            <td>$genres</td>\n" +
                    "            <td>$cast</td>\n" +
                    "            <td>$imdbRate</td>\n" +
                    "            <td>$rating</td>\n" +
                    "            <td>$duration</td>\n" +
                    "            <td>$ageLimit</td>\n" +
                    "            <td><a href=\"/movies/$ID\">Link</a></td>\n" +
                    "        </tr>";

            String movies = "";

            for (Movie movie : MovieManager.movies.values()) {
                movies += movieTableLine;
                movies = movies.replace("$name", movie.getName());
                movies = movies.replace("$summary", movie.getSummary());
                movies = movies.replace("$releaseDate", movie.getReleaseDate());
                movies = movies.replace("$director", movie.getDirector());
                movies = movies.replace("$writers", movie.getWriters().toString());
                movies = movies.replace("$genres", movie.getGenres().toString());

                String cast = "";

                for (Integer actorId : movie.getCast()) {
                    cast += ActorManager.getActor(actorId).getName();
                    cast += ", ";
                }

                movies = movies.replace("$cast", cast);
                movies = movies.replace("$imdbRate", movie.getImdbRate().toString());
                movies = movies.replace("$rating", String.valueOf(movie.getAverageRatingRate()));
                movies = movies.replace("$duration", movie.getDuration().toString());
                movies = movies.replace("$ageLimit", movie.getAgeLimit().toString());
                movies = movies.replace("$ageLimit", movie.getAgeLimit().toString());
                movies = movies.replace("$ID", movie.getId().toString());
            }

            htmlString = htmlString.replace("$movies", movies);

            ctx.html(htmlString);
        } catch (Exception exception) {
            throw new NotFoundResponse();
        }
    };

    public static Handler fetchMovieById = ctx -> {
        try {
            Validator<Integer> movieId = ctx.pathParamAsClass("movie_id", Integer.class);
            movieId.check(id -> 1 <= id, "Movie ID should be greater than 0");
            Movie movie = MovieManager.getMovie(movieId.get());

            String htmlString = MovieController.getMovieHtmlString(movie);

            ctx.html(htmlString);
        } catch (ValidationException validationException) {
            throw new BadRequestResponse();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new NotFoundResponse();
        }
    };

    public static Handler handleRatingMovie = ctx -> {
        ctx.result("Hello Movie Controller");
    };

    public static Handler fetchMoviesByReleaseYaer = ctx -> {
        ctx.result("Hello Movie Controller");
    };

    public static Handler fetchMoviesByGenre = ctx -> {
        ctx.result("Hello Movie Controller");
    };
}
