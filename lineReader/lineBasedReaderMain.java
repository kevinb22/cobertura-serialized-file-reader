package lineReader;

import java.io.File;

public class lineBasedReaderMain {
	
	public static void main(String[] args) {
	
	/* Workflow:
 	* (1) Initialize the reader object
 	* (2) Load the serialized file on onto the Reader Object
 	* (3 & 4) Create the line map for the Reader Object
 	* (5) Display map
 	*/	
		File f = new File(args[0]);	
		lineBasedSerReader reader = new lineBasedSerReader(f);
		reader.loadClassInfo();
		reader.createClassLineMap();
		reader.displayClassLineMap();
	
	}
}

