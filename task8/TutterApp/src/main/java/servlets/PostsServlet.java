package servlets;

import dao.PostDAO;
import dao.impl.PostDAOImpl;
import forms.FilterForm;
import models.Constants;
import models.Post;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@WebServlet("/posts")
public class PostsServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAOImpl();
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

            List<Post> page = postDAO.getPage(Constants.OBJECT_MAPPER.readValue(json, FilterForm.class));

            resp.getWriter().write(Constants.OBJECT_MAPPER.writeValueAsString(page));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
