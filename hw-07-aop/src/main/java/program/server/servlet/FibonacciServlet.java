package program.server.servlet;

import aspects.AspectProfile;
import program.Fibonacci;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class FibonacciServlet extends Servlet {

    @Override
    @AspectProfile
    protected void get(String login, Map<String, String[]> command, HttpServletResponse resp) throws IOException {
        sendLineHtml(resp, String.valueOf(Fibonacci.f(login, Integer.parseInt(command.get("input")[0]))));
    }
}
