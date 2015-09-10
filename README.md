Cobertura-ser-reader
--------------------

Set Up
------
The cobertura serReader requires the cobertura src files to be on the build path additionally cobertura will 
require slf4j (Simple Logging Facade for Java) just add the slf4j-api-...jar and slf4j-simple...jar. The
sites for these files are listed below. Alternatively the project includes the cobertura src files in the
cobertura directory and the slf4j files in the lib directory. (Note: Cobertura-2.1.1 does not contain the 
cobertura src files)

-https://github.com/cobertura/cobertura 

-http://www.slf4j.org/

Workflow
--------

- (1) Initialize the reader object

- (2) Load the serialized file on onto the serReader Object

- (3) Use the ser Reader tp create a hit or execution class map

- (4) Display the results

serReader
---------
The serReader takes the serialized file cobertura produces whenever it 
instruments a program and can extract information from the file. Currently it 
can be used to find both coverage and hits/executions per line of the instrumented program.
The main fields for the serReader object are:
	
	- classCoverageMap is a Integer, Boolean map, where key=line # & value=executed

	-  classHitMap is a Integer, Integer map, where key=line # & value=hit #

readerMain
----------
readerMain is a simple example main constructed to initialize the serReader and print out the
classCoverageMap and classHitMap of whatever serialized file the serReader loaded.
readerMain takes one command line argument, which is the absolute path to a 
cobertura.ser file.

Test Run
--------
Within the repo is included the project triangle, you can use the serReader to deserialize
the file produced when cobertura instruments triangle.

- Go into Triangle and use cobertura to produce a serialized file
	- cd Triangle
	- mvn cobertura:cobertura-integration-test

- Compile the reader classes in the src directory
	- javac -cp lib/\\\* src/\*.java

- In the target directory of Triangle, a cobertura directory with cobertura.ser will appear
  run the ReaderMain.java program with the absolute path to the .ser file as the argument
	- java -cp .:lib/\\\* src.ReaderMain triangle/target/cobertura/cobertura.ser

- ReaderMain will display which lines were covered by all the tests and how many times each line
  was executed.   

- The classes in src were originally run in eclipse using the Run Configurations option to specify 
command line arguments

lineBasedSerReader
------------------
The lineBasedSerReader is a subclass of the serReader. It uses a Integer, Line
map where key=line # & value=various info about the line #. The Line object 
contains information about the line such as it's number and how many pass or fail
tests the line has been executed in. Originally the lineBasedSerReader was to be
used to re-implement the tarantula fault localization technique. But since cobertura
does not do per test line coverage the implementation was never fully completed. The
lineBasedSerReader can be run using the similiar commands to the serReader above

- compile the lineBasedReader classes in lineReader directory

- java -cp .:lib/\\\* lineReader.lineBasesReaderMain triangle/target/cobertura/cobertura.ser

lineBasedSerRerader Associated Code 
------------------

- Line: Simple object that holds information about a line in a program

- LineList: Collection of Line objects

- TestCoverage: Object that holds the pass or fail result of a test, as well as the LineList corresponding to that test.

- TestCoverageSuite: Collection of TestCoverage objects. 

The workflow for the lineBasedSerReaderTarantula Approach would have been:
--------------------------------------------------------------------------

- (1) Run one test case, use cobertura to output the serialized file with the lines
executed

- (2) Use lineBasedSerReader to create a LineList of the executed lines, assign
the LineList to a TestCoverage with the test case results

- (3) Repeat steps (1) & (2) until all test cases are covered

- (4) Put all the TestCoverage cases into a TestCoverageSuite object

- (5) Use TestCoverageSuite to calculate suspiciousness for each Line Object

Directory Structure
-------------------
The directory structure is as follows
	
	cobertura-ser-reader
		|
		|--- lib:			Jars or files used in the repo
		|
		|--- src:			Contains serReader and ReaderMain
		|
		|--- lineReader:		Contains all the code associated with the lineBasedSerReader subclass 
		|
		|--- triangle:			Test program 


	

