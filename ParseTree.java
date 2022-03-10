import java.util.ArrayList;

public class ParseTree {
    
    ArrayList<String> data = new ArrayList<String>(); 
    ParseTree left;
    ParseTree right;

    public ParseTree(ArrayList<String> data, ParseTree left, ParseTree right){
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public ParseTree(ArrayList<String> data){
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void printTree(){
        for(int i=0; i<this.data.size(); i++){
            System.out.print(data.get(i)+" ");
        }
        System.out.println();
    }
}
