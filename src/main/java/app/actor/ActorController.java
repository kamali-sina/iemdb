package app.actor;

import io.javalin.http.Handler;

public class ActorController {
    public static Handler fetchActor = ctx -> {
        ctx.result("hello actor: " + ctx.pathParam("actor_id"));
    };
}
