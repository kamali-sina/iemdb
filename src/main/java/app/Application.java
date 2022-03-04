package app;

import app.actor.ActorController;
import app.movie.MovieController;
import app.user.UserController;
import io.javalin.Javalin;

public class Application {
    static Javalin app;

    public static void initialize() {
        Application.app = Javalin.create().start(5000);

        app.routes(() -> {
            app.get("/", ctx -> ctx.result("Hello World"));
            app.get("movies", MovieController.fetchAllMovies);
            app.get("movies/{movie_id}", MovieController.fetchMovieById);
            app.get("actors/{actor_id}", ActorController.fetchActor);
            app.get("watchList/{user_id}", UserController.fetchUserWatchList);
            app.get("watchList/{user_id}/{movie_id}", UserController.handleAddMovieToWatchList);
            app.get("rateMovie/{user_id}/{movie_id}/{rate}", MovieController.handleRatingMovie);
            app.get("voteComment/{user_id}/{comment_id}/{vote}", UserController.handleVotingComment);
            app.get("movies/search/{start_year}/{end_year}", MovieController.fetchMoviesByReleaseYear);
            app.get("movies/search/{genre}", MovieController.fetchMoviesByGenre);
        });
    }
}
