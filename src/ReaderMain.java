package coberturaTarantula;

import java.io.File;

public class ReaderMain {

	public static void main(String[] args) throws InterruptedException {
		/* Workflow:
		 * (1) Initialize the reader object
		 * (2) Load the serialized file on onto the Reader Object
		 * (3 & 4) Create the class coverage map and class hit map for the Reader Object
		 * (5) Display maps
		 * */
		serReader reader = new serReader(new File(args[0]));
		reader.loadClassInfo();
		reader.createClassCoverageMap();
		reader.createClassHitMap();
		
		//displays all the Hit and Coverage maps in the Project
		reader.displayAllClassHitAndCoverage();		
	}
}