package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class QueryServlet extends MethodServlet {

    public QueryServlet(String connectionDatabase) {
        super(connectionDatabase);
    }

    @Override
    protected String getQuery(Parameters parameters) {
        String command = parameters.parameters.get("command")[0];
        switch (command) {
            case "max":
                return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
            case "min":
                return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
            case "sum":
                return "SELECT SUM(price) FROM PRODUCT";
            case "count":
                return "SELECT COUNT(*) FROM PRODUCT";
            default:
                return null;
        }
    }

    @Override
    protected String getInvalidAnswer() {
        return "Invalid command";
    }

    @Override
    protected void updateResponse(Parameters parameters, ResultSet resultSet, HttpServletResponse response) throws IOException, SQLException {
        String command = parameters.parameters.get("command")[0];
        response.getWriter().println("<html><body>");
        switch (command) {
            case "max":
                response.getWriter().println("<h1>Product with max price: </h1>");
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    response.getWriter().println(name + "\t" + price + "</br>");
                }
                break;
            case "min":
                response.getWriter().println("<h1>Product with min price: </h1>");
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    response.getWriter().println(name + "\t" + price + "</br>");
                }
                break;
            case "sum":
                response.getWriter().println("Summary price: ");
                if (resultSet.next()) {
                    response.getWriter().println(resultSet.getInt(1));
                }
                break;
            case "count":
                response.getWriter().println("Number of products: ");
                if (resultSet.next()) {
                    response.getWriter().println(resultSet.getInt(1));
                }
                break;
            default:
                throw new IllegalStateException("Getting response from invalid parameters");
        }
        response.getWriter().println("</body></html>");
    }
}
