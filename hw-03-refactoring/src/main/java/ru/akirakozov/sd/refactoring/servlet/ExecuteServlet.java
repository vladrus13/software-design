package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Servlet, executing response
 *
 * @author vladrus13
 */
public abstract class ExecuteServlet extends Servlet {
    /**
     * @param connectionDatabase address where we send to database
     * @param logger             logger of server
     */
    public ExecuteServlet(String connectionDatabase, Logger logger) {
        super(connectionDatabase, logger);
    }

    /**
     * Get update which
     *
     * @param parameters parameters of request
     * @return update
     */
    protected abstract String getUpdate(Parameters parameters);

    /**
     * Update response with parameters
     *
     * @param parameters parameters
     * @param response   response
     * @throws IOException if we have problems with html
     */
    protected abstract void updateResponse(Parameters parameters, HttpServletResponse response) throws IOException;

    @Override
    protected void statementGet(Parameters parameters, Statement statement, HttpServletResponse response) throws SQLException, IOException {
        statement.executeUpdate(getUpdate(parameters));
        updateResponse(parameters, response);
    }
}
