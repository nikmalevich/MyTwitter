package servlets;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import forms.LoginForm;
import models.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            LoginForm loginForm = Constants.OBJECT_MAPPER.readValue(json, LoginForm.class);
            String name = new String(Base64.getDecoder().decode(loginForm.getName()));

            Optional<Integer> userID = userDAO.login(name, loginForm.getPassword());

            if (userID.isPresent()) {
                HttpSession httpSession = req.getSession(true);

                httpSession.setAttribute("userID", userID.get());

                resp.getWriter().write(Constants.OBJECT_MAPPER.writeValueAsString(userID.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession(false);

        httpSession.invalidate();
    }
}
