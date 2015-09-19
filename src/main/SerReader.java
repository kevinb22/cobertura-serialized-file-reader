package main;

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
 * 
 * A cobertura produced .ser file is required as a parameter in the constructor.
 *
 * Once a SerReader object is declared and initialized the first call should be to the loadClassInfo()
 * method which loads all the information from the .ser file to the object.
 * Next the createClassCoverageMaps() and createClassHitsMaps() methods should be called
 * which create the executions and hits maps respectively.
 * Finally the displayAllClassHitsAndCoverageMaps() method should be called to dispaly both
 * these maps.
 *
 * Alternatively displayAllHitsMaps() and displayAllCoverageMaps() displays only the hits or 
 * coverage maps in the object whereas displayClassHitsAndCoverageMaps(String className) 
 * prints the hits and coverage maps of the class specified by the string. 
 */

public class SerReader{
    /** This file holds the serialized file of the instrumented object. */
    private File serFile;
    
    /** ProjectData class, serialized file is loaded onto this class. */
    protected ProjectData project;
    
    /** Collection to hold all the ClassData objects in the ProjectData object, important to make sure
     * all the classes are stored/loaded into allClasses before any other methods are called, useful
     * since it holds the ClassData objects it can access other ClassData objects unlike classMap. */
    protected Collection<ClassData> allClasses;
    
    /** Map where each String represents the name of a ClassData object in ProjectData
     *  For the map that is held in the value field:
     *  The key in the map represents the line number of a program and the 
     *  value represents whether the line was covered/executed in the tests */
    private TreeMap<String, TreeMap<Integer, Boolean>> classCoverageMap;
    
    /** Map where each String represents the name of a ClassData object in ProjectData
     *  For the map that is held in the value field:    
     *  The key in map represents the line number of a program and the 
     *  value represents amount of times the line was covered/executed in the tests */
    private TreeMap<String, TreeMap<Integer, Integer>> classHitsMap;
    
    /** Ensures that the ClassData objects in a ProjectData object have been retrieved and placed in a Collection. */
    protected boolean classesLoaded = false;
    
    /** Constructor, initializes the ser file, load the file into the ProjectData Object. */
    public SerReader(File serFile) {
        if(serFile.exists()) {
            this.serFile = serFile;
            this.project = new ProjectData();
            this.project = CoverageDataFileHandler.loadCoverageData(serFile);
        } else {
            System.out.println(serFile.getName() + " does not exist");
        }
    }
    
    /** Places all ClassData objects from ProjectData Object into a collection of ClassData Objects. */
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
    
    /** Returns a collection of all the ClassData object that have been loaded. **/
    protected Collection<ClassData> getAllClasses(){
        return this.allClasses;
    }
    
    /** Creates a map where key=class name, value=Map(LineNumber, boolean of coverage) for all loaded ClassData objects. */
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
    
    /** Takes a ClassData object as a parameter and for that object creates a map that holds key=line number, value=T/F for executed. */
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
    
    /** Creates a map where key=class name, value=Map(LineNumber, # of hits) for all loaded ClassData objects. */
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
    
    /** Takes a ClassData object as a parameter and for that object creates a map that holds key=line number, value=# of hits. */
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
    
    /** Returns the Line Coverage Map of the class specified in the parameter. */
    public TreeMap<Integer, Boolean> getClassCoverageInfo(String className){
        for(String clazz : this.classCoverageMap.keySet()){
            if(clazz.equals(className))
                return this.classCoverageMap.get(clazz);
        }
        System.out.println("Class Map does not hold " + className);
        return null;
    }
    
    /** Returns the Hit Coverage Map of that class specified in the parameter. */
    public TreeMap<Integer, Integer> getClassHitInfo(String className){
        for(String clazz : this.classHitsMap.keySet()){
            if(clazz.equals(className))
                return this.classHitsMap.get(clazz);
        }
        System.out.println("Class Map does not hold " + className);
        return null;
    }
    
    /** Returns the classCoverageMap. */
    public TreeMap<String, TreeMap<Integer, Boolean>> getClassCoverageMap(){
        return this.classCoverageMap;
    }
    
    /** Returns the classHitsMap. */
    public TreeMap<String, TreeMap<Integer, Integer>> getClassHitMap(){
        return this.classHitsMap;
    }
    
    /** Displays the classCoverageMaps for each ClassData object in the ProjectData object. */
    public void displayAllCoverageMaps(){
        for(String className : this.classCoverageMap.keySet()) {
            if (className != null && this.classCoverageMap.get(className) != null) {
                System.out.println("Class: " + className);
                displayCoverage(this.classCoverageMap.get(className));
            }
        }
    }
    
    /** Displays the classHitsMaps for each ClassData object in the ProjectData object. */
    public void displayAllHitMaps(){
        for(String className : this.classHitsMap.keySet()) {
            if (className != null && this.classHitsMap.get(className) != null) {
                System.out.println("Class: " + className);
                displayHits(this.classHitsMap.get(className));
            }
        }
    }
    
    /** Displays the hit and coverage info of the class specified in the parameter. */
    public void displayClassHitsAndCoverageMaps(String className) {
        checkClassesLoaded();
        for(ClassData clazz : this.allClasses) {
            if(clazz.getBaseName().equals(className)) {
                display(clazz);
                return;
            }
        }
        System.out.println(className + " Class is not in project");
    }
    
    /** Displays the hits and coverage info about all the ClassData objects in Collection. */
    public void displayAllClassHitsAndCoverageMaps() {
        checkClassesLoaded();
        for(ClassData clazz: this.allClasses) {
            display(clazz);
        }
    }
    
    /** Finds various statistics about a ClassData object, helper program. */
    private void display(ClassData singleClass) {
        Map<Integer, Boolean> classLineCoverage = new TreeMap<Integer, Boolean>();
        Map<Integer, Integer> classHitsCoverage = new TreeMap<Integer, Integer>();
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
        classHitsCoverage = createClassHitsMap(singleClass);
            
        System.out.println("Determining lines executed ...");
        displayCoverage(classLineCoverage);
            
        System.out.println("Determining hits per lines ...");
        displayHits(classHitsCoverage);
    }
    
    /** Loops through a coverage map and prints out all the lines executed per class. */
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
    
    /** Loops through a hits map and prints out the number of hits per line in a class. */
    private void displayHits(Map<Integer, Integer> classHitsCoverage) {
        if(classHitsCoverage.isEmpty() || classHitsCoverage == null) {
            System.out.println("Lines not covered in this class \n");
        } else {
            for(Integer i : classHitsCoverage.keySet()) {
                System.out.println("line: " + i + " hits: " + classHitsCoverage.get(i));
            }
            System.out.println();
        }
    }
    
    /** Makes sure that all the ClassData objects are loaded if not throw an IllegalStateArgument. */
    protected void checkClassesLoaded(){
        if (!classesLoaded) {
            throw new IllegalStateException("ClassData Collection has not been loaded, need to call reader.loadClassInfo()");
        }
    }
}
