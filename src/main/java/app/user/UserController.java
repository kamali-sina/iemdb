package app.user;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import main.Actor;
import main.User;
import main.Vote;
import main.WatchList;
import manager.ActorManager;
import manager.UserManager;

public class UserController {
    public static Handler fetchUserWatchList = ctx -> {
        String user_email = ctx.pathParam("user_id");
        System.out.println(user_email);
        User user = null;
        try {
            user = UserManager.getUser(user_email);
        } catch (Exception exception) {
            throw new NotFoundResponse();
        }
        // TODO: add template rendering
        ctx.result("user watchlist: " + user.getSerializedWatchlist());
    };

    public static Handler handleAddMovieToWatchList = ctx -> {
        Integer movie_id = null;
        String user_email = ctx.pathParam("user_id");
        try {
            movie_id = Integer.valueOf(ctx.pathParam("movie_id"));
        } catch (Exception exception){
            throw new BadRequestResponse();
        }
        WatchList watchListItem = new WatchList(movie_id, user_email);
        try {
            UserManager.addToWatchList(watchListItem);
        } catch (Exception exception) {
            throw new NotFoundResponse(); //TODO: should we give not found when movie is duplicate?
        }
        // TODO: add template rendering
        ctx.result("successfully added to watchlist!");
    };

    public static Handler handleVotingComment = ctx -> {
        Integer comment_id = null;
        Integer vote = null;
        String user_email = ctx.pathParam("user_id");
        try {
            comment_id = Integer.valueOf(ctx.pathParam("comment_id"));
            vote = Integer.valueOf(ctx.pathParam("vote"));
        } catch (Exception exception){
            throw new BadRequestResponse();
        }
        Vote usersVote = new Vote(user_email, comment_id, vote);
        try {
            UserManager.addVote(usersVote);
        } catch (Exception exception) {
            throw new NotFoundResponse(); //TODO: should we give not found when movie is duplicate?
        }
        // TODO: add template rendering
        ctx.result("successfully voted comment!");
    };
}
