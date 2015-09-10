package lineReader;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import src.serReader;
import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler;
import net.sourceforge.cobertura.coveragedata.LineData;
import net.sourceforge.cobertura.coveragedata.ProjectData;

public class lineBasedSerReader extends serReader {
	
	/** Map where each String represents the name of a class in ProjectData
	 * Each Map holds the Line data associated per executed line in the class  
	 * solely used for this purpose, is not able to have other info about ClassData**/
	private TreeMap<String, TreeMap<Integer, Line>> classLineMap;

	/** Ensures that the classes in project have been retrieved and placed in a Collection**/
	private boolean classesLoaded = false;
	
	/** constructor, initialize the ser file, load the file into the ProjectData Object **/
	public lineBasedSerReader(File serFile) {
		super(serFile);
	}
	
	/** return a collection of all the classes loaded by serReader **/
	public Collection<ClassData> getAllClasses(){
		return this.allClasses;
	}
	
	/** Create a map where key=class name, value=Map(LineNumber, Line) **/
	public void createClassLineMap() {
		checkClassesLoaded();
		this.classLineMap = new TreeMap<String, TreeMap<Integer, Line>>();
		for(ClassData singleClass : this.allClasses) {
			System.out.println("Placing " + singleClass.getBaseName() + " into classMap ... ");
			TreeMap<Integer, Line> classLineCoverage = createClassLineCoverage(singleClass);
			System.out.println(classLineCoverage);
			
			if (!classLineCoverage.isEmpty() && classLineCoverage != null) {
				System.out.println(singleClass.getBaseName() + " placed in classMap");
				this.classLineMap.put(singleClass.getBaseName(), classLineCoverage);
			} else{ 
				System.out.println(singleClass.getBaseName() + " is empty, will not place in classMap");
			}
			System.out.println();
		}
	}
	
	/** Loops through Class Line Map to find a certain class, returns it Line Map**/
	public TreeMap<Integer, Line> getClassLineInfo(String className){
		for(String clazz : this.classLineMap.keySet()){
			if(clazz.equals(className))
				return this.classLineMap.get(clazz);
		}
		System.out.println("Class Line Map does not hold " + className);
		return null;
	}
	

	/** returns the Class Line Map **/
	public TreeMap<String, TreeMap<Integer, Line>> getClassLineMap(){
		return this.classLineMap;
	}
	
	/** displays the classLineMap **/
	public void displayClassLineMap() {
		for(String className : this.classLineMap.keySet()) {
			if (className != null && this.classLineMap.get(className) != null) {
				System.out.println("Class: " + className);
				display(this.classLineMap.get(className));
			}
		}
	}
	
	/**take a class and creates a map that holds key=line number, value=Line object**/
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
	
	// loop through a classLineCoverage map and print out all the Line in the class 
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
