package app.user;

import io.javalin.http.Handler;

public class UserController {
    public static Handler fetchUserWatchList = ctx -> {
        ctx.result("Hello WatchList Controller");
    };

    public static Handler handleAddMovieToWatchList = ctx -> {
        ctx.result("Hello WatchList Controller");
    };

    public static Handler handleVotingComment = ctx -> {
        ctx.result("Hello Movie Controller");
    };
}
