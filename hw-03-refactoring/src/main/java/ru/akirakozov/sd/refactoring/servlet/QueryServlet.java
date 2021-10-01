package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author akirakozov
 */
public class QueryServlet extends MethodServlet {
    /**
     * @param connectionDatabase address where we send to database
     * @param logger             logger of server
     */
    public QueryServlet(String connectionDatabase, Logger logger) {
        super(connectionDatabase, logger);
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
        sendBeginHTML(response);
        switch (command) {
            case "max":
                sendLineHtml(response, "<h1>Product with max price: </h1>");
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    sendLineHtml(response, name + "\t" + price + "</br>");
                }
                break;
            case "min":
                sendLineHtml(response, "<h1>Product with min price: </h1>");
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    sendLineHtml(response, name + "\t" + price + "</br>");
                }
                break;
            case "sum":
                sendLineHtml(response, "Summary price: ");
                if (resultSet.next()) {
                    sendLineHtml(response, String.valueOf(resultSet.getInt(1)));
                }
                break;
            case "count":
                sendLineHtml(response, "Number of products: ");
                if (resultSet.next()) {
                    sendLineHtml(response, String.valueOf(resultSet.getInt(1)));
                }
                break;
            default:
                throw new IllegalStateException("Getting response from invalid parameters");
        }
        sendEndHTML(response);
    }
}
