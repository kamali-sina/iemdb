package app.user;

import io.javalin.core.validation.ValidationException;
import io.javalin.core.validation.Validator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import main.*;
import manager.UserManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class UserController {
    public static String getWatchListHtmlString(User user) throws IOException {

        File input = new File("src/main/resources/templates/watchlist.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        String htmlString = doc.html();

        String movieTableLine = "<tr>\n" +
                "            <th>$name</th>\n" +
                "            <th>$releaseDate</th>\n" +
                "            <th>$director</th>\n" +
                "            <th>$genres</th>\n" +
                "            <th>$imdbRating</th>\n" +
                "            <th>$rating</th>\n" +
                "            <th>$duration</th>\n" +
                "            <td><a href=\"/movies/$ID\">Link</a></td>\n" +
                "            <td><a href=\"/removeMovieFromWatchList/" + user.getEmail() + "/$ID\">Link</a></td>\n" +
                "        </tr>\n";

        htmlString = htmlString.replace("$name", user.getName());
        htmlString = htmlString.replace("$nickname", user.getNickname());

        String movies = "";

        for (Movie movie : user.getWatchList().values()) {
            movies += movieTableLine;
            movies = movies.replace("$name", movie.getName());
            movies = movies.replace("$releaseDate", movie.getReleaseDate());
            movies = movies.replace("$director", movie.getDirector());
            movies = movies.replace("$genres", movie.getGenres().toString());
            movies = movies.replace("$imdbRating", movie.getImdbRate().toString());
            movies = movies.replace("$rating", String.valueOf(movie.getAverageRatingRate()));
            movies = movies.replace("$duration", movie.getDuration().toString());
            movies = movies.replace("$ID", movie.getId().toString());
        }

        htmlString = htmlString.replace("$movies", movies);

        return htmlString;
    }

    public static Handler fetchUserWatchList = ctx -> {
        try {
            Validator<String> userId = ctx.pathParamAsClass("user_id", String.class);
            User user = UserManager.getUser(userId.get());

            String htmlString = UserController.getWatchListHtmlString(user);

            ctx.html(htmlString);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new NotFoundResponse();
        }
    };

    public static Handler handleAddMovieToWatchList = ctx -> {
        try {
            Validator<String> userId = ctx.pathParamAsClass("user_id", String.class);
            Validator<Integer> movieId = ctx.pathParamAsClass("movie_id", Integer.class);
            WatchList watchListItem = new WatchList(movieId.get(), userId.get());

            UserManager.addToWatchList(watchListItem);

            // TODO: Add template rendering
            ctx.result("successfully added to watchlist!");
        } catch (ValidationException validationException){
            throw new BadRequestResponse();
        } catch (Exception exception) {
            throw new NotFoundResponse(); //TODO: should we give not found when movie is duplicate?
        }
    };

    public static Handler handleRemoveMovieFromWatchList = ctx -> {
        try {
            Validator<String> userId = ctx.pathParamAsClass("user_id", String.class);
            Validator<Integer> movieId = ctx.pathParamAsClass("movie_id", Integer.class);
            WatchList watchListItem = new WatchList(movieId.get(), userId.get());

            UserManager.removeFromWatchList(watchListItem);

            // TODO: Add template rendering
            ctx.result("successfully removed from watchlist!");
        } catch (ValidationException validationException){
            throw new BadRequestResponse();
        } catch (Exception exception) {
            throw new NotFoundResponse(); //TODO: should we give not found when movie is duplicate?
        }
    };

    public static Handler handleVotingComment = ctx -> {
        try {
            Validator<String> userId = ctx.pathParamAsClass("user_id", String.class);
            Validator<Integer> commentId = ctx.pathParamAsClass("comment_id", Integer.class);
            Validator<Integer> vote = ctx.pathParamAsClass("vote", Integer.class);

            Vote usersVote = new Vote(userId.get(), commentId.get(), vote.get());
            UserManager.addVote(usersVote);

            // TODO: add template rendering
            ctx.result("successfully voted comment!");
        } catch (ValidationException validationException){
            throw new BadRequestResponse();
        } catch (Exception exception) {
            throw new NotFoundResponse(); //TODO: should we give not found when movie is duplicate?
        }
    };
}
