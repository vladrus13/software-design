package ru.akirakozov.sd.refactoring;

/**
 * @author akirakozov
 */
public class Main {
    /**
     * @param args arguments of main
     * @throws Exception if we get exception on creating database
     */
    public static void main(String[] args) throws Exception {
        new ProductServer("jdbc:sqlite:test.db", 8081);
    }
}
