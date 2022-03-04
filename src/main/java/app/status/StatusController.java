package app.status;

import app.actor.ActorController;
import io.javalin.core.validation.ValidationException;
import io.javalin.core.validation.Validator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import main.Actor;
import manager.ActorManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class StatusController {
    public static Handler forbiddenHandler = ctx -> {
        File input = new File("src/main/resources/templates/403.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        ctx.html(doc.html());
    };

    public static Handler notFoundHandler = ctx -> {
        File input = new File("src/main/resources/templates/404.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        ctx.html(doc.html());
    };

    public static Handler successHandler = ctx -> {
        File input = new File("src/main/resources/templates/200.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        ctx.html(doc.html());
    };

}
