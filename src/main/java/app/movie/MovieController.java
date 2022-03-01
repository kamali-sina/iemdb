package app.movie;

import io.javalin.http.Handler;

public class MovieController {
    public static Handler fetchAllMovies = ctx -> {
        ctx.result("Hello Movie Controller");
    };

    public static Handler fetchMovieById = ctx -> {
        ctx.result("Hello Movie Controller");
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
