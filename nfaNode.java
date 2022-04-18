import java.util.ArrayList;
import java.util.Objects;

public class nfaNode {

    protected int index ;
    protected ArrayList<transition> edges;

    public nfaNode(int index){
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        nfaNode nfaNode = (nfaNode) o;
        return index == nfaNode.index;
    }


    public void AddTransition(transition e){
        this.edges.add(e);
    }

}
