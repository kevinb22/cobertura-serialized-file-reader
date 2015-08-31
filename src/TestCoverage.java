package coberturaTarantula;

import java.util.TreeMap;

public class TestCoverage {
	private LineList stmts;
	private boolean passed;
	private String testName;
	
	public TestCoverage(){}
	
	public TestCoverage(LineList stmt){
		this("", stmt, true);
	}
	
	public TestCoverage(String testName, LineList stmt, boolean passed){
		assignStmts(stmt);
		assignResult(passed);
	}
	
	/** assign a name to the Test **/
	public void assignName(String name){
		this.testName = name;
	}
	
	/** assign a StmtList to be associated with this test **/
	public void assignStmts(LineList stmts){
		this.stmts = stmts;
	}
	
	/** makes sure all Line in Line list know the result of the test **/
	public void assignPFtoStmt(TreeMap<Integer, Line> class_stmt_map){
		for(Integer line_num : class_stmt_map.keySet()) {
			Line line = class_stmt_map.get(line_num);	// get each Stmt for a line number,
			if(passed) 									// add a passed or failed count per line
				line.addPassed();
			else 
				line.addFailed();
			
			stmts.add(line);							// add the line to the StmtList
		}
	}
	
	/** return the StmtList associated with this test **/
	public LineList getStmts(){
		return this.stmts;
	}
	
	/** assign a boolean representing the pass/fail value of the test **/
	public void assignResult(boolean passed){
		this.passed = passed;
	}
	
	public String toString(){
        return "Test Name: " + this.testName + ", Test Passed: " + this.passed + ", lines coverered: " + this.stmts;
    }
	
	
	
}
