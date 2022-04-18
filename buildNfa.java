import groovyjarjarantlr4.v4.misc.OrderedHashMap;

import java.util.*;



public class buildNfa {

    private int counter;
    private int nodeCounter;
    private Queue<transition> q;
    private Map<Integer, Set<transition> > m;

    public buildNfa(){
        q = new LinkedList<transition>();
        m = new HashMap<Integer, Set<transition> >();
        this.nodeCounter = 0;
    }

    public void printAfterCLear(NFA node){
        System.out.println("Start state : ");
        System.out.println(node.start.index);
        System.out.println("Accept states : ");
        for(nfaNode n : node.acceptStates){
            System.out.print(n.index + " ; ");
        }
        System.out.println("");
        System.out.println("Transitions : ");
        for(int x : m.keySet()){
            for(transition t : m.get(x)){
                t.print();
            }
        }
    }

    public void print(NFA node){
        int n = this.m.keySet().size();
        int a = node.acceptStates.size();
        int t = countTransitions();
        System.out.println(n + " " + a + " " + t);
        printAcceptStates(node);
        System.out.println("");
        printTransitions(node);
    }

    private void printAcceptStates(NFA node) {
        ArrayList<Integer> arrr = new ArrayList<>();
        for(nfaNode x : node.acceptStates){
            arrr.add(x.index);
        }
        Collections.sort(arrr);
        for(int temp : arrr){
            System.out.print(temp + " ");
        }
    }

    private void printTransitions(NFA node) {
        for(int elem = 0; elem < counter; elem ++){
            if(m.get(elem) == null){
                System.out.println(0);
                continue;
            }
            System.out.print(m.get(elem).size()+ " ");
            for(transition t : m.get(elem)){
                System.out.print(t.transitionSymbol + " " + t.to.index + " ");
            }
            System.out.println("");
        }
    }

    private int countTransitions() {
        int res = 0;
        for(int x : this.m.keySet()){
            res+=this.m.get(x).size();
        }
        return res;
    }

    private void makeThingsInShape(NFA node){
        Map<Integer,Set<transition> > result = new HashMap<>();
        Queue<nfaNode> q = new LinkedList<nfaNode>();
        Set<nfaNode> s = new HashSet<>();
        counter =0;
        q.add(node.start);
        s.add(node.start);
        while(!q.isEmpty()){
            nfaNode temp = q.peek();
            q.remove();
            if(m.get(temp.index) != null) {
                for (transition t : m.get(temp.index)) {
                    if (!s.contains(t.to)) {
                        s.add(t.to);
                        q.add(t.to);
                    }
                }
            }
            Set<transition> tempRe = m.get(temp.index);
            result.put(counter,tempRe);
            temp.index = counter;
            counter++;
        }
        this.m = result;
    }

    public void clearNfa(NFA node){
        for(transition t : node.transitions){
            if(t.transitionSymbol == '$'){
                q.add(t);
            }
        }
        storeTransitions(node);
        removeTransitions(node);
        removeExtraStates(node);
        makeThingsInShape(node);
    }
    private void removeExtraStates(NFA node){
        ArrayList<Integer> a = new ArrayList<>();
        for(int x : m.keySet()){
            Boolean flag = false;
            for(int elem : m.keySet()){
                if(elem == x) continue;
                for(transition t: m.get(elem)){
                    if(t.to.index == x){
                        flag = true;
                        break;
                    }
                }
            }
            if(!flag && node.start.index != x){
                a.add(x);
            }
        }
        for(int elem : a){
            m.remove(elem);
            node.removeAcceptState(elem);
        }
        ArrayList<nfaNode > aa = new ArrayList<>();
        for(nfaNode temp : node.acceptStates){
            if(checkFor(node,temp)){
                aa.add(temp);
                continue;
            }
            if(m.get(temp.index) == null ){
                m.put(temp.index,new HashSet<>());
            }
        }
        for(nfaNode temp : aa){
            node.acceptStates.remove(temp);
        }
    }

    private void storeTransitions(NFA node) {
        for(transition t : node.transitions){
            if(m.containsKey(t.from.index)){
                m.get(t.from.index).add(t);
            }else{
                Set<transition> temp = new HashSet<transition>();
                temp.add(t);
                m.put(t.from.index,temp);
            }
        }
    }


    private void removeTransitions(NFA node) {
        while(!q.isEmpty()){
            transition t = q.peek();
            q.remove();
            m.get(t.from.index).remove(t);
            if(t.from == t.to) continue;
            if(node.acceptStates.contains(t.to)){
                node.addAcceptState(t.from);
            }
            Set<transition> ar = getNeigs(t.to);
            if(ar == null) {
                continue;
            }
            for(transition elem : ar){
                if(elem.to == t.to && elem.transitionSymbol == '$') continue;
                transition newT = new transition(t.from,elem.to,elem.transitionSymbol);
                m.get(t.from.index).add(newT);
                if(elem.transitionSymbol == '$'){
                    q.add(newT);
                }
            }
        }
    }

    private boolean checkFor(NFA node, nfaNode to) {
        if(m.containsKey(to.index)) return false;
        for(int x : m.keySet()) {
            for(transition t: m.get(x)){
                if(t.to.index == to.index){
                    return false;
                }
            }
        }
        return true;
    }

    private Set<transition> getNeigs(nfaNode to) {
        return m.get(to.index);
    }


    public NFA build(Node root){
        if(root.name == NodeName.SYMBOL){
            SymbolNode temp = (SymbolNode) root;
            return makeNfaWithOneNode(temp.symbol);
        }
        if(root.name == NodeName.STAR){
            StarNode temp = (StarNode) root;
            NFA starResult = build(temp.child);
            //todo
            return starNfa(starResult);
        }
        if(root.name == NodeName.UNION){
            UnionNode temp = (UnionNode) root;
            NFA firstResult = build(temp.firstChild);
            NFA secondResult = build(temp.secondChild);
            //todo
            return unionNfa(firstResult,secondResult);
        }
        if(root.name == NodeName.CONCAT){
            ConcatNode temp = (ConcatNode) root;
            NFA firstRes = build(temp.firstChild);
            NFA secondRes = build(temp.secondChild);
            //todo
            return concatNfa(firstRes,secondRes);
        }
        return null;
    }

//    public void printNfa(NFA node){
//        System.out.println("Start state : ");
//        System.out.println(node.start.index);
//        System.out.println("Accept states : ");
//        for(nfaNode n : node.acceptStates){
//            System.out.print(n.index + " ; ");
//        }
//        System.out.println("");
//        System.out.println("Transitions : ");
//        for (transition e : node.transitions){
//            e.print();
//        }
//    }

    private NFA concatNfa(NFA first , NFA second ){
        NFA result  = new NFA(first.start);
        for(transition e : first.transitions){
            result.addTransition(e);
        }
        for(transition e : second.transitions){
            result.addTransition(e);
        }
        for(nfaNode temp : first.acceptStates){
            result.addTransition(new transition(temp,second.start,'$'));
        }
        for(nfaNode temp : second.acceptStates){
            result.addAcceptState(temp);
        }
        return result;
    }


    private NFA unionNfa(NFA first, NFA second){
        nfaNode newStart = new nfaNode(nodeCounter);
        nodeCounter++;
        NFA result = new NFA(newStart);
        result.addTransition(new transition(newStart,first.start,'$'));
        result.addTransition(new transition(newStart,second.start,'$'));
        for(transition e: first.transitions){
            result.addTransition(e);
        }
        for(transition e: second.transitions){
            result.addTransition(e);
        }
        for(nfaNode temp : first.acceptStates){
            result.addAcceptState(temp);
        }
        for(nfaNode temp : second.acceptStates){
            result.addAcceptState(temp);
        }
        return result;
    }

    private NFA starNfa(NFA toBeStared){
        Set<nfaNode> accepts = toBeStared.acceptStates;
        nfaNode news = new nfaNode(nodeCounter);
        nodeCounter++;
        nfaNode prev = toBeStared.start;
        toBeStared.start = news;
        toBeStared.addTransition(new transition(toBeStared.start,prev,'$'));
        for (nfaNode temp : accepts) {
            toBeStared.addTransition(new transition(temp,toBeStared.start,'$'));
        }
        toBeStared.addAcceptState(toBeStared.start);
        return toBeStared;
    }


    private NFA makeNfaWithOneNode(char c){
        nfaNode first = new nfaNode(nodeCounter);
        nodeCounter++;
        nfaNode second = new nfaNode(nodeCounter);
        nodeCounter++;
        transition e = new transition(first,second,c);
        NFA res = new NFA(first);
        res.addTransition(e);
        res.addAcceptState(second);
        return  res;
    }
}
