package app.actor;

import io.javalin.core.validation.ValidationException;
import io.javalin.core.validation.Validator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import main.Actor;
import main.Movie;
import manager.ActorManager;
import manager.MovieManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActorController {
    public static Handler fetchActor = ctx -> {
        try {
            Validator<Integer> actorId = ctx.pathParamAsClass("actor_id", Integer.class);
            Actor actor = ActorManager.getActor(actorId.get());

            String htmlString = ActorController.getActorHtmlString(actor);

            ctx.html(htmlString);
        } catch (ValidationException validationException) {
            throw new BadRequestResponse();
        } catch (Exception exception) {
            throw new NotFoundResponse();
        }
    };

    public static String getActorHtmlString(Actor actor) throws IOException {
        File input = new File("src/main/resources/templates/actor.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        String htmlString = doc.html();

        String movieTableLine = "<tr>\n" +
                "<td>$name</td>\n" +
                "<td>$imdbRating</td>\n" +
                "<td>$rating</td>\n" +
                "<td><a href=\"/movies/$ID\">Link</a></td>\n" +
                "</tr>\n";

        htmlString = htmlString.replace("$name", actor.getName());
        htmlString = htmlString.replace("$birthDate", actor.getBirthDate());
        htmlString = htmlString.replace("$nationality", actor.getNationality());
        ArrayList<Movie> actorMovies = MovieManager.getActorMovies(actor.getId());
        htmlString = htmlString.replace("$tma", String.valueOf(actorMovies.size()));

        StringBuilder movies = new StringBuilder();

        for (Movie movie : actorMovies) {
            movies.append(movieTableLine);
            movies = new StringBuilder(movies.toString().replace("$name", movie.getName()));
            movies = new StringBuilder(movies.toString().replace("$imdbRating", String.valueOf(movie.getImdbRate())));
            movies = new StringBuilder(movies.toString().replace("$rating", String.valueOf(movie.getAverageRatingRate())));
            movies = new StringBuilder(movies.toString().replace("$ID", String.valueOf(movie.getId())));
        }

        htmlString = htmlString.replace("$movies", movies.toString());
        return htmlString;
    }
}
