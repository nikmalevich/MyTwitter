package servlets;

import dao.PostDAO;
import dao.PostDAOImpl;
import models.Constants;
import models.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@WebServlet("/post")
public class PostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAOImpl();
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("D:\\GitHub\\MyTwitter\\task8\\TutterApp\\src\\main\\resources\\log.config"));

            logger = Logger.getLogger(PostServlet.class.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long id = Long.parseLong(req.getParameter("id"));

        Optional<Post> post = postDAO.get(id);

        if (post.isPresent()) {
            try {
                resp.getWriter().write(Constants.objectMapper.writeValueAsString(postDAO.get(id).get()));
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);;

            Post post = Constants.objectMapper.readValue(json, Post.class);

            resp.getWriter().write(Boolean.toString(postDAO.add(post)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

            Post post = Constants.objectMapper.readValue(json, Post.class);

            resp.getWriter().write(Boolean.toString(postDAO.edit(post)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long id = Long.parseLong(req.getParameter("id"));

        try {
            resp.getWriter().write(Boolean.toString(postDAO.remove(id)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}