package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class TimeFilter implements Filter {
    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        long finishTime = System.currentTimeMillis();
        long time = finishTime - startTime;

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = String.valueOf(request.getRequestURL());
        String method = request.getMethod();

        System.out.println("Time: " + time + "ms; URL: " + url + "; Method: " + method);
    }

    public void destroy() {

    }
}
