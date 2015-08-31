package coberturaTarantula;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LineList {
	private String className;
	private int totalFailedTests;
	private int totalPassedTests;
	ArrayList<Line> the_list= new ArrayList<Line>();
	
	public LineList(){
		
	}
	
	/** add a Line to the_list **/
	public void add(Line line) {
		this.the_list.add(line);
	}
	
	/** return the size of the StmtList **/ 
	public int size(){
		return the_list.size();
	}
	
	/** increase the total number of failed tests **/
	public void incrementFailedTests(){
		this.totalFailedTests++;
	}
	
	/** add to total number of failed tests **/
	public void addFailedTests(int failed){
		this.totalFailedTests += failed;
	}
	
	/** public int get the total number of failed tests **/
	public int getTotalFailedTests(){
		return this.totalFailedTests;
	}
	
	/** add to total number of passed tests **/
	public void addPassedTests(int passed){
		this.totalPassedTests += passed;
	}
	
	/** public int get the total number of passed tests **/
	public int getTotalPassedTests(){
		return this.totalPassedTests;
	}
	
	/** increment the total number of passed tests **/
	public void incrementPassedTests(){
		this.totalPassedTests++;
	}
	
	/* pre: assumes all the necessary information has been attained for each field
	* (1) each Stmt in the code is in the the_list
	* (2) each Stmt has a line number, execution count, and knows the number of P/F tests it is in
	* (3) StmtList knows who many tests F/P tests there are in total
	* */
	/** calculate the suspiciouessness of each statement in the list **/
	public void prepStatements(){
		for(int i = 0; i < the_list.size(); i ++){
			double susp_level = calculateSuspiciousness(the_list.get(i));	// calculate the suspiciousness level of the line
			the_list.get(i).set_susp_level(susp_level);						// set the suspiciousness level of the line
		}
	}

	/** return a double representing the suspiciousness level of the line **/
	public double calculateSuspiciousness(Line line){
		int totalFailed = getTotalFailedTests();
		int totalPassed = getTotalPassedTests();
		int failed_s = line.getFailed();
		int passed_s = line.getPassed();
		
		double numerator = 1.0 * failed_s / totalFailed;
		double denominator = (1.0 * passed_s / totalPassed) + (1.0 * failed_s / totalFailed);
		
		return numerator / denominator;
	}
	
	// transfers the elements within the_list to a Queue in order to be binary sorted
	// clears the_list and returns the new Queue full of statements
	private Queue<Line> list2Q(){
		Queue<Line> my_q = new LinkedList<Line>();
		for(int i = 0; i < the_list.size(); i++) {
			my_q.add(the_list.get(i));
		}
		the_list.clear();
		return my_q;
	}
	
	
	// transfers the elements of the Queue back into the_list in sorted order
	private void q2List(Queue<Line> my_q) {
 	while(!my_q.isEmpty()) {
		the_list.add(my_q.remove());		
 		}
	}
	
	/** sorts the Stmts in the_list where the most suspicious statement is at the 0 index of the list and descends in that order **/
	public void Sort(){
		Queue<Line> my_q = list2Q();
		mergeSort(my_q);
		q2List(my_q);
	}
	
	// sorts the Queue recursively
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
	
	// places each greater element between Queue q1, q1 into the Queue result
	private void mergeSortHelper(Queue<Line> result, Queue<Line> q1, Queue<Line> q2) {
		
		while(!q1.isEmpty() && !q2.isEmpty()) {
			if(q1.peek().get_susp() > q2.peek().get_susp()) {
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
	
	/** display the statements, though not necessarily in ranked order, only if the Stmt list is sorted **/
	public void display() throws InterruptedException {
		//System.out.println("Line in " + displayClassName());
		for(int i = 0; i < the_list.size(); i++) {
			System.out.println(the_list.get(i));
		}
	}
	
	/** Sets the classname for the StmtList **/
	public void setClassName(String className){
		this.className=className;
	}
	
	/** Displays the classname for the StmtList **/
	public String displayClassName(){
		return this.className;
	}
	

}
