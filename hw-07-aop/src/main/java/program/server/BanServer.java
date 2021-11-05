package program.server;

import aspects.Euler;
import aspects.Timer;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import program.server.servlet.ExitServlet;
import program.server.servlet.FibonacciServlet;
import program.server.servlet.SortServlet;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class BanServer {
    public static final int WIDTH = 10000;
    public static final int port = 8080;

    private static Server server = null;

    public static void start() throws Exception {
        if (server == null) {
            server = new Server(port);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            context.addServlet(new ServletHolder(new FibonacciServlet()), "/fibonacci");
            context.addServlet(new ServletHolder(new SortServlet()), "/sort");
            context.addServlet(new ServletHolder(new ExitServlet()), "/exit");

            server.start();
        }
    }

    public static void stop() {
        try {
            for (Map.Entry<String, Euler.Vertex> entry : Euler.parents.entrySet()) {
                Graph g = Factory.mutGraph(entry.getKey()).add(entry.getValue().getNode()).toImmutable();
                Graphviz.fromGraph(g).width(WIDTH).render(Format.PNG).toFile(new File("result/" + entry.getKey() + ".png"));
            }
            try (BufferedWriter writer = Files.newBufferedWriter(new File("result/result.txt").toPath())) {
                for (Map.Entry<String, List<Long>> entry : Timer.map.entrySet()) {
                    writer.write("\n=== " + entry.getKey() + " ===");
                    writer.newLine();
                    for (long e : entry.getValue()) {
                        writer.write(e / 1000 + " ");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
