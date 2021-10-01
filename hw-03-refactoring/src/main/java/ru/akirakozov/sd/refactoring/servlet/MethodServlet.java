package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public abstract class MethodServlet extends HttpServlet {

    protected abstract String getQuery(HttpServletRequest request);

    protected abstract void updateResponse(ResultSet resultSet, HttpServletResponse response);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getQuery(req));
            updateResponse(resultSet, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
