package lineReader;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class holds a list of TestCoverage objects and represents the code of a program covered by all the tests 
 * in a test suite and the results of each test. 
 * The class holds the name of all the tests executed on a program, the lines cumulatively covered 
 * by all the tests(represented by a LineList), and the pass/fail results of all the tests on the program.
 *
 * In order to determine the accumulative line coverage from all the tests in the test suite the sumAllLines() method
 * should be called which will conbine all the information from the LineLists of each TestCoverage object. 
 */

public class TestCoverageSuite {
    /** ArrayList to hold all the tests in a TestSuite. */
    private ArrayList<TestCoverage> allTests;
    
    /** String representing the package that that the TestSuite is collecting information about. */
    private String testSuiteName;
    
    /** ArrayList holds a collection of all the Line for the entire test suite
     *holds various information such as line number, execution count, ect. */
    LineList allLines;

    /** Constructor, initializes with a ArrayList for TestCoverages and LineList object. */    
    public TestCoverageSuite(){
        this.allTests = new ArrayList<TestCoverage>();
        this.allLines = new LineList();
    }
    
    /** Assigns the name of the TestSuite object to the parameter. */
    public void assignName(String name){
        this.testSuiteName = name;
    }
    
    /** Adds a test to the Test Suite. */
    public void addTest(TestCoverage test){
        this.allTests.add(test);
    }
    
    /** Loops through all the test cases to get the accumulative passed and failed test count and 
     * assign it to a new Line in allLines. */
    public void sumAllLines(){
        Map<Integer, int[]> tempMap = new TreeMap<>(); 

        for(TestCoverage t : allTests) {
            LineList tempList = t.getStmts();
            for(int i = 0; i < tempList.size(); i++) {
                Line s = tempList.theList.get(i);
                if(!tempMap.containsKey(s.lineNum)) {
                    tempMap.put(s.lineNum, new int[2]);
                    tempMap.get(s.lineNum)[0] = s.getPassed();
                    allLines.addPassedTests(s.getPassed());
                    tempMap.get(s.lineNum)[1] = s.getFailed();
                    allLines.addFailedTests(s.getFailed());
                } else {
                    tempMap.put(s.lineNum, tempMap.get(s.lineNum));
                    tempMap.get(s.lineNum)[0] += s.getPassed();
                    allLines.addPassedTests(s.getPassed());
                    tempMap.get(s.lineNum)[1] += s.getFailed();
                    allLines.addFailedTests(s.getFailed());
                }    
            }
        }

        for(Integer i : tempMap.keySet()) {
            Line s = new Line(i);
            s.setPassed(tempMap.get(i)[0]);
            s.setFailed(tempMap.get(i)[1]);
            allLines.add(s);
        }
    }
    
    /** Displays all the tests currently in test suite. */
    public void displayTests(){
        for(TestCoverage t : allTests){
            System.out.println(t);
        }
    }
}
