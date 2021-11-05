package program.server.servlet;

import aspects.AspectProfile;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public abstract class Servlet extends HttpServlet {

    public void sendLineHtml(ServletResponse response, String s) throws IOException {
        response.getWriter().println(s);
    }

    protected abstract void get(String login, Map<String, String[]> command, HttpServletResponse resp) throws IOException;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        get(req.getParameter("login"), req.getParameterMap(), resp);
    }
}
