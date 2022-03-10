package com.iemdb;

import exception.CommandException;
import main.Movie;
import manager.ActorManager;
import manager.ErrorManager;
import manager.MovieManager;
import manager.UserManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ActorServlet", value = "/actors/*")
public class ActorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UserManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String actorIdString = pathParts[1];
        try {
            ActorManager.getActor(Integer.valueOf(actorIdString));
        } catch (CommandException commandException) {
            ErrorManager.error(request, response, commandException.getMessage());
        } catch (Exception exception) {
            ErrorManager.error(request, response, "Invalid value for actor_id");
        }
        request.getRequestDispatcher("/jsps/actor.jsp?actorId=" + actorIdString).forward(request, response);
    }
}
