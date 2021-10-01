package ru.akirakozov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.Main;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ALL")
public class ProductionServerTest {

    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @BeforeClass
    public static void beforeAll() {
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
        } catch (SQLException e) {
            e.printStackTrace();
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
        while (true) {
            try {
                return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            } catch (ConnectException ignored) {
            }
        }
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
        Set<String[]> set = element.childNodes().stream()
                .filter(it -> it instanceof TextNode)
                .map(it -> (TextNode) it)
                .map(TextNode::getWholeText)
                .filter(it -> !it.isBlank())
                .map(String::trim)
                .map(it -> it.split("\\s(?=\\b(\\d+(?:\\.\\d+)?)$)"))
                .map(it -> Arrays.stream(it).map(String::trim).collect(Collectors.toList()).toArray(new String[]{}))
                .collect(Collectors.toSet());
        assertTrue(set.stream().allMatch(it ->
                (it[0].equals("apple_heh") && it[1].equals("100")) ||
                        (it[0].equals("samsung_heh") && it[1].equals("78")) ||
                        (it[0].equals("xiomi_heh") && it[1].equals("18")) ||
                        (it[0].equals("nokia_heh") && it[1].equals("19"))));
        endTest();
    }

    public String getCommand(String answer) {
        return Jsoup.parse(answer).body().childNodes().get(2).toString().trim().split("\\s(?=\\b(\\d+(?:\\.\\d+)?)$)")[1];
    }

    public String getEdit(String answer, int number) {
        return Jsoup.parse(answer).body().childNodes().get(0).toString().split(" ")[number];
    }

    @Test
    public void testFunctions() throws IOException, InterruptedException {
        sendRequest("http://localhost:8081/add-product?name=apple_heh&price=100");
        sendRequest("http://localhost:8081/add-product?name=samsung_heh&price=78");
        sendRequest("http://localhost:8081/add-product?name=xiomi_heh&price=18");
        sendRequest("http://localhost:8081/add-product?name=nokia_heh&price=19");
        sendRequest("http://localhost:8081/add-product?name=ban_heh&price=50");
        assertEquals(getCommand(sendRequest("http://localhost:8081/query?command=max")), "100");
        assertEquals(getCommand(sendRequest("http://localhost:8081/query?command=min")), "18");
        assertEquals(getEdit(sendRequest("http://localhost:8081/query?command=sum"), 3), "265");
        assertEquals(getEdit(sendRequest("http://localhost:8081/query?command=count"), 4), "5");
        endTest();
    }
}
