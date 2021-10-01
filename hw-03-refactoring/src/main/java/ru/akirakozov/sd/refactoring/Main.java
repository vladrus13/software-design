package ru.akirakozov.sd.refactoring;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) {
        ProductServer productServer = new ProductServer("jdbc:sqlite:test.db", 8081);
        productServer.run();
    }
}
