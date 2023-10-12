import java.util.*;
import java.io.*;

public class Graph {
    private final LinkedList<Integer>[] adjacencyList;
    private final int vertexCount;

    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        adjacencyList = new LinkedList[vertexCount];
        for (int i = 0; i < vertexCount; ++i) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    public static Graph readFromFile(String graphName) throws IOException {
        String path = "task6/src//" + graphName + ".txt";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String[] firstLine = reader.readLine().split(" ");
        int vertexCount = Integer.parseInt(firstLine[0]);
        Graph graph = new Graph(vertexCount);

        String line;
        while ((line = reader.readLine()) != null) {
            // Skip empty lines
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            try {
                graph.addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing line: " + line);
            }
        }

        return graph;
    }


    public void addEdge(int from, int to) {
        adjacencyList[from].add(to);
    }

    public void bfs(int startVertex) {
        boolean[] visited = new boolean[vertexCount];
        Map<Integer, Integer> predecessor = new HashMap<>();
        Map<Integer, Integer> distance = new HashMap<>();

        Queue<Integer> queue = new LinkedList<>();
        visited[startVertex] = true;
        distance.put(startVertex, 0);
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();
            //System.out.println("Current vertex: " + currentVertex);  // Debug print
            for (int adjacentVertex : adjacencyList[currentVertex]) {
                //System.out.println("  Checking adjacent: " + adjacentVertex);  // Debug print
                if (!visited[adjacentVertex]) {
                    visited[adjacentVertex] = true;
                    queue.add(adjacentVertex);
                    predecessor.put(adjacentVertex, currentVertex);
                    distance.put(adjacentVertex, distance.get(currentVertex) + 1);
                    //System.out.println("    Marking predecessor: " + currentVertex);  // Debug print
                }
            }
        }

        // Print results
        System.out.println("Node  Forgj  Dist");
        for (int i = 0; i < vertexCount; ++i) {
            System.out.print(i + "     ");
            if (predecessor.get(i) != null) {
                System.out.print(predecessor.get(i) + "     ");
            } else {
                System.out.print("N/A   ");
            }
            if (distance.get(i) != null) {
                System.out.println(distance.get(i));
            } else {
                System.out.println("N/A");
            }
        }
    }

    public void topologicalSort() {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, stack);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        for (int vertex : result) {
            System.out.print(vertex + " ");
        }
    }

    private void topologicalSortUtil(int vertex, boolean[] visited, Stack<Integer> stack) {
        visited[vertex] = true;

        for (int adjacentVertex : adjacencyList[vertex]) {
            if (!visited[adjacentVertex]) {
                topologicalSortUtil(adjacentVertex, visited, stack);
            }
        }

        stack.push(vertex);
    }

    public static void main(String[] args) throws IOException {
        Graph graph1 = Graph.readFromFile("ø6g1");
        Graph graph2 = Graph.readFromFile("ø6g2");
        Graph graph3 = Graph.readFromFile("ø6g3");
        Graph graph5 = Graph.readFromFile("ø6g5");
        Graph graph7 = Graph.readFromFile("ø6g7");

        System.out.println("Breath-first search:");
        System.out.println("Graph 1:");
        graph1.bfs(0);
        System.out.println();

        System.out.println("Graph 2:");
        graph2.bfs(0);
        System.out.println();

        System.out.println("Graph 3:");
        graph3.bfs(0);
        System.out.println();

        System.out.println("Graph 5:");
        graph5.bfs(5);
        System.out.println();

        System.out.println("Graph 7:");
        graph7.bfs(0);
        System.out.println();

        System.out.println("Topological sort:");
        System.out.println("Graph 5:");
        graph5.topologicalSort();
        System.out.println();

        System.out.println("Graph 7:");
        graph7.topologicalSort();


    }
}
