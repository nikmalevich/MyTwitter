package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.PostDAO;
import dao.PostDAOImpl;
import models.Constants;
import models.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@WebServlet("/post")
public class PostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));

        Optional<Post> post = postDAO.get(id);

        if (post.isPresent()) {
            resp.getWriter().write(objectMapper.writeValueAsString(postDAO.get(id).get()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));

            Post post = objectMapper.readValue(json, Post.class);

            resp.getWriter().write(Boolean.toString(postDAO.add(post)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));

        Post post = objectMapper.readValue(json, Post.class);

        resp.getWriter().write(Boolean.toString(postDAO.edit(post)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));

        resp.getWriter().write(Boolean.toString(postDAO.remove(id)));
    }
}