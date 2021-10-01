package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Servlet, executing method
 *
 * @author vladrus13
 */
public abstract class MethodServlet extends Servlet {
    /**
     * @param connectionDatabase address where we send to database
     * @param logger             logger of server
     */
    public MethodServlet(String connectionDatabase, Logger logger) {
        super(connectionDatabase, logger);
    }

    /**
     * Get query for database
     *
     * @param parameters parameters of request
     * @return query for database
     */
    protected abstract String getQuery(Parameters parameters);

    /**
     * Get response on invalid request
     *
     * @return response on invalid request
     */
    protected String getInvalidAnswer() {
        return null;
    }

    /**
     * Update response with parameters and result from database query answer
     *
     * @param parameters parameters of request
     * @param resultSet  result from database query answer
     * @param response   response
     * @throws IOException  if we have problems with html
     * @throws SQLException if we have problems with database
     */
    protected abstract void updateResponse(Parameters parameters, ResultSet resultSet, HttpServletResponse response) throws IOException, SQLException;

    @Override
    protected void statementGet(Parameters parameters, Statement statement, HttpServletResponse response) throws SQLException, IOException {
        String query = getQuery(parameters);
        if (query == null) {
            sendBeginHTML(response);
            String invalidAnswer = getInvalidAnswer();
            if (invalidAnswer != null) {
                sendLineHtml(response, invalidAnswer);
            }
            sendEndHTML(response);
        } else {
            ResultSet resultSet = statement.executeQuery(getQuery(parameters));
            updateResponse(parameters, resultSet, response);
            resultSet.close();
        }
    }
}
