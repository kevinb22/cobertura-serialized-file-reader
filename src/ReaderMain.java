package src;

import java.io.File;

/**
 * This class prints hit(times executed) and coverage(executed or not) information about the lines of a program in a cobertura serialized file.
 * Takes one argument - path to a cobertura.ser file.
 */

public class ReaderMain {

    public static void main(String[] args) throws InterruptedException {
	/* Workflow:
	 * (1) Initialize the reader object
	 * (2) Load the serialized file on onto the Reader Object
	 * (3 & 4) Create the class coverage map and class hit map for the Reader Object
	 * (5) Display maps
	 * */
	SerReader reader = new SerReader(new File(args[0]));
	reader.loadClassInfo();
	reader.createClassCoverageMaps();
	reader.createClassHitsMaps();
		
	//displays all the Hit and Coverage maps in the Project
	reader.displayAllClassHitAndCoverageMaps();		
    }
}
