package ru.akirakozov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.ProductServer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerTest {

    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";
    private final Random random = new Random();

    @BeforeClass
    public static void beforeAll() throws IOException {
        Files.createDirectories(Path.of("tests-db"));
    }

    @AfterClass
    public static void afterAll() throws IOException {
        Files.walkFileTree(Path.of("tests-db"), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public ProductServer startServer() {
        Path path = Path.of("tests-db");
        int port = 8082;
        while (true) {
            try {
                Path table = path.resolve(String.format("test%d.db", port));
                if (!table.toFile().exists()) {
                    return new ProductServer(String.format("jdbc:sqlite:%s", table), port);
                } else {
                    port++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer(ProductServer productServer) throws IOException {
        int port = productServer.port;
        productServer.stop();
        Path table = Path.of("tests-db").resolve(String.format("test%d.db", port));
        Files.delete(table);
    }

    @Test
    public void testEmpty() throws IOException {
        ProductServer productServer = startServer();
        stopServer(productServer);
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

    private String sendRequest(int port, String type, Map<String, String> parameters) throws IOException, InterruptedException {
        String request = String.format("http://localhost:%d/%s?%s",
                port,
                type,
                parameters.entrySet().stream()
                        .map(it -> String.format("%s=%s", it.getKey(), it.getValue()))
                        .collect(Collectors.joining("&")));
        return sendRequest(request);
    }

    @Test
    public void testAdd() throws Exception {
        ProductServer productServer = startServer();
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of(
                "name", "apple_heh", "price", "100"
        )));
        stopServer(productServer);
    }

    @Test
    public void testAddAndCheck() throws IOException, InterruptedException {
        ProductServer productServer = startServer();
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "apple_heh", "price", "100")));
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "samsung_heh", "price", "78")));
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "xiomi_heh", "price", "18")));
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "nokia_heh", "price", "19")));
        String body = sendRequest(productServer.port, "get-products", new HashMap<>());
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
        stopServer(productServer);
    }

    public String getCommand(String answer) {
        return Jsoup.parse(answer).body().childNodes().get(2).toString().trim().split("\\s(?=\\b(\\d+(?:\\.\\d+)?)$)")[1];
    }

    public String getEdit(String answer, int number) {
        return Jsoup.parse(answer).body().childNodes().get(0).toString().split(" ")[number];
    }

    @Test
    public void testFunctions() throws IOException, InterruptedException {
        ProductServer productServer = startServer();
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "apple_heh", "price", "100")));
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "samsung_heh", "price", "78")));
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "xiomi_heh", "price", "18")));
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "nokia_heh", "price", "19")));
        sendRequest(productServer.port, "add-product", new HashMap<>(Map.of("name", "ban_heh", "price", "50")));
        assertEquals(getCommand(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "max")))),
                "100");
        assertEquals(getCommand(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "min")))),
                "18");
        assertEquals(getEdit(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "sum"))), 3),
                "265");
        assertEquals(getEdit(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "count"))), 4),
                "5");
        stopServer(productServer);
    }

    public String generateRandomString() {
        int length = random.nextInt(6) + 4;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }

    @Test
    public void testBigFunctions() throws IOException, InterruptedException {
        ProductServer productServer = startServer();
        int max = 0;
        int min = 9999;
        int sum = 0;
        int count = 0;
        Set<String> used = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String newName;
            do {
                newName = generateRandomString();
            } while (used.contains(newName));
            used.add(newName);
            int price = random.nextInt(9599) + 5;
            max = Math.max(max, price);
            min = Math.min(min, price);
            sum += price;
            count++;
            sendRequest(productServer.port, "add-product", new HashMap<>(Map.of(
                    "name", newName,
                    "price", String.valueOf(price))));
        }
        assertEquals(getCommand(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "max")))),
                String.valueOf(max));
        assertEquals(getCommand(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "min")))),
                String.valueOf(min));
        assertEquals(getEdit(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "sum"))), 3),
                String.valueOf(sum));
        assertEquals(getEdit(
                        sendRequest(productServer.port, "query", new HashMap<>(Map.of("command", "count"))), 4),
                String.valueOf(count));
        stopServer(productServer);
    }
}
