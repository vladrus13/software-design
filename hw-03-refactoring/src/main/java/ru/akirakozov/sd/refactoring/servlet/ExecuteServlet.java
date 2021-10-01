package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class ExecuteServlet extends Servlet {
    public ExecuteServlet(String connectionDatabase, Logger logger) {
        super(connectionDatabase, logger);
    }

    protected abstract String getUpdate(Parameters parameters);

    protected abstract void updateResponse(Parameters parameters, HttpServletResponse response) throws IOException;

    @Override
    protected void statementGet(Parameters parameters, Statement statement, HttpServletResponse response) throws SQLException, IOException {
        statement.executeUpdate(getUpdate(parameters));
        updateResponse(parameters, response);
    }
}
