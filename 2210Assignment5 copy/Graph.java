import java.util.Iterator;
import java.util.LinkedList;

public class Graph implements GraphADT{

    private LinkedList<Node>[] adjacencyList;
    private LinkedList<Edge>[] edgeList;
    private Node[] nodes;

    public Graph(int n){

        // Initialize lists with the number of nodes contained in the graph.
        adjacencyList = new LinkedList[n];
        edgeList = new LinkedList[n];
        nodes = new Node[n];

        // Create all nodes in the graph, and initialize their adjacency lists as empty linked lists.
        for(int i = 0; i < n; i++){
            nodes[i] = new Node(i);
            adjacencyList[i] = new LinkedList<>();
            edgeList[i] = new LinkedList<>();
        }
    }

    /**
     * Checks if a given integer is between 0 and the number of nodes in the graph.
     *
     * @param name an integer
     * @return true if 0 <= name < # nodes in graph, false otherwise
     */
    private boolean isNameInGraph(int name){
        return !(name < 0 || name >= nodes.length);
    }

    public void insertEdge(Node nodeu, Node nodev, int edgeType) throws GraphException {

        // Check if the name of the nodes can be in the graph.
        if(!(isNameInGraph(nodeu.getName()) || isNameInGraph(nodev.getName()))){
            throw new GraphException("Node does not exist.");
        }

        // Check if the nodes in the graph are exactly equal to the inputted ones.
        if(nodes[nodeu.getName()] != nodeu || nodes[nodev.getName()] != nodev){
            throw new GraphException("Node does not exist.");
        }

        // Check to see if an edge from nodeu to nodev already exists.
        Iterator<Edge> checkEdges = incidentEdges(nodeu);
        if(checkEdges != null) {
            while (checkEdges.hasNext()) {

                // If it does, then the edge has already been entered and we throw an exception.
                if (checkEdges.next().secondEndpoint() == nodev) {
                    throw new GraphException("Edge already exists in graph.");
                }
            }
        }

        // If not, then we create and insert the appropriate edges (create two edges since the graph is unidirectional).
        Edge toInsertU = new Edge(nodeu, nodev, edgeType);
        edgeList[nodeu.getName()].add(toInsertU);
        Edge toInsertV = new Edge(nodev, nodeu, edgeType);
        edgeList[nodev.getName()].add(toInsertV);

    }

    public Node getNode(int name) throws GraphException {
        // Check if the name of the node can be in the graph.
        if (!(isNameInGraph(name))){
            throw new GraphException("The node " + name + " is not in the graph.");
        }

        // If it can be in the graph then it is since we initialize all nodes in the constructor, return the node.
        return nodes[name];
    }

    public Iterator incidentEdges(Node u) throws GraphException {
        int name = u.getName();

        // Check if the name of the node can be in the graph.
        if(!isNameInGraph(name) || nodes[name] != u){
            throw new GraphException("Node is not contained in graph.");
        }

        // Check if the nodes associated edge list is empty.
        if(edgeList[name].isEmpty()){
            return null;
        }

        // If not, return an iterator produced by this edge list.
        return edgeList[name].iterator();
    }

    public Edge getEdge(Node u, Node v) throws GraphException {

        // Check if the name of the nodes can be in the graph.
        if(!(isNameInGraph(u.getName()) || isNameInGraph(u.getName()))){
            throw new GraphException("Node does not exist.");
        }

        // Check if the nodes in the graph are exactly equal to the inputted ones.
        if(nodes[u.getName()] != u || nodes[v.getName()] != v){
            throw new GraphException("Node is not contained in graph.");
        }

        // Iterate through the edge list of u until you find an edge going from u to v, which we then return.
        Iterator<Edge> checkEdges = incidentEdges(u);
        while(checkEdges.hasNext()){
            Edge current = checkEdges.next();
            if(current.secondEndpoint() == v){
                return current;
            }
        }

        // If we iterate through the entire edge list without finding an edge connecting u and v then one
        // does not exist, therefore we throw an exception.
        throw new GraphException("Edge is not contained in graph.");
    }

    public boolean areAdjacent(Node u, Node v) throws GraphException {

        // Check if the name of the nodes can be in the graph.
        if(!(isNameInGraph(u.getName()) || isNameInGraph(u.getName()))){
            throw new GraphException("Node does not exist.");
        }

        // Check if the nodes in the graph are exactly equal to the inputted ones.
        if(nodes[u.getName()] != u || nodes[v.getName()] != v){
            throw new GraphException("Node is not contained in graph.");
        }

        // Iterate through the edge list of u until you find an edge going from u to v,
        // if such an edge exists we return true.
        Iterator<Edge> checkEdges = incidentEdges(u);
        while(checkEdges.hasNext()){
            if(checkEdges.next().secondEndpoint() == v){
                return true;
            }
        }

        // If we iterate through the entire edge list without finding an edge connecting u and v then one
        // does not exist, therefore we return false.
        return false;
    }

}
