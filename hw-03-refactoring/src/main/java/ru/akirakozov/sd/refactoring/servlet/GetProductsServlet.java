package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends MethodServlet {

    public GetProductsServlet(String connectionDatabase, Logger logger) {
        super(connectionDatabase, logger);
    }

    @Override
    protected String getQuery(Parameters parameters) {
        return "SELECT * FROM PRODUCT";
    }

    @Override
    protected void updateResponse(Parameters parameters, ResultSet resultSet, HttpServletResponse response) throws IOException, SQLException {
        response.getWriter().println("<html><body>");
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int price = resultSet.getInt("price");
            response.getWriter().println(name + "\t" + price + "</br>");
        }
        response.getWriter().println("</body></html>");
    }
}
