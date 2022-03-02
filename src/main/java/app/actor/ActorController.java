package app.actor;

import io.javalin.http.*;
import main.Actor;
import manager.ActorManager;

public class ActorController {
    public static Handler fetchActor = ctx -> {
        Integer actor_id = null;
        try {
            actor_id = Integer.valueOf(ctx.pathParam("actor_id"));
        } catch (Exception exception){
            throw new BadRequestResponse();
        }
        Actor actor = null;
        try {
            actor = ActorManager.getActor(actor_id);
        } catch (Exception exception) {
            throw new NotFoundResponse();
        }
        // TODO: add template rendering
        ctx.result("hello actor: " + actor.getName());
    };
}
