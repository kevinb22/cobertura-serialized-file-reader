package lineReader;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import src.SerReader;
import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.LineData;

/**
 * This class deserializes a cobertura.ser file and creates a map with with a 
 * Line Object pertaining to each line of a the program contained in the .ser file.
 * A cobertura produced .ser file is required as a parameter in the constructor.
 */

public class LineBasedSerReader extends SerReader {
	
    /** Map where each String represents the name of a class in ProjectData
     * Each Map holds the Line data associated per executed line in the class  
     * solely used for this purpose, is not able to have other info about ClassData**/
    private TreeMap<String, TreeMap<Integer, Line>> classesMap;
	
    /** Constructor, initialize the ser file, load the file into the ProjectData Object **/
    public LineBasedSerReader(File serFile) {
	super(serFile);
    }
	
    /** Create a map where key=class name, value=Map(LineNumber, Line) **/
    public void createClassLineMap() {
	checkClassesLoaded();
	this.classesMap = new TreeMap<String, TreeMap<Integer, Line>>();
	for(ClassData singleClass : this.allClasses) {
	    System.out.println("Placing " + singleClass.getBaseName() + " into classMap ... ");
	    TreeMap<Integer, Line> classLineCoverage = createClassLineCoverage(singleClass);
	    System.out.println(classLineCoverage);
			
	    if (!classLineCoverage.isEmpty() && classLineCoverage != null) {
		System.out.println(singleClass.getBaseName() + " placed in classMap");
		this.classesMap.put(singleClass.getBaseName(), classLineCoverage);
	    } else{ 
		System.out.println(singleClass.getBaseName() + " is empty, will not place in classMap");
	    }
	    System.out.println();
	}
    }
	
    /** Loops through classes in the classesMap to find a certain class, returns the Line Map **/
    public TreeMap<Integer, Line> getClassLineInfo(String className){
	for(String clazz : this.classesMap.keySet()){
	    if(clazz.equals(className))
		return this.classesMap.get(clazz);
	}
	System.out.println("Class Line Map does not hold " + className);
	return null;
    }
	

    /** returns the Class Line Map **/
    public TreeMap<String, TreeMap<Integer, Line>> getClassLineMap(){
	return this.classesMap;
    }
	
    /** Displays the classLineMap **/
    public void displayClassLineMap() {
	for(String className : this.classesMap.keySet()) {
	    if (className != null && this.classesMap.get(className) != null) {
		System.out.println("Class: " + className);
		display(this.classesMap.get(className));
	    }
	}
    }
	
    /** Take a class and creates a map that holds key=line number, value=Line object**/
    private TreeMap<Integer, Line> createClassLineCoverage(ClassData singleClass){
	TreeMap<Integer, Line> temp = new TreeMap<Integer, Line>();
	int allLinesAndBranches = singleClass.getNumberOfValidBranches() + singleClass.getNumberOfValidLines();
	for(int i = 0; i < allLinesAndBranches; i++) {
	    LineData line = singleClass.getLineData(i);
	    if(line != null && singleClass.isValidSourceLineNumber(i)) {
		double hits = line.getHits();
		if(line.isCovered() && hits > 0) { 
		    Line code_line = new Line(i);
		    code_line.set_exec_count((int)hits);
		    temp.put(i, code_line);
		} else {
		    Line code_line = new Line(i);
		    code_line.set_exec_count(0);
		    temp.put(i, code_line);
		}
	    }
	}
	return temp;
    }
	
    /** Loop through a classLineCoverage map and print out all the Line in the class **/
    private void display(Map<Integer, Line> classLineCoverage) {
	if(classLineCoverage.isEmpty() || classLineCoverage == null) {
	    System.out.println("Lines not covered in this class \n");
	} else {
	    for(Integer i : classLineCoverage.keySet()) {
		if(classLineCoverage.get(i) != null)
		    System.out.println(classLineCoverage.get(i));
	    }
	    System.out.println();
	}
    }
}
