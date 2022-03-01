package manager;

import exception.CommandException;
import exception.ErrorType;

import java.util.HashMap;

import main.Actor;

public class ActorManager {
    public static final HashMap<Integer, Actor> actors = new HashMap<>();

    public static Actor getActor(Integer actorId) throws CommandException {
        Actor actor = ActorManager.actors.get(actorId);
        if (actor == null) {
            throw new CommandException(ErrorType.ActorNotFound);
        }
        return actor;
    }

    public static boolean doesActorExist(Integer actorId) {
        return ActorManager.actors.containsKey(actorId);
    }

    public static String addActor(Actor actor) {
        String response = "\"actor added successfully\"";
        if (ActorManager.doesActorExist(actor.getId())) {
            response = "\"actor updated successfully\"";
        }
        ActorManager.actors.put(actor.getId(), actor);
        return response;
    }
}
