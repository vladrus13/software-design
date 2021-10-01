package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Servlet abstract class
 *
 * @author vladrus13
 */
public abstract class Servlet extends HttpServlet {

    /**
     * Address where we send to database
     */
    protected final String connectionDatabase;
    /**
     * Logger of server
     */
    protected final Logger logger;


    /**
     * @param connectionDatabase address where we send to database
     * @param logger             logger of server
     */
    public Servlet(String connectionDatabase, Logger logger) {
        this.connectionDatabase = connectionDatabase;
        this.logger = logger;
    }

    /**
     * Parse request to parameters
     *
     * @param request request
     * @return parameters of request
     */
    protected Parameters parse(HttpServletRequest request) {
        return new Parameters(request);
    }

    /**
     * Do get of statement
     *
     * @param parameters parameters of request
     * @param statement  statement
     * @param response   total response
     * @throws SQLException if we can't connect to database
     * @throws IOException  if we get problems with input
     */
    protected abstract void statementGet(Parameters parameters, Statement statement, HttpServletResponse response) throws SQLException, IOException;

    /**
     * Get head of html
     *
     * @param response response
     * @throws IOException if we have problem with html
     */
    public void sendBeginHTML(HttpServletResponse response) throws IOException {
        sendLineHtml(response, "<html><body>");
    }

    /**
     * Get end of html
     *
     * @param response response
     * @throws IOException if we have problem with html
     */
    public void sendEndHTML(HttpServletResponse response) throws IOException {
        sendLineHtml(response, "</body></html>");
    }

    /**
     * Send line to response
     *
     * @param response response
     * @param s        line we write to response
     * @throws IOException if we have problem with html
     */
    public void sendLineHtml(HttpServletResponse response, String s) throws IOException {
        response.getWriter().println(s);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Get request: " + request.toString());
        try (Connection connection = DriverManager.getConnection(connectionDatabase)) {
            Statement statement = connection.createStatement();
            Parameters parameters = parse(request);
            statementGet(parameters, statement, response);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Parameters of request
     */
    static class Parameters {

        /**
         * Parameters
         */
        public final Map<String, String[]> parameters;

        /**
         * @param request request
         */
        Parameters(HttpServletRequest request) {
            parameters = request.getParameterMap();
        }
    }
}
