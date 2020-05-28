package servlets;

import dao.PostDAO;
import dao.impl.PostDAOImpl;
import forms.EditPostForm;
import forms.NewPostForm;
import models.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
            LogManager.getLogManager().readConfiguration(PostServlet.class.getClassLoader().getResourceAsStream(Constants.LOGGING_PROPERTIES));

            logger = Logger.getLogger(PostServlet.class.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            NewPostForm form = Constants.OBJECT_MAPPER.readValue(json, NewPostForm.class);

            resp.getWriter().write(Boolean.toString(postDAO.add(form)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession(false);
        int userID = (int) httpSession.getAttribute("userID");

        try {
            String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            EditPostForm form = Constants.OBJECT_MAPPER.readValue(json, EditPostForm.class);
            Optional<Integer> postUserID = postDAO.getUserID(form.getId());

            if (postUserID.isPresent()) {
                if (postUserID.get() == userID) {
                    resp.getWriter().write(Boolean.toString(postDAO.edit(form)));

                    return;
                }
            }

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession(false);
        int userID = (int) httpSession.getAttribute("userID");
        int id = Integer.parseInt(req.getParameter("id"));

        try {
            Optional<Integer> postUserID = postDAO.getUserID(id);

            if (postUserID.isPresent()) {
                if (postUserID.get() == userID) {
                    resp.getWriter().write(Boolean.toString(postDAO.remove(id)));

                    return;
                }
            }

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}