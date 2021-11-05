package aspects;

import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableNode;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Euler {

    public static final ConcurrentHashMap<String, Vertex> parents = new ConcurrentHashMap<>();

    public static void into(String login, String mark) {
        if (!parents.containsKey(login)) {
            parents.put(login, Vertex.createVertex(null, login));
        }
        Vertex vertex = parents.get(login);
        Vertex newVertex = Vertex.createVertex(vertex, mark);
        vertex.add(newVertex);
        parents.put(login, newVertex);
    }

    public static void onto(String login, long time) {
        Vertex vertex = parents.get(login);
        vertex.time = time;
        parents.put(login, vertex.parent);
    }

    public static class Vertex {
        private final ArrayList<Vertex> childs;
        private final Vertex parent;
        private final String mark;
        public long time = 0;

        private Vertex(Vertex parent, String mark) {
            this.childs = new ArrayList<>();
            this.parent = parent;
            this.mark = mark;
        }

        public static Vertex createVertex(Vertex parent, String mark) {
            return new Vertex(parent, mark);
        }

        public MutableNode getNode() {
            MutableNode node = Factory.mutNode((mark == null ? "null" : mark) + " " + time / 1000 + " ms time");
            for (Vertex vertex : childs) {
                node.addLink(vertex.getNode());
            }
            return node;
        }

        public void add(Vertex child) {
            childs.add(child);
        }

        public ArrayList<Vertex> getChilds() {
            return childs;
        }

        public Vertex getParent() {
            return parent;
        }
    }
}
