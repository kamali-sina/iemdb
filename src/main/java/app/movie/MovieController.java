package app.movie;

import exception.CommandException;
import input.GetMoviesByGenreInput;
import io.javalin.core.validation.ValidationException;
import io.javalin.core.validation.Validator;
import io.javalin.http.Handler;
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
    public static Handler fetchAllMovies = ctx -> {
        try {
            ArrayList<Movie> moviesList = new ArrayList<>(MovieManager.movies.values());
            String htmlString = MovieController.getMoviesHtmlString(moviesList);

            ctx.html(htmlString);
        } catch (Exception exception) {
            ctx.redirect("/notFound");
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
            ctx.redirect("/forbidden");
        } catch (Exception exception) {
            ctx.redirect("/notFound");
        }
    };
    public static Handler handleRatingMovie = ctx -> {
        try {
            Validator<String> userEmail = ctx.pathParamAsClass("user_id", String.class);
            UserManager.getUser(userEmail.get());

            Validator<Integer> movieId = ctx.pathParamAsClass("movie_id", Integer.class);
            movieId.check(id -> 1 <= id, "Movie ID should be greater than 0");
            MovieManager.getMovie(movieId.get());

            Validator<Integer> rate = ctx.pathParamAsClass("rate", Integer.class);
            rate.check(score -> 0 <= score && score <= 10, "rating score should be between 0 and 10");

            Rating rating = new Rating(userEmail.get(), movieId.get(), rate.get());

            ctx.redirect("/success");
            MovieManager.addRating(rating);
        } catch (Exception exception) {
            ctx.redirect("/forbidden");
        }
    };
    public static Handler handleRatingMovieByPost = ctx -> {
        try {
            Validator<String> userEmail = ctx.formParamAsClass("user_id", String.class);
            UserManager.getUser(userEmail.get());
            Validator<Integer> movieId = ctx.pathParamAsClass("movie_id", Integer.class);
            movieId.check(id -> 1 <= id, "Movie ID should be greater than 0");
            MovieManager.getMovie(movieId.get());

            Validator<Integer> rate = ctx.formParamAsClass("quantity", Integer.class);
            rate.check(score -> 0 <= score && score <= 10, "rating score should be between 0 and 10");

            Rating rating = new Rating(userEmail.get(), movieId.get(), rate.get());

            ctx.redirect("/success");
            MovieManager.addRating(rating);
        } catch (Exception exception) {
            ctx.redirect("/forbidden");
        }
    };
    public static Handler fetchMoviesByReleaseYear = ctx -> {
        try {
            Validator<Integer> startYear = ctx.pathParamAsClass("start_year", Integer.class);
            Validator<Integer> endYear = ctx.pathParamAsClass("end_year", Integer.class);
            startYear.check(year -> 0 <= year, "Start year should be greater than 0");
            endYear.check(year -> 0 <= year, "End year should be greater than 0");
            endYear.check(year -> startYear.get() <= year, "End year should be greater than Start year");

            ArrayList<Movie> moviesList = MovieManager.getMoviesByReleaseYear(startYear.get(), endYear.get());
            String htmlString = MovieController.getMoviesHtmlString(moviesList);

            ctx.html(htmlString);
        } catch (Exception exception) {
            ctx.redirect("/forbidden");
        }
    };
    public static Handler fetchMoviesByGenre = ctx -> {
        try {
            Validator<String> genre = ctx.pathParamAsClass("genre", String.class);

            GetMoviesByGenreInput getMoviesByGenreInput = new GetMoviesByGenreInput(genre.get());
            ArrayList<Movie> moviesByGenre = MovieManager.getMoviesByGenre(getMoviesByGenreInput);

            String htmlString = MovieController.getMoviesHtmlString(moviesByGenre);
            ctx.html(htmlString);
        } catch (Exception exception) {
            ctx.redirect("/forbidden");
        }
    };

    public static String getMovieHtmlString(Movie movie) throws IOException, CommandException {

        File input = new File("src/main/resources/templates/movie.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        String htmlString = doc.html();

        String commentTableLine = "      <tr>\n" +
                "        <td>$name</td>\n" +
                "        <td>$comment</td>\n" +
                "        <td>$like</td>\n" +
                "        <td>$disLike</td>\n";
        String commentTableLineAddOn = "<td>\n" +
                "          <form action=\"/api/voteComment/$ID/1\" method=\"POST\">\n" +
                "            <label>ID:</label>\n" +
                "            <input type=\"text\" name=\"user_id\" value=\"\"/>" +
                "            <button type=\"submit\">like</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "          <form action=\"/api/voteComment/$ID/-1\" method=\"POST\">\n" +
                "            <label>ID:</label>\n" +
                "            <input type=\"text\" name=\"user_id\" value=\"\"/>" +
                "            <button type=\"submit\">dislike</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "      </tr>";

        htmlString = htmlString.replace("$name", movie.getName());
        htmlString = htmlString.replace("$summary", movie.getSummary());
        htmlString = htmlString.replace("$releaseDate", movie.getReleaseDate());
        htmlString = htmlString.replace("$director", movie.getDirector());
        htmlString = htmlString.replace("$writers", movie.getWriters().toString());
        htmlString = htmlString.replace("$genres", movie.getGenres().toString());
        htmlString = htmlString.replace("$ID", movie.getId().toString());

        String cast = "";
        String delimiter = "";
        for (Integer actorId : movie.getCast()) {
            cast += delimiter;
            cast += ActorManager.getActor(actorId).getName();
            delimiter = ", ";
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
                comments += commentTableLineAddOn;
                comments = comments.replace("$ID", comment.getId().toString());
            }
        }

        htmlString = htmlString.replace("$comments", comments);

        return htmlString;
    }

    public static String getMoviesHtmlString(ArrayList<Movie> moviesList) throws IOException, CommandException {
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

        for (Movie movie : moviesList) {
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

        return htmlString;
    }
}
