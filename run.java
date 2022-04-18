import java.util.*;

public class run {
    private static int n;
    private static int a;
    private static int t;
    private static String input;
    private static ArrayList < Map<Character, ArrayList<Integer>> > m;
    private static Set<Integer> accepts;

    public static void main(String[] args){
        readAndStore();
        runProgram();
    }

    private static void runProgram() {
        String res = "";
        Set<Integer> current = new HashSet<>();
        current.add(0);
        for(int i = 0; i < input.length(); i++ ){
            char currentChar = input.charAt(i);
            Set<Integer> ifWere = new HashSet<>();
            for(int tempX : current){
                ArrayList<Integer> toBeAdded = m.get(tempX).get(currentChar);
                if(toBeAdded == null) continue;
                for(int x : toBeAdded ){
                    ifWere.add(x);
                }
            }
            Boolean flag = true;
            for(int a : accepts){
                if(ifWere.contains(a)){
                    res+='Y';
                    flag = false;
                    break;
                }
            }
            if(flag){
                res+='N';
            }
            current = ifWere;
        }
        System.out.println(res);
    }

    private static void readAndStore(){
        Scanner sc = new Scanner(System.in);
        input = sc.next();
        n = sc.nextInt();
        a = sc.nextInt();
        t = sc.nextInt();
        m = new ArrayList<>();
        accepts = new HashSet<>();
        for(int i = 0; i < a; i++ ){
            accepts.add(sc.nextInt());
        }
        for(int i = 0 ; i < n; i++ ){
            int tempX = sc.nextInt();
            Map<Character, ArrayList<Integer>> tempM= new HashMap<>();
            for(int j = 0 ; j < tempX; j++ ){
                String  transition;
                transition = sc.next();
                int to = sc.nextInt();
                char tSymbol = transition.charAt(0);
                if(tempM.containsKey(tSymbol)){
                    tempM.get(tSymbol).add(to);
                }else{
                    ArrayList<Integer> newly= new ArrayList<>();
                    newly.add(to);
                    tempM.put(tSymbol,newly);
                }
            }
            m.add(tempM);
        }
    }
}
