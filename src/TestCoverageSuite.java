package coberturaTarantula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.cobertura.coveragedata.ClassData;

public class TestCoverageSuite {
	
	/** ArrayList to hold all the tests in a TestSuite **/
	private ArrayList<TestCoverage> allTests;
	
	/** String representing the package that that the TestSuite is collecting information about **/
	private String testSuiteName;
	
	/** ArrayList holds a collection of all the Line for the entire test suite
	 olds various information such as line number, execution count, ect. **/
	LineList allLines;
	
	public TestCoverageSuite(){
		this.allTests = new ArrayList<TestCoverage>();
		this.allLines = new LineList();
	}
	
	/** Assign the name of the TestSuite object to the parameter **/
	public void assignName(String name){
		this.testSuiteName = name;
	}
	
	/** Add a test to the Test Suite **/
	public void addTest(TestCoverage test){
		this.allTests.add(test);
	}
	
	/** loop through all the test cases to get the accumulative passed and failed test count and 
	 * assign it to a new Line in allLines
	 */
	public void sumAllLines(){
		
		Map<Integer ,int[]> temp_map = new TreeMap<>(); 
		for(TestCoverage t : allTests) {
			LineList temp_list = t.getStmts();
			for(int i = 0; i < temp_list.size(); i++) {
				Line s = temp_list.the_list.get(i);
				if(!temp_map.containsKey(s.line_num)) {
					temp_map.put(s.line_num, new int[2]);
					temp_map.get(s.line_num)[0] = s.getPassed();
					allLines.addPassedTests(s.getPassed());
					temp_map.get(s.line_num)[1] = s.getFailed();
					allLines.addFailedTests(s.getFailed());
				} else {
					temp_map.put(s.line_num, temp_map.get(s.line_num));
					temp_map.get(s.line_num)[0] += s.getPassed();
					allLines.addPassedTests(s.getPassed());
					temp_map.get(s.line_num)[1] += s.getFailed();
					allLines.addFailedTests(s.getFailed());
				}
			}
		}
		
		for(Integer i : temp_map.keySet()) {
			Line s = new Line(i);
			s.setPassed(temp_map.get(i)[0]);
			s.setFailed(temp_map.get(i)[1]);
			allLines.add(s);
		}
	}
	
    /** Displays all the tests currently in test suite**/
    public void displayTests(){
        for(TestCoverage t : allTests){
            System.out.println(t);
        }
    }
}
