import java.util.Stack;

public class Build {

    public Node buildTree(String regex){
        if(regex.length() == 1){
            return new SymbolNode(NodeName.SYMBOL,regex.charAt(0));
        }
        if(regex.length() == 2){
            return lengthOfTwo(regex);
        }
        if (regex.charAt(0) == '('){
            int ind = getInBraces(regex);
            return checkConditions(ind,regex);
        }
        if(isLanguageSymbol(regex.charAt(0))){
            return charFirst(regex);
        }
        throw new RuntimeException("Invalid character encountered");
    }

    public void printTree(Node root){
        if(NodeName.SYMBOL == root.name){
            SymbolNode temp = (SymbolNode) root;
            System.out.println(temp.symbol);
        }else{
            System.out.println(root.name);
        }
        if(NodeName.STAR == root.name){
            StarNode temp = (StarNode) root;
            printTree(temp.child);
        }else if(NodeName.CONCAT == root.name){
            ConcatNode temp = (ConcatNode) root;
            printTree(temp.firstChild);
            printTree(temp.secondChild);
        }else if(NodeName.UNION == root.name){
            UnionNode temp = (UnionNode) root;
            printTree(temp.firstChild);
            printTree(temp.secondChild);
        }
    }

    private Node charFirst(String regex) {
        int ind = 1;
        char first = regex.charAt(0);
        Node result = null;
        Node firstNode = new SymbolNode(NodeName.SYMBOL,first);
        if(regex.charAt(ind) == '*'){
            while(regex.charAt(ind) == '*') ind++;
            result = new StarNode(NodeName.STAR,firstNode);
        }else{
            result = firstNode;
        }
        if(isLanguageSymbol(regex.charAt(ind)) || regex.charAt(ind) == '(' ){
            Node secondNode = this.buildTree(regex.substring(ind));
            return new ConcatNode(NodeName.CONCAT,result,secondNode);
        }
        if(regex.charAt(ind) == '|'){
            ind++;
            Node secondNode = this.buildTree(regex.substring(ind));
            return new UnionNode(NodeName.UNION,result,secondNode);
        }
        throw  new RuntimeException("Invalid character encountered");
    }

    private Node checkConditions(int ind, String regex) {
        int len = regex.length();
        if(len == ind){
            return this.buildTree(regex.substring(1,len-1));
        }
        Node res ;
        if(regex.charAt(ind) == '*'){
            Node tempRes = this.buildTree(regex.substring(1,ind-1));
            res = new StarNode(NodeName.STAR,tempRes);
            if(ind == regex.length() -1) return res;
            ind++;
            while(ind < regex.length() && regex.charAt(ind) == '*')ind++;
        }else{
            res = this.buildTree(regex.substring(1,ind-1));
        }
        if(isLanguageSymbol(regex.charAt(ind)) || regex.charAt(ind) == '('){
            Node second = this.buildTree(regex.substring(ind));
            return new ConcatNode(NodeName.CONCAT,res,second);
        }
        if(regex.charAt(ind) == '|'){
            ind++;
            Node second = this.buildTree(regex.substring(ind,len));
            return new UnionNode(NodeName.UNION,res,second);
        }
        throw new RuntimeException("Invalid character encountered");
    }

    private int getInBraces(String regex) {
        Stack<Character> st = new Stack<Character>();
        st.push('(');
        int ind = 1;
        while(!st.empty()){
            char curr = regex.charAt(ind);
            if(curr == '(' ) st.push('(');
            if(curr == ')' ) st.pop();
            ind++;
            if(ind == regex.length() && !st.empty()){
                throw new RuntimeException("Invalid parenthesis");
            }
        }
        return ind;
    }

    private Node lengthOfTwo(String regex) {
        char firstChar = regex.charAt(0);
        char secondChar = regex.charAt(1);
        if(firstChar == '(' && secondChar == ')'){
            return new SymbolNode(NodeName.SYMBOL,'$');
        }
        if(isLanguageSymbol(firstChar) && isLanguageSymbol(secondChar)){
            return new ConcatNode(NodeName.CONCAT,buildTree(""+firstChar),buildTree(""+secondChar));
        }
        if(isLanguageSymbol(firstChar) && secondChar == '*'){
            return new StarNode(NodeName.STAR,buildTree(""+firstChar));
        }
        throw  new RuntimeException("Invalid symbols");
    }

    private boolean isLanguageSymbol(char c){
        if(c >= 'a' && c <= 'z' ) return true;
        if(c >= '0' && c <= '9' ) return true;
        return false;
    }
}
