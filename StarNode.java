public class StarNode extends Node{

    protected Node child;

    public StarNode(NodeName name, Node child){
        super(name);
        this.child = child;
    }
}
