package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author akirakozov
 */
public class AddProductServlet extends ExecuteServlet {

    /**
     * @param connectionDatabase address where we send to database
     * @param logger             logger of server
     */
    public AddProductServlet(String connectionDatabase, Logger logger) {
        super(connectionDatabase, logger);
    }

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
        sendLineHtml(response, "OK");
    }
}
