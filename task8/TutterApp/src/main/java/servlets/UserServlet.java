package servlets;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import models.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAOImpl();
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(PostsServlet.class.getClassLoader().getResourceAsStream(Constants.LOGGING_PROPERTIES));

            logger = Logger.getLogger(PostsServlet.class.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");

        try {
            Optional<Integer> id = userDAO.getID(name);

            if (id.isPresent()) {
                resp.getWriter().write(Constants.OBJECT_MAPPER.writeValueAsString(id.get()));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
