public class Edge {
    int capacity;
    int flow;

    public Edge(int capacity) {
        this.capacity = capacity;
        this.flow = 0;
    }

    public int getRestCapacity() {
        return capacity - flow;
    }

}
