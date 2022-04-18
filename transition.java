import java.util.Objects;

public class transition {
    protected nfaNode from;
    protected nfaNode to;
    protected char transitionSymbol;

    public transition(nfaNode from, nfaNode to, char transitionSymbol){
        this.from = from;
        this.to = to;
        this.transitionSymbol = transitionSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        transition that = (transition) o;
        return transitionSymbol == that.transitionSymbol && from.index ==that.from.index && to.index == that.to.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, transitionSymbol);
    }

    public void print(){
        System.out.print(from.index);
        System.out.print("->");
        System.out.print(transitionSymbol);
        System.out.print("->");
        System.out.println(to.index);
    }

}
