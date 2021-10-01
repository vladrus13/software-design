package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends ExecuteServlet {

    @Override
    protected String getUpdate(Parameters parameters) {
        String name = parameters.parameters.get("name")[0];
        long price = Long.parseLong(parameters.parameters.get("price")[0]);
        return String.format("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"%s\",%d)", name, price);
    }

    @Override
    protected void updateResponse(Parameters parameters, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
