import java.util.ArrayList;
import java.util.Collections;

public class Node implements Comparable<Node> {
    int nr;
    Double width;
    Double length;
    int toEndEstimate;
    int toStart;
    Integer previous = null;

    boolean settled = false;

    int code;

    String name;

    public ArrayList<Integer> adjacentNodes;

    //the index of the weight is the index of the corresponding node in adjacent Nodes list
    public ArrayList<Integer> adjacentNodesWeight;

    public Node(int nr, Double width, Double length) {
        this.nr = nr;
        this.width = width;
        this.length = length;
        this.toStart = Integer.MAX_VALUE;
        this.toEndEstimate = 0;
        this.code = 0;
        adjacentNodes = new ArrayList<>();
        adjacentNodesWeight = new ArrayList<>();
    }

    public int getSum(){
        return toEndEstimate + toStart;
    }

    //nodes with the least estimated time to end are prioritized
    @Override
    public int compareTo(Node n) {
        if(getSum() < n.getSum()){
            return -1;
        }else if(getSum() > n.getSum()){
            return 1;
        }return 0;
    }

    public int getWeightToAdjacentNode(int nodeNr) throws IllegalArgumentException {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i<adjacentNodes.size(); i++){
            if(adjacentNodes.get(i) == nodeNr){
                list.add(adjacentNodesWeight.get(i));
            }
        }
        return Collections.min(list);
    }

    public String toString(){
        return String.valueOf(nr);
    }
}
