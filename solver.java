import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class solver{

    public static int dpllSolver(State dpll, int verbose){
        if(dpll.getSentences().size()==0){
            for(Map.Entry<String, String> entry : dpll.getAssignment().entrySet()){
                if(entry.getValue().equals("unbound")){
                    entry.setValue("false");
                    if(verbose == 1){
                        System.out.println("unbound "+ entry.getKey()+" = "+entry.getValue());
                    }
                }
            }
            dpll.printAssignment();
            System.exit(0);
        }

        for(String[] a: dpll.getSentences()){
            if(a.length == 0){
                if(verbose == 1){
                    System.out.println("Contradiction!");
                }
                return -1;
            }
        }

        if(verbose == 1){
            dpll.printSentences();
        }

        for(int i=0; i<dpll.getSentences().size(); i++){
            String[] currString = dpll.getSentences().get(i);
            if(currString.length == 1){
                ArrayList<String[]> sentences = new ArrayList<String[]>();
                Map<String, String> assignment = new HashMap<String, String>();
                String stringWithoutSign = "";
                for (Map.Entry<String, String> entry : dpll.getAssignment().entrySet()) {
                    assignment.put(entry.getKey(),entry.getValue());
                }
                if(currString[0].charAt(0) == '!'){
                    assignment.replace(currString[0].substring(1), "false");
                    stringWithoutSign = currString[0].substring(1);
                    if(verbose == 1){
                        System.out.println("easy case: "+currString[0].substring(1)+ " = "+"false");
                    }
                }else{
                    assignment.replace(currString[0], "true");
                    stringWithoutSign = currString[0];
                    if(verbose == 1){
                        System.out.println("easy case: "+currString[0]+ " = "+"true");
                    }
                }

                for(int j=0; j<dpll.getSentences().size(); j++){
                    if(containsString(currString[0], dpll.getSentences().get(j))){  
                        continue;
                    }else if(assignment.get(stringWithoutSign).equals("true")){
                        if(containsString("!"+currString[0],dpll.getSentences().get(j))){
                            List<String> list = new ArrayList<String>(Arrays.asList(dpll.getSentences().get(j)));
                            list.remove("!"+currString[0]);
                            String[] modified = list.toArray(new String[0]);
                            sentences.add(modified);
                        }
                        else{
                            sentences.add(dpll.getSentences().get(j));
                        }
                    }else if(assignment.get(stringWithoutSign).equals("false")){
                        if(containsString(currString[0].substring(1),dpll.getSentences().get(j)) && (!containsString(currString[0], dpll.getSentences().get(j)))){
                            List<String> list = new ArrayList<String>(Arrays.asList(dpll.getSentences().get(j)));
                            list.remove(currString[0].substring(1));
                            String[] modified = list.toArray(new String[0]);
                            sentences.add(modified);
                        }
                        else{
                            sentences.add(dpll.getSentences().get(j));
                        }
                    }
                }
                State newDpll = new State(sentences, assignment);
                dpllSolver(newDpll, verbose);
                return -1;
            }
        }

        for (Map.Entry<String, String> entry : dpll.getAssignment().entrySet()) {
            if(entry.getValue().equals("unbound")){
                int isPureForTrue = 1;
                int isPureForFalse = 1;
                for(int i=0; i<dpll.getSentences().size(); i++){
                    if(containsString("!"+entry.getKey(), dpll.getSentences().get(i))){
                        isPureForTrue = 0;
                    }else if(containsString(entry.getKey(), dpll.getSentences().get(i))){
                        isPureForFalse = 0;
                    }
                }
            
                if(isPureForTrue == 1 || isPureForFalse ==1){
                    String stringWithSign = "";
                    ArrayList<String[]> sentences = new ArrayList<String[]>();
                    Map<String, String> assignment = new HashMap<String, String>();
                    if(isPureForTrue==1){
                        for (Map.Entry<String, String> entryy : dpll.getAssignment().entrySet()) {
                            assignment.put(entryy.getKey(),entryy.getValue());
                        }
                        assignment.replace(entry.getKey(), "true");
                        stringWithSign = entry.getKey();
                        if(verbose == 1){
                            System.out.println("easy case: "+entry.getKey()+ " = "+"true");
                        }
                    }else if(isPureForFalse==1){
                        for (Map.Entry<String, String> entryy : dpll.getAssignment().entrySet()) {
                            assignment.put(entryy.getKey(),entryy.getValue());
                        }
                        assignment.replace(entry.getKey(), "false");
                        stringWithSign = "!"+entry.getKey();
                        if(verbose == 1){
                            System.out.println("easy case: "+entry.getKey()+ " = "+"false");
                        }
                    }

                    for(int j = 0; j<dpll.getSentences().size(); j++){
                        if(containsString(stringWithSign, dpll.getSentences().get(j))){
                            continue;
                        }
                        else if(isPureForTrue==1){
                            if(containsString(stringWithSign,dpll.getSentences().get(j))){
                                List<String> list = new ArrayList<String>(Arrays.asList(dpll.getSentences().get(j)));
                                list.remove("!"+stringWithSign);
                                String[] modified = list.toArray(new String[0]);
                                sentences.add(modified);
                            }else{
                                sentences.add(dpll.getSentences().get(j));
                            }
                        }
                        else if(isPureForFalse==1){
                            if(containsString(stringWithSign.substring(1),dpll.getSentences().get(j)) && (!containsString(stringWithSign, dpll.getSentences().get(j)))){
                                List<String> list = new ArrayList<String>(Arrays.asList(dpll.getSentences().get(j)));
                                list.remove(stringWithSign.substring(1));
                                String[] modified = list.toArray(new String[0]);
                                sentences.add(modified);
                            }else{
                                sentences.add(dpll.getSentences().get(j));
                            }
                        }
                    }
                    State newDpll = new State(sentences, assignment);
                    dpllSolver(newDpll, verbose);
                    return -1;
                }
            }
        }

        String guessAtom = "";
        ArrayList<String[]> sentencesHard = new ArrayList<String[]>();
        Map<String, String> assignmentHard = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : dpll.getAssignment().entrySet()) {
            assignmentHard.put(entry.getKey(),entry.getValue());
        }
        
        for (Map.Entry<String, String> entry : dpll.getAssignment().entrySet()){
            if(entry.getValue().equals("unbound")){
                guessAtom = entry.getKey();
                assignmentHard.replace(guessAtom, "true");
                break;
            }
        }
        if(verbose == 1){
            System.out.println("hard case, guess: "+ guessAtom+" = "+ assignmentHard.get(guessAtom));
        }

        for(int j = 0; j<dpll.getSentences().size(); j++){
            if(containsString(guessAtom, dpll.getSentences().get(j))){
                continue;
            }
            else if(assignmentHard.get(guessAtom).equals("true")){
                if(containsString("!"+guessAtom,dpll.getSentences().get(j))){
                    List<String> list = new ArrayList<String>(Arrays.asList(dpll.getSentences().get(j)));
                    list.remove("!"+guessAtom);
                    String[] modified = list.toArray(new String[0]);
                    sentencesHard.add(modified);
                }else{
                    sentencesHard.add(dpll.getSentences().get(j));
                }
            }
        }
        State dpllGuessTrue = new State(sentencesHard, assignmentHard);
        dpllSolver(dpllGuessTrue, verbose);

        ArrayList<String[]> sentencesHardFalse = new ArrayList<String[]>();
        assignmentHard.replace(guessAtom, "false");
        if(verbose == 1){
            System.out.println("failed|hard case, guess: "+ guessAtom+" = "+ assignmentHard.get(guessAtom)); 
        }

        for(int j = 0; j<dpll.getSentences().size(); j++){
            if(containsString("!"+guessAtom, dpll.getSentences().get(j))){
                continue;
            }
            else if(assignmentHard.get(guessAtom).equals("false")){
                if(containsString(guessAtom, dpll.getSentences().get(j))  &&  (!containsString("!"+guessAtom, dpll.getSentences().get(j)))){
                    List<String> list = new ArrayList<String>(Arrays.asList(dpll.getSentences().get(j)));
                    list.remove(guessAtom);
                    String[] modified = list.toArray(new String[0]);
                    sentencesHardFalse.add(modified);
                }else{
                    sentencesHardFalse.add(dpll.getSentences().get(j));
                }
            }
        }
        State dpllGuessFalse = new State(sentencesHardFalse, assignmentHard);
        dpllSolver(dpllGuessFalse, verbose);
        return -1;
    }

    public static State BNFSolver(ParseTree tree, int verbose, int solver){
        ParseTree bnf = BNFduplicateTree(tree);
        BNFprintTree(bnf);
        System.out.println();
        
        bnf = BNFSolverStep1(bnf);
        if(verbose == 1){
            System.out.println("Step 1: eliminate all \"<=>\"");
            BNFprintTree(bnf);
            System.out.println("\n");   
        }
        
        bnf = BNFSolverStep2(bnf);
        if(verbose == 1){
            System.out.println("Step 2: eliminate all \"=>\"");
            BNFprintTree(bnf);
            System.out.println("\n");   
        }

        bnf = BNFSolverStep3(bnf);
        if(verbose == 1){
            System.out.println("Step 3: put all negation to the atom");
            BNFprintTree(bnf);
            System.out.println("\n");   
        }

        bnf = BNFSolverStep4(bnf);
        if(verbose == 1){
            System.out.println("Step 4: Replace a|(b&c) with (a|b)&(a|c)");
            BNFprintTree(bnf);
            System.out.println("\n");   
        }

        ArrayList<ArrayList<String>> cnf = BNFSolverStep5(bnf);
        if(solver == 0){
            //printout solution of cnf
            for(int i=0; i<cnf.size(); i++){
                for(int j=0; j<cnf.get(i).size(); j++){
                    System.out.print(cnf.get(i).get(j)+" ");
                }
                System.out.println();
            }
            return null;
        }

        ArrayList<String[]> sentences = new ArrayList<String[]>();
        for(ArrayList<String> a: cnf){
            String[] temp = new String[a.size()];
            for(int i=0; i<a.size(); i++){
                temp[i] = a.get(i);
            }
            sentences.add(temp);
        }

        Map<String, String> assignment = new HashMap<String, String>();
        ArrayList<String> elements = new ArrayList<String>();
        for(int i=0; i< sentences.size(); i++){
            for(int j=0; j<sentences.get(i).length; j++){
                for(int k=0; k<sentences.get(i)[j].length(); k++){
                    if(Character.isLetter(sentences.get(i)[j].charAt(k))){
                        String temp = String.valueOf(sentences.get(i)[j].charAt(k));
                        if(!elements.contains(temp)){
                            elements.add(temp);
                        }
                    }
                }
            }
        }
        for(int i=0; i<elements.size(); i++){
            assignment.put(elements.get(i), "unbound");
        }
        State dpll = new State(sentences, assignment);
        return dpll;
    }

    public static ParseTree BNFSolverStep1(ParseTree tree){
        if(tree == null){
            return null;
        }

        BNFSolverStep1(tree.left);
        if(tree.data.contains("<=>")){
            tree.data.set(tree.data.indexOf("<=>"), "&");
            ArrayList<String> newData = new ArrayList<String>();
            newData.add("=>");
            ParseTree newLeft = new ParseTree(newData, tree.left, tree.right);
            ParseTree newRight = new ParseTree(newData, tree.right, tree.left);
            tree.left = newLeft;
            tree.right = newRight;
        }
        BNFSolverStep1(tree.right);
        return tree;
    }

    public static ParseTree BNFSolverStep2(ParseTree tree){
        if(tree == null){
            return null;
        }

        BNFSolverStep2(tree.left);
        if(tree.data.contains("=>")){
            ArrayList<String> newLeftData = new ArrayList<String>();
            newLeftData.add(0, "!");
            for(String a: tree.left.data){
                newLeftData.add(a);
            }
            ParseTree newLeft = new ParseTree(newLeftData, tree.left.left, tree.left.right);
            tree.left = newLeft;
            ArrayList<String> newTreeData = new ArrayList<String>();
            for(String a: tree.data){
                if(a.equals("=>")){
                    newTreeData.add("|");
                }else{
                    newTreeData.add(a);
                }
            }
            tree.data = newTreeData;
        }
        BNFSolverStep2(tree.right);
        return tree;
    }

    public static ParseTree BNFSolverStep3(ParseTree tree){
        if(tree == null){
            return null;
        }

        BNFSolverStep3(tree.left);
        if(tree.data.contains("!") && tree.data.contains("|")){
            ArrayList<String> newLeftData = new ArrayList<String>();
            newLeftData.add("!");
            for(String a: tree.left.data){
                newLeftData.add(a);
            }
            ParseTree newLeft = new ParseTree(newLeftData, tree.left.left, tree.left.right);
            tree.left = newLeft;

            ArrayList<String> newRightData = new ArrayList<String>();
            newRightData.add("!");
            for(String a: tree.right.data){
                newRightData.add(a);
            }
            ParseTree newRight = new ParseTree(newRightData, tree.right.left, tree.right.right);
            tree.right = newRight;

            ArrayList<String> newTreeData = new ArrayList<String>();
            for(String a: tree.data){
                newTreeData.add(a);
            }
            newTreeData.remove(newTreeData.indexOf("|"));
            newTreeData.remove(newTreeData.indexOf("!"));
            newTreeData.add("&");
            tree.data = newTreeData;
        }
        
        if(tree.data.contains("!") && tree.data.contains("&")){
            ArrayList<String> newLeftData = new ArrayList<String>();
            newLeftData.add("!");
            for(String a: tree.left.data){
                newLeftData.add(a);
            }
            ParseTree newLeft = new ParseTree(newLeftData, tree.left.left, tree.left.right);
            tree.left = newLeft;

            ArrayList<String> newRightData = new ArrayList<String>();
            newRightData.add("!");
            for(String a: tree.right.data){
                newRightData.add(a);
            }
            ParseTree newRight = new ParseTree(newRightData, tree.right.left, tree.right.right);
            tree.right = newRight;

            ArrayList<String> newTreeData = new ArrayList<String>();
            for(String a: tree.data){
                newTreeData.add(a);
            }
            newTreeData.remove(newTreeData.indexOf("&"));
            newTreeData.remove(newTreeData.indexOf("!"));
            newTreeData.add("|");
            tree.data = newTreeData;
        }
        
        if(tree.data.contains("!")){
            int totalNegation = 0;
            for(String a: tree.data){
                if(a.equals("!")){
                    totalNegation++;
                }
            }
            int remainNegation = Math.floorMod(totalNegation, 2);
            ArrayList<String> newData = new ArrayList<String>();
            for(String a: tree.data){
                if(a.equals("!")){
                    if(remainNegation>0){
                        newData.add(a);
                        remainNegation--;
                    }else{
                        continue;
                    }
                }else{
                    newData.add(a);
                }
            }
            tree.data = newData;
        }

        BNFSolverStep3(tree.right);
        return tree;
    }

    public static ParseTree BNFSolverStep4(ParseTree tree){
        if(tree == null){
            return null;
        }

        BNFSolverStep4(tree.left);
        if(tree.data.contains("|")){
            if(tree.right.data.contains("&")){
                ArrayList<String> newData = new ArrayList<String>();
                newData.add("&");
                tree.data = newData;
                ArrayList<String> newLeftData = new ArrayList<String>();
                newLeftData.add("|");
                ParseTree newLeft = new ParseTree(newLeftData, tree.left, tree.right.left);
                ArrayList<String> newRightData = new ArrayList<String>();
                newRightData.add("|");
                ParseTree newRight = new ParseTree(newRightData, tree.left, tree.right.right);
                tree.left = newLeft;
                tree.right = newRight;
            }else if(tree.left.data.contains("&")){
                ArrayList<String> newData = new ArrayList<String>();
                newData.add("&");
                tree.data = newData;
                ArrayList<String> newLeftData = new ArrayList<String>();
                newLeftData.add("|");
                ParseTree newLeft = new ParseTree(newLeftData,tree.left.left, tree.right);
                ArrayList<String> newRightData = new ArrayList<String>();
                newRightData.add("|");
                ParseTree newRight = new ParseTree(newRightData, tree.left.right, tree.right);
                tree.left = newLeft;
                tree.right = newRight;
            }
        }

        BNFSolverStep4(tree.right);
        return tree;
    }

    public static ArrayList<ArrayList<String>> BNFSolverStep5(ParseTree tree){
        //collect cnf solution into Arraylist<ArrayList<String>>, does not print it out.
        //use BNFcollectLeaf function
        ArrayList<ArrayList<String>> cnf = new ArrayList<ArrayList<String>>();
        return cnf;
    }   

    public static ArrayList<String> BNFcollectLeaf(ParseTree tree){
        //collect all leaf under the same "|" tree
        ArrayList<String> collection = new ArrayList<String>();
        return collection;
    }

    public static ParseTree BNFparseTree(ArrayList<String> input){
        Map<String,Integer> value = new HashMap<String, Integer>(); 
        value.put("<=>", 4);
        value.put("=>", 3);
        value.put("|", 2);
        value.put("&", 1);
        value.put("!", 0);

        if(input.size() == 0){
            return null;
        }

        if(input.size()==1){
            return new ParseTree(input, null, null);
        }

        String opString = BNFmaxOp(input, value);
        ArrayList<String> data = new ArrayList<String>();
        data.add(opString);
        ArrayList<String> dataLeft = new ArrayList<String>();
        ArrayList<String> dataRight = new ArrayList<String>();
        int index = input.lastIndexOf(opString);
        
        for(int i=0; i<index; i++){
            dataLeft.add(input.get(i));
        }

        for(int i=index+1; i<input.size(); i++){
            dataRight.add(input.get(i));
        }

        ParseTree tree = new ParseTree(data, BNFparseTree(dataLeft), BNFparseTree(dataRight));
        return tree;
    }

    public static ParseTree BNFduplicateTree(ParseTree root){
        if (root == null)
        {
            return null;
        }
        ParseTree newNode = new ParseTree(root.data,null, null);
        newNode.left= BNFduplicateTree(root.left);
        newNode.right= BNFduplicateTree(root.right);
        return newNode;
    }

    public static int BNFcheckOrder(String op, Map<String, Integer> value){
        if(!value.containsKey(op)){
            return -1;
        }
        return value.get(op);
    }

    public static String BNFcheckValue(int order, Map<String, Integer> value){
        if(value.containsValue(order)){
            for (Map.Entry<String, Integer> target : value.entrySet()) {
                if(target.getValue() == order){
                    return target.getKey();
                } 
            }
        }
        return "Key not found";
    }

    public static String BNFmaxOp(ArrayList<String> ops, Map<String, Integer> value){
        int max = -1;
        String maxOp = "";
        for(int i=0; i<ops.size(); i++){
            int currOrder = BNFcheckOrder(ops.get(i), value);
            if(currOrder > max){
                max = currOrder;
                maxOp = BNFcheckValue(currOrder, value);
            }
        }
        return maxOp;
    }

    public static void BNFprintTree(ParseTree tree){
        if(tree == null){
            return;
        }

        BNFprintTree(tree.left);
        System.out.print(tree.data);
        BNFprintTree(tree.right);
    }

    public static ArrayList<ArrayList<String>> setUpBNF(ArrayList<String> input){
        ArrayList<ArrayList<String>> sentences = new ArrayList<ArrayList<String>> ();
        for(int i=0; i<input.size(); i++){
            String[] temp = input.get(i).split(" ");
            ArrayList<String> tempArray = new ArrayList<String>();
            for(int j=0; j<temp.length; j++){
                tempArray.add(temp[j]);
            }
            sentences.add(tempArray);
        }
        return sentences;
    }

    public static State setUpDpll(ArrayList<String> input){
        ArrayList<String> elements = new ArrayList<String>();
        for(int i = 0; i<input.size(); i++){
            for(int j=0; j<input.get(i).length(); j++){
                char currChar = input.get(i).charAt(j);
                String currString = Character.toString(currChar);
                if(!elements.contains(currString) && Character.isLetter(currChar)){
                    elements.add(currString);
                }
            }
        }
        
        Map<String, String> assignment = new HashMap<String, String>();
        for(int i=0; i<elements.size(); i++){
            assignment.put(elements.get(i), "unbound");
        }
        
        ArrayList<String[]> sentences = new ArrayList<String[]>();
        for(int i=0; i<input.size(); i++){
            String[] temp = input.get(i).split(" ");
            sentences.add(temp);
        }
        State dpll = new State(sentences, assignment);
        return dpll;
    }

    public static Boolean containsString(String s, String[] list){
        for(String a: list){
            if(a.equals(s)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> readFile(String[] args) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(args[args.length-1]));
        ArrayList<String> fileContent = new ArrayList<String>();
        try{
            String line = reader.readLine();
            while (line != null) {
                fileContent.add(line);
                line = reader.readLine();
            }
        }finally{
            reader.close();
        }
        return fileContent;
    }
   
    public static void main(String[] args) throws Exception{
        ArrayList<String> input = readFile(args);
        int verbose = 0;
        int solver = 0;
        if(args[0].equals("-v")){
            verbose = 1;
        }

        if(args[args.length-2].equals("dpll")){
            State dpll = setUpDpll(input); 
            int result = dpllSolver(dpll, verbose);
            if(result == -1){
                System.out.println("No Valid Assignment");
            }
        }
        else if(args[args.length-2].equals("cnf")){
            solver = 0;
            ArrayList<ArrayList<String>> sentences = setUpBNF(input);
            ParseTree bnf = BNFparseTree(sentences.get(0));
            BNFSolver(bnf, verbose, solver);
        }
        else if(args[args.length-2].equals("solver")){
            solver = 1;
            ArrayList<ArrayList<String>> sentences = setUpBNF(input);
            ParseTree bnf = BNFparseTree(sentences.get(0));
            State dpll = BNFSolver(bnf, verbose, solver);
            int result = dpllSolver(dpll, verbose);
            if(result == -1){
                System.out.println("No Valid Assignment");
            }
        }
        else{
           throw new Exception("Argument format error. Please check README file.");
        }
       
    }
}