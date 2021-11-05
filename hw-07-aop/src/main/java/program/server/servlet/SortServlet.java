package program.server.servlet;

import aspects.AspectProfile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Collection;

public class SortServlet extends Servlet {

    @Override
    @AspectProfile
    protected void get(String login, Map<String, String[]> command, HttpServletResponse resp) throws IOException {
        String[] args = command.get("input")[0].split(",");
        List<Integer> list = new ArrayList<>();
        for (String arg : args) {
            list.add(Integer.parseInt(arg));
        }
        Collections.sort(list);
        Collection<String> listString = new ArrayList<>();
        for (Integer i : list) {
            listString.add(String.valueOf(i));
        }
        String v = String.join(", ", listString);
        sendLineHtml(resp, v);
    }
}
