public class UnionNode extends Node{

    protected Node firstChild;
    protected Node secondChild;

    public UnionNode(NodeName name, Node first, Node second){
        super(name);
        this.firstChild = first;
        this.secondChild = second;
    }

}
