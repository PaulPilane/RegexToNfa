import groovyjarjarantlr4.v4.runtime.misc.OrderedHashSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NFA {

    protected nfaNode start;
    protected ArrayList<transition> transitions;
    protected HashSet<nfaNode> acceptStates;

    public NFA(nfaNode start){
        this.start = start;
        this.transitions = new ArrayList<transition>();
        this.acceptStates = new HashSet<>();
    }

    public void addTransition(transition e){
        this.transitions.add(e);
    }

    public void addAcceptState(nfaNode e){
        this.acceptStates.add(e);
    }

    public void removeAcceptState(int elem){
        for(nfaNode n: acceptStates){
            if(n.index == elem){
                acceptStates.remove(n);
                break;
            }
        }
    }
}
