package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductServer {
    private final String connectionDatabase;
    private final int port;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ProductServer(String connectionDatabase, int port) {
        this.connectionDatabase = connectionDatabase;
        this.port = port;
    }

    public void run() {
        executorService.submit(() -> {
            try (Connection c = DriverManager.getConnection(connectionDatabase)) {
                String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " NAME           TEXT    NOT NULL, " +
                        " PRICE          INT     NOT NULL)";
                Statement stmt = c.createStatement();

                stmt.executeUpdate(sql);
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Server server = new Server(port);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            context.addServlet(new ServletHolder(new AddProductServlet(connectionDatabase)), "/add-product");
            context.addServlet(new ServletHolder(new GetProductsServlet(connectionDatabase)), "/get-products");
            context.addServlet(new ServletHolder(new QueryServlet(connectionDatabase)), "/query");

            try {
                server.start();
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        executorService.shutdown();
    }
}
