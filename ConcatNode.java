public class ConcatNode extends Node{

    protected Node firstChild;
    protected Node secondChild;

    public ConcatNode(NodeName name,Node first, Node second) {
        super(name);
        this.firstChild = first;
        this.secondChild = second;
    }
}
