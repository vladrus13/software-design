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

/**
 * Server, answering by servlets
 *
 * @author vladrus13
 */
public class ProductServer {
    /**
     * Main logger of server
     */
    public static final Logger logger = Logger.getLogger(ProductServer.class.getName());
    /**
     * Server's port
     */
    public final int port;
    /**
     * Service we launch server
     */
    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final String connectionDatabase;

    /**
     * @param connectionDatabase address where we send to database
     * @param port               port where server launch
     * @throws IOException if we got exception on creating database
     */
    public ProductServer(String connectionDatabase, int port) throws IOException {
        this.connectionDatabase = connectionDatabase;
        this.port = port;
        run();
    }

    private void run() throws IOException {
        LogManager.getLogManager().readConfiguration(ProductServer.class.getResourceAsStream("/logging_database.properties"));
        logger.info(String.format("Started server at port %d and database %s", port, connectionDatabase));
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
        executorService.submit(() -> {

            try {
                server.start();
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Stops server
     */
    public void stop() {
        logger.info(String.format("Stopped server at port %d and database %s", port, connectionDatabase));
        executorService.shutdown();
    }
}
