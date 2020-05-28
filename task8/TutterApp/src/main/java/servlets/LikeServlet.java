package servlets;

import dao.PostDAO;
import dao.impl.PostDAOImpl;
import models.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@WebServlet("/like")
public class LikeServlet extends HttpServlet {
    private static PostDAO postDAO = new PostDAOImpl();
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
        int postID = Integer.parseInt(req.getParameter("postID"));
        int userID = Integer.parseInt(req.getParameter("userID"));

        try {
            resp.getWriter().write(Boolean.toString(postDAO.like(postID, userID)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
