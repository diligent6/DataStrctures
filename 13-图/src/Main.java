import java.util.Map;

public class Main {

    public static void main(String[] args) {
        testMultiSp();
    }

    static Graph.WeightManager<Double> weightManager = new Graph.WeightManager<Double>() {
        public int compare(Double w1, Double w2) {
            return w1.compareTo(w2);
        }

        public Double add(Double w1, Double w2) {
            return w1 + w2;
        }

        @Override
        public Double zero() {
            return 0.0;
        }
    };
    /**
     * 有向图
     */
    private static Graph<Object, Double> directedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph<>(weightManager);
        for (Object[] edge : data) {
            if (edge.length == 1) {
                graph.addVertex(edge[0]);
            } else if (edge.length == 2) {
                graph.addEdge(edge[0], edge[1]);
            } else if (edge.length == 3) {
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
            }
        }
        return graph;
    }
    static void testMultiSp() {
        Graph<Object, Double> graph = directedGraph(Data.NEGATIVE_WEIGHT1);
        Map<Object, Map<Object, Graph.PathInfo<Object, Double>>> sp = graph.shortPath();
        sp.forEach((Object from, Map<Object, Graph.PathInfo<Object, Double>> paths) -> {
            System.out.println(from + "---------------------");
            paths.forEach((Object to, Graph.PathInfo<Object, Double> path) -> {
                System.out.println(to + " - " + path);
            });
        });
    }

}
