import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

public class RoadMap {

    private int start;
    private int end;
    private int initialBudget;
    private Graph graph;
    private int toll;
    private int gain;

    public RoadMap(String inputFile) throws MapException{
        try{
            BufferedReader file = new BufferedReader(new FileReader(inputFile));

            // Discard the scale.
            file.readLine();

            // Store the starting destination.
            start = Integer.parseInt(file.readLine());

            // Store the end destination.
            end = Integer.parseInt(file.readLine());

            // Store the initial budget.
            initialBudget = Integer.parseInt(file.readLine());

            // Use the width and length to determine how many nodes are contained in the graph.
            int width = Integer.parseInt(file.readLine());
            int length = Integer.parseInt(file.readLine());
            graph = new Graph(width * length);

            // Store the toll and gain value.
            toll = Integer.parseInt(file.readLine());
            gain = Integer.parseInt(file.readLine());

            // The rest of the file contains the information about the edges contained in the graph,
            // call the buildGraph helper function to process this information.
            buildGraph(file, width, length, graph);

            file.close();
        }
        catch(IOException e){
            throw new MapException("File does not exist.");
        }
    }

    /**
     * Processes input from a text file to generate the edges of a graph.
     * When this method reads in the following characters, the associated edge types are assigned:
     *
     *  T = toll (1)
     *  F = free (0)
     *  C = compensation (-1)
     *
     * The input data has the property that nodes are arranged in a regular grid.
     *
     * @param file a BufferedReader of a text file where the next line contains the first line of edge data
     * @param width the number of nodes contained in one row of the graph
     * @param length the number of nodes in one column of the graph
     * @param gr the Graph object to which edges will be added
     */
    private void buildGraph(BufferedReader file, int width, int length, Graph gr){
        try {
            Node u, v;
            String lineOne, lineTwo;

            for(int l = 0; l < length; l++){

                // First adding all horizontal edges.
                lineOne = file.readLine();
                for(int w = 1; w < width * 2 - 1; w += 2){

                    // Find the nodes on each side (left and right) of a potential road.
                    u = gr.getNode(l * width + (int)Math.floor(w/2.0));
                    v = gr.getNode(l * width + (int)Math.ceil(w/2.0));

                    // Depending on the type of road, add the appropriate edge.
                    switch (lineOne.charAt(w)) {
                        case 'T':
                            gr.insertEdge(u, v, 1);
                            break;
                        case 'F':
                            gr.insertEdge(u, v, 0);
                            break;
                        case 'C':
                            gr.insertEdge(u, v, -1);
                            break;
                    }
                }

                // Add all of the verticals edges.
                lineTwo = file.readLine();
                if(lineTwo != null){
                    for(int w = 0; w < width * 2; w += 2) {

                        // Find the nodes on each side (below and above) of a potential road.
                        u = gr.getNode(l * width + w/2);
                        v = gr.getNode((l + 1) * width + w/2);

                        // Depending on the type of road, add the appropriate edge.
                        switch (lineTwo.charAt(w)) {
                            case 'T':
                                gr.insertEdge(u, v, 1);
                                break;
                            case 'F':
                                gr.insertEdge(u, v, 0);
                                break;
                            case 'C':
                                gr.insertEdge(u, v, -1);
                                break;
                        }
                    }
                }
            }
        } catch (IOException | GraphException e) {
            e.printStackTrace();
        }
    }

    public Graph getGraph(){
        return graph;
    }

    public int getStartingNode(){
        return start;
    }

    public int getDestinationNode(){
        return end;
    }

    public int getInitialMoney(){
        return initialBudget;
    }

    private boolean pathDFS(Node u, Node v, Stack s, int currentMoney){

        s.add(u);
        u.setMark(true);
        if(u == v){
            return true;
        }

        try {
            Iterator<Edge> edges = graph.incidentEdges(u);
            while(edges.hasNext()){
                Edge curEdge = edges.next();
                Node curNode = curEdge.secondEndpoint();
                int money = 0;

                if(curEdge.getType() == 1){
                    money = toll;
                }
                else if(curEdge.getType() == -1){
                    money = gain;
                }


                if(!curNode.getMark() && currentMoney - money >= 0){
                    if(pathDFS(curNode, v, s, currentMoney - money)){
                        return true;
                    }
                    s.pop();
                    curNode.setMark(false);
                }
            }
        } catch (GraphException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Iterator findPath(int start, int destination, int initialMoney){
        try {
            Stack<Node> s = new Stack<>();

            if(pathDFS(graph.getNode(start), graph.getNode(destination), s, initialMoney)){
                return s.iterator();
            }
        } catch (GraphException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            for(int i = 2; i < 3; i++) {
                RoadMap x = new RoadMap("map" + i);
                x.findPath(x.getStartingNode(), x.getDestinationNode(), x.getInitialMoney());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

