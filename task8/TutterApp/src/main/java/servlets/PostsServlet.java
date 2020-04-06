package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.PostDAO;
import dao.PostDAOImpl;
import forms.FilterForm;
import models.Constants;
import models.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/posts")
public class PostsServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));

        List<Post> page = postDAO.getPage(objectMapper.readValue(json, FilterForm.class));

        resp.getWriter().write(objectMapper.writeValueAsString(page));
    }
}
