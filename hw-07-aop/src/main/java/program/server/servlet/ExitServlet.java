package program.server.servlet;


import program.server.BanServer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ExitServlet extends Servlet {
    @Override
    protected void get(String login, Map<String, String[]> command, HttpServletResponse resp) throws IOException {
        sendLineHtml(resp, "OK!");
        resp.setContentType("text/html");
        BanServer.stop();
    }
}
