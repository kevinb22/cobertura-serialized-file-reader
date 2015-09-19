package lineReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class holds a list of Line Objects and represents a program where each Line object corresponds to a line in the program.
 * This class can also compute the suspiciousness of lines using the Tarantula fault localization technique using
 * its compute method, read the comments in the compute method for more information.
 */

public class LineList {
    private String className;                            /** Name of the class the LineList represents. */
    private int totalFailedTests;                        /** Total failed tests produced by the test class. */
    private int totalPassedTests;                        /** Total passed tests produced by test class. */
    ArrayList<Line> theList= new ArrayList<Line>();      /** ArrayList to hold List Objects. */
    
    public LineList(){}
    
    /** Add a Line to theList. */
    public void add(Line line) {
        this.theList.add(line);
    }
    
    /** Return the size of the LineList. */ 
    public int size(){
        return theList.size();
    }
    
    /** Increase the total number of failed tests. */
    public void incrementFailedTests(){
        this.totalFailedTests++;
    }
    
    /** Add to total number of failed tests. */
    public void addFailedTests(int failed){
        this.totalFailedTests += failed;
    }
    
    /** Returns int of the total number of failed tests. */
    public int getTotalFailedTests(){
        return this.totalFailedTests;
    }
    
    /** Add to total number of passed tests. */
    public void addPassedTests(int passed){
        this.totalPassedTests += passed;
    }
    
    /** Returns an int of the total number of passed tests. */
    public int getTotalPassedTests(){
        return this.totalPassedTests;
    }
    
    /** Increment the total number of passed tests. */
    public void incrementPassedTests(){
        this.totalPassedTests++;
    }
    
    /** Calculate the suspiciousness of each statement in the list
     *  pre: assumes all the necessary information has been attained for each field
     *  (1) each Line in the code is in the theList
     *  (2) each Line has a line number, execution count, and knows the number of P/F tests it is in
     *  (3) LineList knows who many tests F/P tests there are in total */
    public void prepStatements(){
        for(int i = 0; i < theList.size(); i ++){
            double suspLevel = calculateSuspiciousness(theList.get(i));    // calculate the suspiciousness level of the line
            theList.get(i).setSuspLevel(suspLevel);                        // set the suspiciousness level of the line
        }
    }

    /** Return a double representing the suspiciousness level of the line. */
    public double calculateSuspiciousness(Line line){
        int totalFailed = getTotalFailedTests();
        int totalPassed = getTotalPassedTests();
        int failedS = line.getFailed();
        int passedS = line.getPassed();
        
        double numerator = 1.0 * failedS / totalFailed;
        double denominator = (1.0 * passedS / totalPassed) + (1.0 * failedS / totalFailed);
        
        return numerator / denominator;
    }
    
    /** Transfers the elements within theList to a Queue in order to be binary sorted
     * clears theList and returns the new Queue full of statements. */
    private Queue<Line> list2Q(){
        Queue<Line> myQ = new LinkedList<Line>();
        for(int i = 0; i < theList.size(); i++) {
            myQ.add(theList.get(i));
        }
        theList.clear();
        return myQ;
    }
    
    
    /** Transfers the elements of the Queue back into theList in sorted order. */
    private void q2List(Queue<Line> myQ) {
        while(!myQ.isEmpty()) {
            theList.add(myQ.remove());        
        }
    }
    
    /** Sorts the lines in theList where the most suspicious statement is at the 0 index of the list and descends in that order. */
    public void sort(){
        Queue<Line> myQ = list2Q();
        mergeSort(myQ);
        q2List(myQ);
        }
    
    /** Sorts the Queue recursively. */
    private void mergeSort(Queue<Line> seq){
        if(1 < seq.size()) {
            int size1 = seq.size() / 2;
            int size2 = seq.size() - size1;
            
            Queue<Line> q1 = new LinkedList<Line>();
            Queue<Line> q2 = new LinkedList<Line>();
            
            for(int i = 0; i < size1; i++) {
                q1.add(seq.remove());
            }
            
            for(int i = 0 ; i < size2; i++) {
                q2.add(seq.remove());
            }
            mergeSort(q1);
            mergeSort(q2);
            mergeSortHelper(seq, q1, q2);
        }    
    }
    
    /** Places each greater element between Queue q1, q1 into the Queue result. */
    private void mergeSortHelper(Queue<Line> result, Queue<Line> q1, Queue<Line> q2) {    
        while(!q1.isEmpty() && !q2.isEmpty()) {
            if(q1.peek().getSusp() > q2.peek().getSusp()) {
                result.add(q1.remove());
            } else {
                result.add(q2.remove());
            }
        }
        
        while(!q1.isEmpty()) {
            result.add(q1.remove());
        }
        
        while(!q2.isEmpty()) {
            result.add(q2.remove());
        }
    }
    
    /** Display the statements, though not necessarily in ranked order, only if the Line List is sorted. */
    public void display() {
        for(int i = 0; i < theList.size(); i++) {
            System.out.println(theList.get(i));
        }
    }
    
    /** Sets the classname for the LineList. */
    public void setClassName(String className){
        this.className=className;
    }
    
    /** Displays the classname for the LineList. */
    public String displayClassName(){
        return this.className;
    }
}
