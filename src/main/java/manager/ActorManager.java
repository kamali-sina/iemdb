package manager;

import exception.CommandException;
import exception.ErrorType;
import main.Actor;
import repository.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActorManager {
    public static ArrayList<Actor> getActors() throws CommandException {
        ArrayList<Actor> actors = new ArrayList<>();
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Actors");

            while (result.next()) {
                Actor actor = new Actor(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("birthDate"),
                        result.getString("nationality"),
                        result.getString("image")
                );

                actors.add(actor);
            }
            result.close();
            stmt.close();
            con.close();
            return actors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Actor getActor(Integer actorId) throws CommandException {
        Actor actor = null;
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Actors where id = \"" + actorId.toString() + "\"");
            while (result.next()) {
                 actor = new Actor(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("birthDate"),
                        result.getString("nationality"),
                        result.getString("image")
                );
            }
            result.close();
            stmt.close();
            con.close();
            if (actor == null) {
                throw new CommandException(ErrorType.ActorNotFound);
            }
            return actor;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
