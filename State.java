import java.util.ArrayList;
import java.util.Map;

public class State{
    
    ArrayList<String[]> sentences;
    Map<String, String> assignment;

    public State(ArrayList<String[]> sentences, Map<String,String> assignment){
        this.sentences = sentences;
        this.assignment = assignment;
    }  

    public ArrayList<String[]> getSentences(){
        return this.sentences;
    }

    public Map<String, String> getAssignment(){
        return this.assignment;
    }

    public void printSentences(){
        for(String[] a: sentences){
            for(String b: a){
                System.out.print(b+" ");
            }
            System.out.println();
        }
    }

    public void printAssignment(){
        for(Map.Entry<String, String> entry : this.getAssignment().entrySet()){
            System.out.println(entry.getKey() + " = "+entry.getValue());
        }
    }

    public String toString(){
        return this.sentences + "\n" + this.assignment;
    }
}
