package lineReader;

import java.io.File;

/**
 * This class prints displays Line objects to represent the lines of a program in a cobertura serialized file.
 * Takes one argument - path to a cobertura.ser file.
 */

public class LineBasedReaderMain {
    
    public static void main(String[] args) {
        /* Workflow:
         * (1) Initialize the reader object
         * (2) Load the serialized file on onto the Reader Object
         * (3 & 4) Create the line map for the Reader Object
         * (5) Display map
         */    
        File f = new File(args[0]);    
        LineBasedSerReader lineBasedReader = new LineBasedSerReader(f);
        lineBasedReader.loadClassInfo();
        lineBasedReader.createClassesMap();
        lineBasedReader.displayLineMaps();
    }
}

