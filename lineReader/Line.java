package lineReader;

/**
 * This class represents a line in a program, it holds information
 * that makes it useful for the Tarantula fault localization technique
 * such as the line number and the amount of passed or failed tests it
 * has been covered/executed in
 * */

public class Line {
    int line_num; 	    /** statement line number **/
    int exec_count;     /** count of times statement has been executed, accumulative throughout all tests **/
    int failed_s;       /** number of times statement was executed by passed tests **/
    int passed_s;       /** number of times statement was executed by failed tests **/
    double susp_level;  /** suspicious level of statement **/
	
    /** initialize the Line with no fields **/
    public Line() {}
	
    /** initialize the Line with a line number **/
    public Line(int line_num) {
	set_line_num(line_num);
    }
	
    /** set the line number of this statement **/
    public void set_line_num(int line_num){
	this.line_num = line_num;
    }
	
    /** return the line number the Line represents **/
    public int getLineNum(){
	return this.line_num;
    }
	
    /** increase the execution count of this statements by one **/
    public void increment_exec_count(){
	this.exec_count++;
    }
	
    /** set the execution count of this statement to the parameter **/
    public void set_exec_count(int exec_count){
	this.exec_count = exec_count;
    }
	
    /** increment the amount of Failed tests by one **/
    public void addFailed(){
	this.failed_s++;
    }
	
    /** set the number of Failed Tests equal to the parameter **/
    public void setFailed(int failed_s){
	this.failed_s = failed_s;
    }
	
    /** return the total amount of failed tests associated with this statement **/
    public int getFailed(){
	return this.failed_s;
    }
	
    /** increment the number of Passed tests by one **/
    public void addPassed(){
	this.passed_s++;
    }
	
    /** return the total amount of passed tests this statement is in **/
    public void setPassed(int passed_s){
	this.passed_s = passed_s;
    }
	
    /** return the total amount of passed tests associated with this statement **/
    public int getPassed(){
	return this.passed_s;
    }
	
    /** returns the suspiciousness of the statement **/
    public double get_susp(){
	return this.susp_level;
    }
	
    /** set the suspiciousness value for this statement **/
    public void set_susp_level(double level){
	this.susp_level = level;
    }
	
    /** return a string in format line#, passed#, failed#, suspicion-level **/
    public String toString(){
	return "line #: " + this.line_num + ", passed tests: " + this.passed_s + " , failed tests: " + this.failed_s + ", suspicion-level: " + this.susp_level;
    }
}
