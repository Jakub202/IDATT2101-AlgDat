import java.io.*;
import java.util.*;

public class ALT {

    ArrayList<Node> nodes;

    ArrayList<ArrayList<Integer>> processedList;

    public ALT() {
        this.nodes = new ArrayList<>();
        this.processedList = new ArrayList<ArrayList<Integer>>();
    }

    public static void main(String[] args) throws IOException {

        ALT alt = new ALT();

        System.out.println("Loading processed file");
        alt.readProcessedFile();
        System.out.println("Loading nodes");
        alt.loadNodesFromFile();
        System.out.println("Loading edges");
        alt.loadEdgesFromFile();
        System.out.println("Loading interest points");
        alt.loadInterestPointsFromFile();


        int start =  2266026;

        int goal = 7826348;

        ArrayList<Node> path;


        long s = System.nanoTime();
        alt.getPathWithDijkstra(start,goal);
        long e = System.nanoTime();

        System.out.println("Estimert tid i hundredels sekunder: "+alt.nodes.get(goal).toStart);
        System.out.println(alt.timeConvert(alt.nodes.get(goal).toStart));
        System.out.println("Tid brukt for beregning i millisekund: " + (e-s)/1000000);
        System.out.println();

        s = System.nanoTime();
        path = alt.ALT(start,goal);
        e = System.nanoTime();



        System.out.println("Estimert tid i hundredels sekunder: "+alt.nodes.get(goal).toStart);
        System.out.println(alt.timeConvert(alt.nodes.get(goal).toStart));
        System.out.println("Tid brukt for beregning i millisekund: " + (e-s)/1000000);



    }

    public String timeConvert(int time){
        int total = time/100;
        int hours = total/3600;
        total = total%3600;
        int minutes = total/60;
        total = total%60;
        int seconds = total;
        return "Estimert tid = " + hours + "h " + minutes + "m " + seconds + "s";
    }

    public void loadInterestPointsFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/interessepkt.txt"));
        reader.readLine();
        String[] table;
        String line = reader.readLine();
        while(line != null){
            table = hsplit(line, 3);
            nodes.get(Integer.valueOf(table[0])).code = Integer.valueOf(table[1]);
            nodes.get(Integer.valueOf(table[0])).name = table[2];
            line = reader.readLine();
        }
    }

    public ArrayList<Node> loadClosestInterestPoint(int source, int code){
        nodeReset();
        PriorityQueue<Node> q = new PriorityQueue<>();
        nodes.get(source).toStart = 0;
        q.add(nodes.get(source));
        Node current;
        ArrayList<Node> points = new ArrayList<>();
        while(!q.isEmpty()){
            current = q.poll();
            int currentNr = current.nr;
            nodes.get(currentNr).settled = true;
            if((nodes.get(currentNr).code & code) == code){
                points.add(nodes.get(currentNr));
                if(points.size() == 8){
                    break;
                }
            }
            ArrayList<Integer> list =  nodes.get(currentNr).adjacentNodes;
            for (int i = 0; i < list.size(); i++) {

                int x = list.get(i);

                if(!nodes.get(x).settled){
                    int alternativeEstimate = nodes.get(currentNr).toStart + nodes.get(currentNr).getWeightToAdjacentNode(x);
                    if(alternativeEstimate < nodes.get(x).toStart){
                        q.remove(nodes.get(x));
                        nodes.get(x).toStart = alternativeEstimate;
                        nodes.get(x).previous = currentNr;
                        q.add(nodes.get(x));
                    }
                }
            }
        }
        return points;
    }

    public void loadNodesFromFile() throws IOException {
        nodes.clear();
        BufferedReader reader = new BufferedReader(new FileReader("src/noder.txt"));
        //skips the first line which contains total amount of nodes
        reader.readLine();
        String line = reader.readLine();
        String[] table;
        while (line != null){
            table = hsplit(line, 3);
            nodes.add(new Node(Integer.valueOf(table[0]),Double.valueOf(table[1]),Double.valueOf(table[2])));
            line = reader.readLine();
        }
    }

    public void loadEdgesFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/kanter.txt"));
        //skips the first line which contains total amount of edges
        reader.readLine();
        String line = reader.readLine();
        String[] table;
        while(line != null && line != ""){
            table = hsplit(line,3);
            nodes.get(Integer.valueOf(table[0])).adjacentNodes.add(Integer.valueOf(table[1]));
            nodes.get(Integer.valueOf(table[0])).adjacentNodesWeight.add(Integer.valueOf(table[2]));
            line = reader.readLine();
        }
    }

    //remember to reload nodes beforehand
    public void loadInverseEdges() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/kanter.txt"));
        //skips the first line which contains total amount of edges
        reader.readLine();
        String line = reader.readLine();
        String[] table;
        while(line != null){
            table = hsplit(line,3);
            nodes.get(Integer.valueOf(table[1])).adjacentNodes.add(Integer.valueOf(table[0]));
            nodes.get(Integer.valueOf(table[1])).adjacentNodesWeight.add(Integer.valueOf(table[2]));
            line = reader.readLine();
        }
    }

    public void preprocess() throws IOException {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        list.add(new ArrayList<>());
        list.add(new ArrayList<>());
        list.add(new ArrayList<>());
        list.add(new ArrayList<>());
        list.add(new ArrayList<>());
        list.add(new ArrayList<>());
        Integer[] landMarks = new Integer[3];

        landMarks[0] = 3156231;
        landMarks[1] = 982239;
        landMarks[2] = 3298403;


        for (int i = 0; i < landMarks.length; i++){
            System.out.println("Calculating to landmark number" + landMarks[i]);
            dijkstra(landMarks[i]);
            for(int j = 0; j < nodes.size(); j++){
                list.get(i).add(nodes.get(j).toStart);
            }
            nodeReset();
            System.out.println("To landmark " + landMarks[i] + " done");
        }

        loadNodesFromFile();
        loadInverseEdges();

        for (int i = 0; i < landMarks.length; i++){
            System.out.println("Calculating from landmark number" + landMarks[i]);
            dijkstra(landMarks[i]);
            for(int j = 0; j < nodes.size(); j++){
                list.get(i+3).add(nodes.get(j).toStart);
            }
            nodeReset();
            System.out.println("From landmark " + landMarks[i] + " done");
        }
        processedList = list;


        write("src/processed.txt",list);


        //skrive tabell fra Landmerke til alle noder til fil
        //skrive tabell til landmerke fra alle noder til fil

    }

    public void readProcessedFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/processed.txt"));
        String line = reader.readLine();
        int count = 0;
        while(line != null){
            String[] list = line.split(", ");
            processedList.add(new ArrayList<>());
            processedList.get(count).add(Integer.valueOf(list[0].substring(1)));
            for (int i = 1; i < list.length; i++){
                if(list[i].contains("]")){
                    processedList.get(count).add(Integer.valueOf(list[i].substring(0,list[i].length()-5)));
                }else{
                    processedList.get(count).add(Integer.valueOf(list[i]));
                }

            }
            count++;
            line = reader.readLine();
        }
    }

    public void write(String filename, ArrayList<ArrayList<Integer>> list) throws IOException{
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for(int i = 0; i < list.size(); i++){
            outputWriter.write(String.valueOf(list.get(i)));
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }

    public void writePathToFile(String filename, ArrayList<Node> list) throws IOException {
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for(int i = 0; i < list.size(); i++){
            outputWriter.write(list.get(i).toString());
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }

    public void dijkstra(int source) throws IOException {
        nodeReset();
        PriorityQueue<Node> q = new PriorityQueue<>();
        nodes.get(source).toStart = 0;
        q.add(nodes.get(source));
        Node current;
        while(!q.isEmpty()){
            current = q.poll();
            int currentNr = current.nr;
            nodes.get(currentNr).settled = true;
            ArrayList<Integer> list =  nodes.get(currentNr).adjacentNodes;
            for (int i = 0; i < list.size(); i++) {

                int x = list.get(i);

                if(!nodes.get(x).settled){
                    int alternativeEstimate = nodes.get(currentNr).toStart + nodes.get(currentNr).getWeightToAdjacentNode(x);
                    if(alternativeEstimate < nodes.get(x).toStart){
                        q.remove(nodes.get(x));
                        nodes.get(x).toStart = alternativeEstimate;
                        nodes.get(x).previous = currentNr;
                        q.add(nodes.get(x));
                    }
                }
            }
        }
    }


    public void dijkstraToGoal(int source, int goal) throws IOException {
        int count = 0;
        nodeReset();
        PriorityQueue<Node> q = new PriorityQueue<>();
        nodes.get(source).toStart = 0;
        q.add(nodes.get(source));
        Node current;
        while(!q.isEmpty()){
            current = q.poll();
            int currentNr = current.nr;
            count++;
            if(currentNr == goal){
                break;
            }
            nodes.get(currentNr).settled = true;
            ArrayList<Integer> list =  nodes.get(currentNr).adjacentNodes;
            for (int i = 0; i < list.size(); i++) {

                int x = list.get(i);

                if(!nodes.get(x).settled){
                    int alternativeEstimate = nodes.get(currentNr).toStart + nodes.get(currentNr).getWeightToAdjacentNode(x);
                    if(alternativeEstimate < nodes.get(x).toStart){
                        q.remove(nodes.get(x));
                        nodes.get(x).toStart = alternativeEstimate;
                        nodes.get(x).previous = currentNr;
                        q.add(nodes.get(x));
                    }
                }
            }
        }
        System.out.println("nodes used in dijkstra algorithm: " + count);
    }


    public ArrayList<Node> getPathWithDijkstra(int source, int goal) throws IOException {
        nodeReset();
        int count = 1;
        PriorityQueue<Node> q = new PriorityQueue<>();
        nodes.get(source).toStart = 0;
        q.add(nodes.get(source));
        Node current;
        while(!q.isEmpty()){
            current = q.poll();
            count++;
            nodes.get(current.nr).settled = true;
            if(current.nr == goal){
                break;
            }
            int currentNr = current.nr;
            getUnsettledAdjacentNodes(current.nr).forEach(x -> {
                if(!q.contains(nodes.get(x))){
                    q.add(nodes.get(x));
                }

                int alternativeEstimate = nodes.get(currentNr).toStart + nodes.get(currentNr).getWeightToAdjacentNode(x);
                if(alternativeEstimate < nodes.get(x).toStart){
                    q.remove(nodes.get(x));
                    nodes.get(x).toStart = alternativeEstimate;
                    q.add(nodes.get(x));
                    nodes.get(x).previous = currentNr;
                }
            });

        }
        ArrayList<Node> list = new ArrayList<>();
        int currentNr = goal;
        list.add(nodes.get(currentNr));
        while(currentNr != source){
            list.add(nodes.get(nodes.get(currentNr).previous));
            currentNr = nodes.get(currentNr).previous;
        }
        Collections.reverse(list);
        System.out.println("Noder brukt med dijkstra: " + count);
        return list;
    }

    public int getPathDistanceWithDijkstra(int source, int goal) throws IOException {
        getPathWithDijkstra(source,goal);
        return nodes.get(goal).toStart;
    }


    public ArrayList<Integer> getMultiplePathDistancesWithDijkstra(Node source,Integer[] goals) throws IOException {
        nodeReset();
        List<Integer> list = Arrays.stream(goals).toList();
        ArrayList<Integer> paths = new ArrayList<>();
        PriorityQueue<Node> q = new PriorityQueue<>();
        nodes.get(source.nr).toStart = 0;
        q.add(nodes.get(source.nr));
        Node current;
        while(!q.isEmpty()){
            current = q.poll();
            nodes.get(current.nr).settled = true;
            if(list.contains(current)){
                paths.add(list.indexOf(current),nodes.get(current.nr).toStart);
                if(paths.size() == list.size()){
                    break;
                }
            }
            int currentNr = current.nr;
            getUnsettledAdjacentNodes(current.nr).forEach(x -> {
                if(!q.contains(nodes.get(x))){
                    q.add(nodes.get(x));
                }

                int alternativeEstimate = nodes.get(currentNr).toStart + nodes.get(currentNr).getWeightToAdjacentNode(x);
                if(alternativeEstimate < nodes.get(x).toStart){
                    q.remove(nodes.get(x));
                    nodes.get(x).toStart = alternativeEstimate;
                    q.add(nodes.get(x));
                    nodes.get(x).previous = currentNr;
                }
            });
        }
        return paths;
    }

    public ArrayList<Node> ALT(int source, int goal) throws IOException {
        nodeReset();
        int count = 1;
        PriorityQueue<Node> q = new PriorityQueue<>();
        nodes.get(source).toStart = 0;
        q.add(nodes.get(source));
        Node current;
        while(!q.isEmpty()){
            current = q.poll();
            count++;
            if(current.nr == goal){
                break;
            }
            nodes.get(current.nr).settled = true;
            int currentNr = current.nr;
            getUnsettledAdjacentNodes(currentNr).forEach(x -> {
                if(!q.contains(nodes.get(x))){
                    setEndEstimate(x,goal);
                    q.add(nodes.get(x));
                }

                int alternativeEstimate = nodes.get(currentNr).toStart + nodes.get(currentNr).getWeightToAdjacentNode(x);
                if(alternativeEstimate < nodes.get(x).toStart){
                    q.remove(nodes.get(x));
                    nodes.get(x).toStart = alternativeEstimate;
                    nodes.get(x).previous = currentNr;
                    q.add(nodes.get(x));
                }
            });

        }
        ArrayList<Node> list = new ArrayList<>();
        int currentNr = goal;
        list.add(nodes.get(currentNr));
        while(currentNr != source){
            list.add(nodes.get(nodes.get(currentNr).previous));
            currentNr = nodes.get(currentNr).previous;
        }
        Collections.reverse(list);
        System.out.println("Noder brukt med ALT: " + count);
        return list;
    }

    public void setEndEstimate(int node, int goal){
        int var1 = Integer.MAX_VALUE;
        int var2 = Integer.MAX_VALUE;
        int var3 = Integer.MAX_VALUE;
        int currentEstimate = 0;
        for(int i = 0; i < 3; i++){
            var1 = processedList.get(i).get(goal);
            var2 = processedList.get(i).get(node);
            if(var1-var2 < 0){
                var3 = 0;
            }else {
                var3 = var1-var2;
            }
            var1 = processedList.get(i+3).get(node);
            var2 = processedList.get(i+3).get(goal);
            if(var1-var2 > var3){
                var3 = var1-var2;
            }
            if(var3 > currentEstimate){
                currentEstimate = var3;
            }
        }
        nodes.get(node).toEndEstimate = currentEstimate;
    }





    public  ArrayList<Integer> getUnsettledAdjacentNodes(int nodenr) throws IOException {
        ArrayList<Integer> list = new ArrayList<>();
        Node node = nodes.get(nodenr);
        for(int i = 0; i < node.adjacentNodes.size(); i++){
            if(!nodes.get(node.adjacentNodes.get(i)).settled){
                list.add(node.adjacentNodes.get(i));
            }
        }
        return list;
    }

    public static String[] hsplit(String line, int amount){
        int j = 0;
        int length = line.length();
        String[] table = new String[10];
        for(int i = 0; i < amount; i++){
            while(line.charAt(j) <= ' ') j++;
            int wordStart = j;
            while(j < length && line.charAt(j) > ' ') j++;
            table[i] = line.substring(wordStart, j);
        }
        return table;
    }

    public void resetNodeEstimates(){
        nodes.forEach(x ->{
            x.toStart = Integer.MAX_VALUE;
        });
    }

    public void resetSettledNodes(){
        nodes.forEach(x ->{
            x.settled = false;
        });
    }

    public void resetPreviousNodes(){
        nodes.forEach(x ->{
            x.previous = null;
        });
    }

    public void nodeReset(){
        nodes.forEach(x ->{
            x.previous = null;
            x.settled = false;
            //halvparten av Integer.MaxValue
            x.toStart = 1073741823;
            x.toEndEstimate = 0;
        });
    }


}
