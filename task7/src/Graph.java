import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Graph {
    Edge[][] adjacencyMatrix;

    public Graph(int vertices) {
        //if indexing starts at 0
        vertices++;
        adjacencyMatrix = new Edge[vertices][vertices];
    }

    public static Graph readFromFile(String graphName) throws IOException {
        String path = "task7/src//" + graphName + ".txt";
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
                graph.addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing line: " + line);
            }
        }

        return graph;
    }

    public void addEdge(int from, int to, int capacity){
        if (adjacencyMatrix[from][to] == null) {
            adjacencyMatrix[from][to] = new Edge(capacity);
        } else {
            adjacencyMatrix[from][to].capacity += capacity;
        }

        if(adjacencyMatrix[to][from] == null){
            adjacencyMatrix[to][from] = new Edge(0);
        }
    }

    public List<Integer> bfs(int startVertex, int endVertex) {
        int n = adjacencyMatrix.length;
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] bottleneck = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = -1;
            bottleneck[i] = Integer.MAX_VALUE;
        }

        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVertex);
        visited[startVertex] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            if (u == endVertex) {
                List<Integer> path = new ArrayList<>();
                int bottleneckCapacity = bottleneck[endVertex];

                for (int v = endVertex; v != -1; v = parent[v]) {
                    path.add(0, v);
                }

                path.add(0, bottleneckCapacity); // add the bottleneck capacity at index 0
                return path;
            }

            for (int v = 0; v < n; v++) {
                if (adjacencyMatrix[u][v] != null && !visited[v] && adjacencyMatrix[u][v].getRestCapacity() > 0) {
                    parent[v] = u;
                    bottleneck[v] = Math.min(bottleneck[u], adjacencyMatrix[u][v].getRestCapacity());
                    visited[v] = true;
                    queue.add(v);
                }
            }
        }

        List<Integer> noPath = new ArrayList<>();
        noPath.add(0); // signifies no path found
        return noPath;
    }

    public void updateFlow(List<Integer> path) {
        int flowToAdd = path.get(0); // the first index contains the flow to add

        for (int i = 1; i < path.size() - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);

            // Update the flow for edge u -> v
            adjacencyMatrix[u][v].flow += flowToAdd;

            // Update the flow for edge v -> u
            adjacencyMatrix[v][u].flow -= flowToAdd;
        }
    }


    public void edmondsKarp(int source, int sink) {
        List<List<Integer>> allPaths = new ArrayList<>();
        int maxFlow = 0;

        while (true) {
            List<Integer> path = bfs(source, sink);

            if (path.get(0) == 0) { // no augmenting path found
                break;
            }

            // Update flow and bottleneck
            int bottleneck = path.get(0);
            updateFlow(path);
            maxFlow += bottleneck;

            // Add path for printing results later
            allPaths.add(path);
        }

        // Print results
        System.out.println("Maksimum flyt fra " + source + " til " + sink + " med Edmond-Karp");
        System.out.println("Økning : Flytøkende vei");

        for (List<Integer> path : allPaths) {
            int bottleneck = path.get(0);
            System.out.print(bottleneck + "  ");
            for (int i = 1; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i < path.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        System.out.println("Maksimal flyt ble " + maxFlow);
        System.out.println();
    }





    public static void main(String[] args) throws IOException {
        Graph graph1 = Graph.readFromFile("flytgraf1");
        Graph graph2 = Graph.readFromFile("flytgraf2");
        Graph graph3 = Graph.readFromFile("flytgraf3");
        Graph graph4 = Graph.readFromFile("flytgraf4");
        Graph graph5 = Graph.readFromFile("flytgraf5");

        graph1.edmondsKarp(0, 7);
        graph2.edmondsKarp(0, 1);
        graph3.edmondsKarp(0, 1);
        graph4.edmondsKarp(0, 7);
        graph5.edmondsKarp(0, 7);



    }
}