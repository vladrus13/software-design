package ru.akirakozov.sd.refactoring;

import java.io.IOException;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ProductServer productServer = new ProductServer("jdbc:sqlite:test.db", 8081);
        productServer.run();
    }
}
