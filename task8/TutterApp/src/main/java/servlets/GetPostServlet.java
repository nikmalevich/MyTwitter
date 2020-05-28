package servlets;

import dao.PostDAO;
import dao.impl.PostDAOImpl;
import models.Constants;
import models.Post;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@WebServlet("/getPost")
public class GetPostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAOImpl();
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(PostServlet.class.getClassLoader().getResourceAsStream(Constants.LOGGING_PROPERTIES));

            logger = Logger.getLogger(PostServlet.class.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));

        Optional<Post> post = postDAO.get(id);

        if (post.isPresent()) {
            try {
                resp.getWriter().write(Constants.OBJECT_MAPPER.writeValueAsString(post.get()));
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}