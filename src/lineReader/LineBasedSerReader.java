package lineReader;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import main.SerReader;
import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.LineData;

/**
 * This class deserializes a cobertura.ser file and creates a map with with a 
 * Line Object pertaining to each line of a the program contained in the .ser file.
 * A cobertura produced .ser file is required as a parameter in the constructor.
 *
 * Once this object is declared and initialized the first call should be to
 * the loadClassInfo() method which loads all the info in the .ser file to the object.
 * Next the object needs to call thecreateClassesMap() method. This method creates a Line Map 
 * for each class that is in the .ser file. 
 * Afterwards displayLineMaps() can be called to display all the line maps held by the object.
 */

public class LineBasedSerReader extends SerReader {
    /** Map where each String represents the name of a class in ProjectData
     *  For each map in the value field
     *  The key in the map represents the line number of the porgram and the value is a 
     *  Line object holding information about the line 
     */
    private TreeMap<String, TreeMap<Integer, Line>> classesMap;
    
    /** Constructor, initialize the ser file, load the file into the ProjectData Object. */
    public LineBasedSerReader(File serFile) {
        super(serFile);
    }
    
    /** Create a map where key=class name, value=Map(LineNumber, Line). */
    public void createClassesMap() {
        checkClassesLoaded();
        this.classesMap = new TreeMap<String, TreeMap<Integer, Line>>();
        for(ClassData singleClass : this.allClasses) {
            System.out.println("Placing " + singleClass.getBaseName() + " into classesMap ... ");
            TreeMap<Integer, Line> classLineCoverage = createClassLineCoverage(singleClass);
            System.out.println(classLineCoverage);
        
            if (!classLineCoverage.isEmpty() && classLineCoverage != null) {
                System.out.println(singleClass.getBaseName() + " placed in classesMap");
                this.classesMap.put(singleClass.getBaseName(), classLineCoverage);
            } else{ 
                System.out.println(singleClass.getBaseName() + " is empty, will not place in classesMap");
            }
        System.out.println();
        }
    }
    
    /** Loops through the classesMap to find the class specified by the parameter, returns the Line Map of the class. */
    public TreeMap<Integer, Line> getClassLineMap(String className){
        for(String clazz : this.classesMap.keySet()){
            if(clazz.equals(className))
                return this.classesMap.get(clazz);
        }
        System.out.println("Class Line Map does not hold " + className);
        return null;
    }
    

    /** Returns the classesMap. */
    public TreeMap<String, TreeMap<Integer, Line>> getclassesMap(){
        return this.classesMap;
    }
    
    /** Displays the Line Map for all classes in classesMap */
    public void displayLineMaps() {
        for(String className : this.classesMap.keySet()) {
            if (className != null && this.classesMap.get(className) != null) {
                System.out.println("Class: " + className);
                display(this.classesMap.get(className));
            }
        }
    }
    
    /** Take a class and creates a map that holds key=line number, value=Line object. */
    private TreeMap<Integer, Line> createClassLineCoverage(ClassData singleClass){
        TreeMap<Integer, Line> temp = new TreeMap<Integer, Line>();
        int allLinesAndBranches = singleClass.getNumberOfValidBranches() + singleClass.getNumberOfValidLines();
        for(int i = 0; i < allLinesAndBranches; i++) {
            LineData line = singleClass.getLineData(i);
            if(line != null && singleClass.isValidSourceLineNumber(i)) {
                double hits = line.getHits();
                if(line.isCovered() && hits > 0) { 
                    Line codeLine = new Line(i);
                    codeLine.setExecCount((int)hits);
                    temp.put(i, codeLine);
                } else {
                    Line codeLine = new Line(i);
                    codeLine.setExecCount(0);
                    temp.put(i, codeLine);
                }
            }
        }
        return temp;
    }
    
    /** Loop through a Line Coverage map of a class and print out all the Lines. */
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
