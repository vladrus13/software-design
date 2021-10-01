package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ProductServer {
    private final String connectionDatabase;
    private final int port;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Logger logger = Logger.getLogger(ProductServer.class.getName());

    public ProductServer(String connectionDatabase, int port) throws IOException {
        this.connectionDatabase = connectionDatabase;
        this.port = port;
        LogManager.getLogManager().readConfiguration(ProductServer.class.getResourceAsStream("/logging_database.properties"));
    }

    public void run() {
        logger.info(String.format("Started server at port %d and database %s", port, connectionDatabase));
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

            context.addServlet(new ServletHolder(new AddProductServlet(connectionDatabase, logger)), "/add-product");
            context.addServlet(new ServletHolder(new GetProductsServlet(connectionDatabase, logger)), "/get-products");
            context.addServlet(new ServletHolder(new QueryServlet(connectionDatabase, logger)), "/query");

            try {
                server.start();
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        logger.info(String.format("Stopped server at port %d and database %s", port, connectionDatabase));
        executorService.shutdown();
    }
}
