

public class Edge {
    private Node start;
    private Node end;

    // 0 = free road, 1 = toll road, -1 = compensation road
    private int type;
    private String label;

    public Edge (Node u, Node v, int type){
        label = "";
        start = u;
        end = v;
        this.type = type;
    }

2    public Node firstEndpoint(){
        return start;
    }

    public Node secondEndpoint(){
        return end;
    }

    public int getType(){
        return type;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
