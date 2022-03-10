package manager;

import main.Actor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ErrorManager {
    public static String error_message;

    public static String getError_message() {
        return error_message;
    }

    public static void setError_message(String error_message) {
        ErrorManager.error_message = error_message;
    }

    public static void error(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        ErrorManager.setError_message(message);
        request.getRequestDispatcher("/jsps/error.jsp").forward(request, response);
    }
}
