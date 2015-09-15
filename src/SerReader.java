package src;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler;
import net.sourceforge.cobertura.coveragedata.LineData;
import net.sourceforge.cobertura.coveragedata.ProjectData;

/**
 * This class deserializes a cobertura.ser file and creates a map representing lines executed or 
 * map representing hits per line of a the program contained in the .ser file.
 * A cobertura produced .ser file is required as a parameter in the constructor.
 */

public class SerReader{
    /** This file holds the serialized file of the instrumented object**/
    private File serFile;
	
    /** ProjectData class, serialized file is loaded onto this class**/
    protected ProjectData project;
	
    /** Collection to hold all the ClassData objects in the ProjectData object, important to make sure
     * all the classes are stored/loaded into allClasses before any other methods are called, useful
     * since it holds the ClassData objects it can access other ClassData objects unlike classMap**/
    protected Collection<ClassData> allClasses;
	
    /** Map where each String represents the name of a class in ProjectData
     *  For the map that is held in the value field:
     *	The key in the map represents the line number of a program and the 
     *  value represents whether the line was covered/executed in the tests **/
    private TreeMap<String, TreeMap<Integer, Boolean>> classCoverageMap;
	
    /** Map where each String represents the name of a class in ProjectData
     *  For the map that is held in the value field:	
     *  The key in map represents the line number of a program and the 
     *  value represents amount of times the line was covered/executed in the tests**/
    private TreeMap<String, TreeMap<Integer, Integer>> classHitsMap;
	
    /** Ensures that the classes in project have been retrieved and placed in a Collection**/
    protected boolean classesLoaded = false;
	
    /** Constructor, initialize the ser file, load the file into the ProjectData Object **/
    public SerReader(File serFile) {
	if(serFile.exists()) {
	    this.serFile = serFile;
	    this.project = new ProjectData();
	    this.project = CoverageDataFileHandler.loadCoverageData(serFile);
	} else {
	    System.out.println(serFile.getName() + " does not exist");
	}
    }
	
    /** Load all classes from ProjectData Object into a collection of ClassData Objects */
    @SuppressWarnings("unchecked")
    public void loadClassInfo()  {
	this.allClasses = this.project.getClasses();
	classesLoaded=true;
	System.out.print("Classes in Project:");
	for(ClassData i : allClasses) {
	    System.out.print(" " + i.getBaseName());
	}
	System.out.println(" have been loaded \n");
    }
	
    /** return a collection of all the classes loaded by serReader **/
    protected Collection<ClassData> getAllClasses(){
	return this.allClasses;
    }
	
    /** Create a map where key=class name, value=Map(LineNumber, boolean of coverage) for all the classes contained in the .ser file **/
    public void createClassCoverageMaps(){
	checkClassesLoaded();
	this.classCoverageMap = new TreeMap<String, TreeMap<Integer, Boolean>>();
	for(ClassData singleClass : this.allClasses) {
	    System.out.println("Placing " + singleClass.getBaseName() + " into classMap ... ");
	    TreeMap<Integer, Boolean> classCoverage = createClassCoverageMap(singleClass);
			
			
	    if (!classCoverage.isEmpty() && classCoverage != null) {
		System.out.println(singleClass.getBaseName() + " placed in classCoverageMap");
		this.classCoverageMap.put(singleClass.getBaseName(), classCoverage);
	    } else{ 
		System.out.println(singleClass.getBaseName() + " is empty, will not place in classCoverageMap");
	    }
	    System.out.println();
	}
    }
	
    /** Take a class and creates a map that holds key=line number, value=T/F for executed **/
    private TreeMap<Integer,Boolean> createClassCoverageMap(ClassData singleClass){
	TreeMap<Integer, Boolean> temp = new TreeMap<Integer, Boolean>();
	int allLinesAndBranches = singleClass.getNumberOfValidBranches() + singleClass.getNumberOfValidLines();
	for(int i = 0; i < allLinesAndBranches; i++) {
	    LineData line = singleClass.getLineData(i);
	    if(line != null && singleClass.isValidSourceLineNumber(i)) {
		double hits = line.getHits();
		if(line.isCovered() && hits > 0) {
		    temp.put(i, true);
		}else 
		    temp.put(i, false);
	    }
	}
	return temp;
    }
	
    /** Create a map where key=class name, value=Map(LineNumber, # of hits) for all classes contained in the .ser file**/
    public void createClassHitsMaps(){
	checkClassesLoaded();
	this.classHitsMap = new TreeMap<String, TreeMap<Integer, Integer>>();
	for(ClassData singleClass : this.allClasses) {
	    System.out.println("Placing " + singleClass.getBaseName() + " into classHitsMap ... ");
	    TreeMap<Integer, Integer> classHits = createClassHitsMap(singleClass);
			
	    if (!classHits.isEmpty() && classHits != null) {
		System.out.println(singleClass.getBaseName() + " placed in classHitsMap");
		this.classHitsMap.put(singleClass.getBaseName(), classHits);
	    } else{ 
		System.out.println(singleClass.getBaseName() + " is empty, will not place in classHitsMap");
	    }
	    System.out.println();
	}
    }
	
    /** Take a class and creates a map that holds key=line number, value=# of hits **/
    private TreeMap<Integer,Integer> createClassHitsMap(ClassData singleClass){
	TreeMap<Integer, Integer> temp = new TreeMap<Integer, Integer>();
	int allLinesAndBranches = singleClass.getNumberOfValidBranches() + singleClass.getNumberOfValidLines();
	for(int i = 0; i < allLinesAndBranches; i++) {
	    LineData line = singleClass.getLineData(i);
	    if(line != null && singleClass.isValidSourceLineNumber(i)) {
		int hits = (int) line.getHits();
		temp.put(i, hits);
	    }
	}
	return temp;
    }
	
    /** Loops through Class Map to find a certain class name, returns the Line Coverage Map of that class**/
    public TreeMap<Integer, Boolean> getClassCoverageInfo(String className){
	for(String clazz : this.classCoverageMap.keySet()){
	    if(clazz.equals(className))
		return this.classCoverageMap.get(clazz);
	}
	System.out.println("Class Map does not hold " + className);
	return null;
    }
	
		
    /** Loops through Class Map to find a certain class name, returns the Hit Coverage Map of that class**/
    public TreeMap<Integer, Integer> getClassHitInfo(String className){
	for(String clazz : this.classHitsMap.keySet()){
	    if(clazz.equals(className))
		return this.classHitsMap.get(clazz);
	}
	System.out.println("Class Map does not hold " + className);
	return null;
    }
	
    /** Returns the classCoverageMap **/
    public TreeMap<String, TreeMap<Integer, Boolean>> getClassCoverageMap(){
	return this.classCoverageMap;
    }
	
    /** Returns the classHitMap **/
    public TreeMap<String, TreeMap<Integer, Integer>> getClassHitMap(){
	return this.classHitsMap;
    }
	
    /** Displays all the classCoverageMaps in the Project */
    public void displayAllCoverageMaps(){
	for(String className : this.classCoverageMap.keySet()) {
	    if (className != null && this.classCoverageMap.get(className) != null) {
		System.out.println("Class: " + className);
		displayCoverage(this.classCoverageMap.get(className));
	    }
	}
    }
	
    /** Displays all the classHitsMaps in the Project */
    public void displayAllHitMaps(){
	for(String className : this.classHitsMap.keySet()) {
	    if (className != null && this.classHitsMap.get(className) != null) {
		System.out.println("Class: " + className);
		displayHits(this.classHitsMap.get(className));
	    }
	}
    }
	
    /** Loops through all ClassData objects in Collection
     *  if the class is in the Collection it will print out hit and coverage info about the class */
    public void displayClassHitAndCoverageMaps(String className) {
	checkClassesLoaded();
	for(ClassData clazz : this.allClasses) {
	    if(clazz.getBaseName().equals(className)) {
		display(clazz);
		return;
	    }
	}
	System.out.println(className + " Class is not in project");
    }
	
    /** Displays information about all the ClassData objects in Collection */
    public void displayAllClassHitAndCoverageMaps() {
	checkClassesLoaded();
	for(ClassData clazz: this.allClasses) {
	    display(clazz);
	}
    }
	
	
    /** Find various statistics about a ClassData object, helper program to get classClassInfo() method **/
    private void display(ClassData singleClass) {
	Map<Integer, Boolean> classLineCoverage = new TreeMap<Integer, Boolean>();
	Map<Integer, Integer> classHitCoverage = new TreeMap<Integer, Integer>();
	String className = singleClass.getBaseName();
			
	System.out.println("Class: " + className);
	System.out.println("Branches Covered: " + singleClass.getBranchCoverageRate() * 100 + "%");
	System.out.println("Valid Branches: " + singleClass.getNumberOfValidBranches());
	System.out.println("Covered Branches: " + singleClass.getNumberOfCoveredBranches());
	System.out.println("Line Coverage: " + singleClass.getLineCoverageRate() * 100 + "%");
	System.out.println("Valid lines: " + singleClass.getNumberOfValidLines());
	System.out.println("Covered Lines: " + singleClass.getNumberOfCoveredLines());
	System.out.println();
			
	classLineCoverage = createClassCoverageMap(singleClass);
	classHitCoverage = createClassHitsMap(singleClass);
			
	System.out.println("Determining lines executed ...");
	displayCoverage(classLineCoverage);
			
	System.out.println("Determining hits per lines ...");
	displayHits(classHitCoverage);
    }
	
    /** Loop through a classCoverage map and print out all the lines executed per class **/
    private void displayCoverage(Map<Integer, Boolean> classLineCoverage) {
	if(classLineCoverage.isEmpty() || classLineCoverage == null) {
	    System.out.println("Lines not covered in this class \n");
	} else {
	    for(Integer i : classLineCoverage.keySet()) {
		if(classLineCoverage.get(i)){
		    System.out.println("line: " + i + " executed");
		}else{
		    System.out.println("line: " + i + " un-executed");
		}
	    }
	    System.out.println();
	}
    }
	
    /** Loop through a classHit map and print out all the number of hits per line in a class **/
    private void displayHits(Map<Integer, Integer> classHitCoverage) {
	if(classHitCoverage.isEmpty() || classHitCoverage == null) {
	    System.out.println("Lines not covered in this class \n");
	} else {
	    for(Integer i : classHitCoverage.keySet()) {
		System.out.println("line: " + i + " hits: " + classHitCoverage.get(i));
	    }
	    System.out.println();
	}
    }
	
    /** Makes sure that all the ClassData objects are loaded if not throw an IllegalStateArgument **/
    protected void checkClassesLoaded(){
	if (!classesLoaded) {
	    throw new IllegalStateException("ClassData Collection has not been loaded, need to call reader.loadClassInfo()");
	}
    }
}
