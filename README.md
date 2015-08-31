Cobertura-ser-reader
--------------------

Set Up
------
The cobertura serReader requirescobertura 2.1.1: https://github.com/cobertura/cobertura to be on the
build path additionally cobertura will require slf4j (Simple Logging Facade for Java)
found at http://www.slf4j.org/, just add the slf4j-api-...jar and slf4j-simple...jar
to the buildpath/classpath

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
can be used to find both coverage and hits per line of the instrumented program.
The main fields for the serReader object are:

    - classCoverageMap is a Integer, Boolean map, where key=line # & value=executed

    - classHitMap is a Integer, Integer map, where key=line # & value=hit#

lineBasedSerReader
------------------
The lineBasedSerReader is a subclass of the serReader. It uses a Integer, Line
map where key=line # & value=various info about the line #. The Line object 
contains information about the line such as it's number and how many pass or fail
tests the line has been executed in. Originally the lineBasedSerReader was to be
used to implement the tarantula fault localization technique. But since cobertura
does not do per test line coverage the implementation was never fully completed.

lineBasedSerRerader Associated Code 
------------------

    - Line: Simple object that holds information about a line in a program

    - LineList: Collection of Line objects

    - TestCoverage: Object that holds the pass or fail result of a test, as well
    as the LineList corresponding to that test.

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