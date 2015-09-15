package src.lineReader;

/**
 * This class represents a line in a program, it holds information
 * that makes it useful for the Tarantula fault localization technique
 * such as the line number and the amount of passed or failed tests it
 * has been covered/executed in
 */

public class Line {
    int lineNum;       /** Statement line number. */
    int execCount;     /** Count of times statement has been executed, accumulative throughout all tests. */
    int failedS;       /** Number of times statement was executed by passed tests. */
    int passedS;       /** Number of times statement was executed by failed tests. */
    double suspLevel;  /** Suspicious level of statement. */
	
    /** Initialize the Line with no fields. */
    public Line() {}
	
    /** Initialize the Line with a line number. */
    public Line(int lineNum) {
	setLineNum(lineNum);
    }
	
    /** Set the line number of this statement. */
    public void setLineNum(int lineNum){
	this.lineNum = lineNum;
    }
	
    /** Return the line number the Line represents. */
    public int getLineNum(){
	return this.lineNum;
    }
	
    /** Increase the execution count of this statements by one. */
    public void incrementExecCount(){
	this.execCount++;
    }
	
    /** Set the execution count of this statement to the parameter. */
    public void setExecCount(int execCount){
	this.execCount = execCount;
    }
	
    /** Increment the amount of failed tests by one. */
    public void addFailed(){
	this.failedS++;
    }
	
    /** Set the number of failed tests equal to the parameter. */
    public void setFailed(int failedS){
	this.failedS = failedS;
    }
	
    /** Return the total amount of failed tests associated with this statement. */
    public int getFailed(){
	return this.failedS;
    }
	
    /** Increment the number of passed tests by one. */
    public void addPassed(){
	this.passedS++;
    }
	
    /** Return the total amount of passed tests this statement is in. */
    public void setPassed(int passedS){
	this.passedS = passedS;
    }
	
    /** Return the total amount of passed tests associated with this statement. */
    public int getPassed(){
	return this.passedS;
    }
	
    /** Returns the suspiciousness of the statement. */
    public double getSusp(){
	return this.suspLevel;
    }
	
    /** Set the suspiciousness value for this statement. */
    public void setSuspLevel(double level){
	this.suspLevel = level;
    }
	
    /** Return a string in format line#, passed#, failed#, suspicion-level. */
    public String toString(){
	return "line #: " + this.lineNum + ", passed tests: " + this.passedS + " , failed tests: " + this.failedS + ", suspicion-level: " + this.suspLevel;
    }
}
