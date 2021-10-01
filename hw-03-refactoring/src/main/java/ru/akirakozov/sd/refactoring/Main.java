package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;

import java.io.IOException;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ProductServer productServer = new ProductServer("jdbc:sqlite:test.db", 8081);
    }
}
