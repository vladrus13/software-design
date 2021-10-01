package ru.akirakozov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.Main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServletTest {

    public static Logger logger = Logger.getLogger(ServletTest.class.getName());

    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @BeforeClass
    public static void beforeAll() {
        try {
            LogManager.getLogManager().readConfiguration(ServletTest.class.getResourceAsStream("/logging_test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.submit(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterClass
    public static void afterALl() {
        executorService.shutdown();
    }


    private static void removeAllFromTable() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();

            stmt.executeUpdate("DELETE FROM PRODUCT");
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void endTest() {
        try {
            removeAllFromTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmpty() {
        endTest();
    }

    private String sendRequest(String uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    @Test
    public void testAdd() throws Exception {
        sendRequest("http://localhost:8081/add-product?name=apple_heh&price=100");
        endTest();
    }

    @Test
    public void testAddAndCheck() throws IOException, InterruptedException {
        sendRequest("http://localhost:8081/add-product?name=apple_heh&price=100");
        sendRequest("http://localhost:8081/add-product?name=samsung_heh&price=78");
        sendRequest("http://localhost:8081/add-product?name=xiomi_heh&price=18");
        sendRequest("http://localhost:8081/add-product?name=nokia_heh&price=19");
        String body = sendRequest("http://localhost:8081/get-products");
        Element element = Jsoup.parse(body).body();
        List<String[]> list = element.childNodes().stream()
                .filter(it -> it instanceof TextNode)
                .map(it -> (TextNode) it)
                .map(TextNode::getWholeText)
                .filter(it -> !it.isBlank())
                .map(String::trim)
                .map(it -> it.split("\\s(?=\\b(\\d+(?:\\.\\d+)?)$)"))
                .collect(Collectors.toList());

    }
}
