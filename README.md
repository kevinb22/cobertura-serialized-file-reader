Cobertura Ser Reader
--------------------

SerReader 
---------------
The SerReader class takes a cobertura produced serialized file and extracts information from the file. 
It can be used to find both coverage and hits/executions per line of a program that has been instrumented by cobertura.

The main fields for the SerReader object are:
	
	- classCoverageMap is a Integer, Boolean map, where key=line number & value=executed

	-  classHitsMap is a Integer, Integer map, where key=line number & value=hit count

ReaderMain 
----------------
ReaderMain is a main class that initializes the SerReader and print out the
classCoverageMap and classHitsMap of a program that has been instrumented by cobertura.
ReaderMain takes one command line argument, which is the path to a cobertura.ser file. 
ReaderMain's workflow is as follows:

- (1) Initialize the SerReader object

- (2) Load the serialized file onto the SerReader Object

- (3) Use the SerReader to create a hit or execution class map

- (4) Display the results

Cobertura Overview
------------------
Cobertura stores the coverage data of a program into the ProjectData, ClassData, and LineData objects. 
The following gives a brief overview of these objects.
	
	|--- ProjectData:                       High level, represents the entire java project, holds all the ClassData objects 
		|
		|--- ClassData                  Mid level, represents a single class in a java project, holds LineData objects 
			|
			|--- LineData:          Low level, represents a line in a java class, holds hit/execution information 

The SerReader starts with the ProjectData object and seperates the ClassData objects. For each ClassData object it creats a coverage and
hit map using the information from each LineData object. For more information on how the Cobertura objets interact please look at the 
Cobertrua source code.

Test Run
--------
Within the repo is included the project triangle to serve as a test run of the SerReader. The test can be reproduced in eclipse using 
the Run Configurations option to specify command line arguments

- Use cobertura to instrument triangle which produces a serialized file. A cobertura.ser will appear in triangle/target/cobertura
	- cd Triangle
	- mvn cobertura:cobertura-integration-test

- run the run-main target with the path to the .ser file as the argument
	-  ant -Darg0=src/triangle/target/cobertura/cobertura.ser run-main

- ReaderMain will outputs the lines that were covered during test(s) and how many times each line
  was executed to the console.

LineBasedSerReader 
------------------------
The LineBasedSerReader is a subclass of the SerReader. It uses a Integer, Line map where key=line # & value=Line object. 
The Line object contains information about the line such as it's number and how many pass or fail tests the line has been executed in. 
Originally the LineBasedSerReader was to be used to re-implement the tarantula fault localization technique. But since cobertura
does not do per test line coverage the implementation was never fully completed. 
The LineBasedSerReader can be run using the similiar commands to the SerReader above

- run the LineBasedReaderMain
	- ant -Darg0=src/triangle/target/cobertura/cobertura.ser run-lb-main

LineBasedSerReader Supporting Classes 
-----------------------------------

- Line: Simple object that holds information about a line in a program

- LineList: Collection of Line objects

- TestCoverage: Object that holds the pass or fail result of a test, as well as the LineList corresponding to that test.

- TestCoverageSuite: Collection of TestCoverage objects. 

LineBasedSerReader Tarantula workflow
-------------------------------------
The LineBasedSerReader was originally going to be used as part of a re-implementation of the tarantula fault localization
technique. The LineBasedSerReader would have read the coverage of a program by a test suite then combine all the information
and compute suspiciousness levels per line based on the tarantula formula. However a more efficient way was discovered using
tacoco. The workflow for the LineBasedSerReader Tarantula Approach would have been:

- (1) Run one test case, use cobertura to output the serialized file with the lines
executed

- (2) Use LineBasedSerReader to create a LineList of the executed lines, assign
the LineList to a TestCoverage with the test case results

- (3) Repeat steps (1) & (2) until all test cases are covered

- (4) Put all the TestCoverage cases into a TestCoverageSuite object

- (5) Use TestCoverageSuite to calculate suspiciousness for each Line Object

Using the SerReader & LineBasedSerReader
-----------------------------------------
To use either the SerReader or the LineBasedSerReader to read and display line coverage of code other than triangle:

- (1) Create a pom.xml file that contains the cobertura plugin (see the pom.xml file in trianlge), create a package that follows 
a maven build with the program code in src/main and the test code in src/test. (See the triangle test package for clarification,
any pacakge to be read by the Readers should have a similiar layout) 

- (2) Move the entire package into the project src directory (along with main, lineReader, and triangle)

- (3) Follow the test run instructions (use cobertura to analyze the program and create a .ser file use the 
ant run-main command with -Darg0 being the path to the package's cobertura.ser file)

Directory Structure
-------------------
The directory structure is as follows
	
	cobertura-ser-reader
		|
		|--- lib:                       Jars or files used in the repo
		|
		|--- triangle:                  Test program 
		|
		|--- src:                       Contains source code for the readers
		   |
		   |--- main                    Contains SerReader and ReaderMain
		   |
		   |--- lineReader:             Contains all the code associated with the LineBasedSerReader subclass
