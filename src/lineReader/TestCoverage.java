package lineReader;

import java.util.TreeMap;

/**
 * This class represents the coverage of a test on a program and the results of the test. 
 * The class holds the name of the test, the lines(of the porgram ) 
 * covered/executed by the test (represented by a LineList) and the pass/fail result of the test.
 */

public class TestCoverage {
    private LineList stmts;    /** LineList that holds all the lines executed/covered by the test. */
    private boolean passed;    /** Result of the test either true(passed) or false(failed). */
    private String testName;   /** Name of the test (the test header). */
    
    public TestCoverage(){}
    
    /** Constructor, initializes a TestCoverage object with the testName, the Lines covered
     *  by the test, and the result of the test. */
    public TestCoverage(String testName, LineList stmt, boolean passed){
        assignStmts(stmt);
        assignResult(passed);
    }
    
    /** Assign a name to the Test. */
    public void assignName(String name){
        this.testName = name;
    }
    
    /** Assign a LineList to be associated with this test. */
    public void assignStmts(LineList stmts){
        this.stmts = stmts;
    }
    
    /** Makes sure all Lines in Line list know the result of the test. */
    public void assignPFtoStmt(TreeMap<Integer, Line> classStmtMap){
        for(Integer lineNum : classStmtMap.keySet()) {
            Line line = classStmtMap.get(lineNum);  // get each Stmt for a line number,
            if(passed)                              // add a passed or failed count per line
                line.addPassed();
            else 
                line.addFailed();
            stmts.add(line);                        // add the line to the StmtList
        }
    }
    
    /** Return the LineList associated with this test. */
    public LineList getStmts(){
        return this.stmts;
    }
    
    /** Assign a boolean representing the pass/fail value of the test. */
    public void assignResult(boolean passed){
        this.passed = passed;
    }
    
    /** Returns a String representing the coverage of this test. */
    public String toString(){
        return "Test Name: " + this.testName + ", Test Passed: " + this.passed + ", lines coverered: " + this.stmts;
    }
}
