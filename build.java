import java.util.*;

public class build {


    public static void main(String[] args) {
        runPogram();
    }

    private static void runPogram(){
        while(true) {
            String regex;
            System.out.print("Enter a string : ");
            Scanner sc = new Scanner(System.in);
            regex = sc.next();
            if(regex.equals("STOP")) break;
            toWork(regex);
        }
    }

    private static void toWork(String regex) {
        Build builder = new Build();
        Node root = builder.buildTree(insertBraces(regex));
        buildNfa nfa = new buildNfa();
        NFA result = nfa.build(root);
        nfa.clearNfa(result);
        nfa.print(result);
    }

    private static String insertBraces(String regex) {
        for(int i = 0; i < regex.length(); i++ ){
            if(regex.charAt(i) == '|' ){
                int left = getFromLeft(regex,i);
                if(left == 0){
                    regex = '(' + regex.substring(0,i) + ')' + regex.substring(i);
                }else{
                    regex = regex.substring(0,left + 1) + '(' + regex.substring(left+1,i) + ')' + regex.substring(i);
                }
                i+=2;
                int right = getFromRight(regex,i);
                if(right == regex.length() - 1){
                    regex =regex.substring(0,i+1)+'('+ regex.substring(i+1)+ ')';
                }else{
                    regex = regex.substring(0,i+1)+'(' + regex.substring(i+1,right) + ')' + regex.substring(right);
                }
            }
        }
        return regex;
    }

    private static int getFromRight(String regex,int i) {
        Stack<Character> s = new Stack<>();
        for(int j = i; j < regex.length() ; j++ ){
            if(regex.charAt(j) == ')' && s.empty()) return j;
            if(regex.charAt(j) == '(') s.push('(');
            if(regex.charAt(j) == ')')  s.pop();
        }
        return regex.length() - 1;
    }

    private static int getFromLeft(String regex, int i) {
        Stack<Character> s = new Stack<>();
        for(int j = i; j >= 0; j-- ){
            if(regex.charAt(j) == '(' && s.empty()) return j;
            if(regex.charAt(j) == ')') s.push(')');
            if(regex.charAt(j) == '(')  s.pop();
        }
        return 0;
    }

}
