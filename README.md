# Algorithm.Assn2
Algorithm and Analysis assignment two, in collaboration with <a href="https://github.com/CY1223">Chin</a>.  
The program generates maze use Hunt and Kill algorithm, and Kruskal algorithm. The user specifies the maze's size, with tunnel or without tunnel, the tunnel position.    
Then it solves the maze use normal solver and recursive backtracker solver.  
A test program will evaluate the time used to generate the maze using the two algorithms. It will also evaluate the time used to solve the maze.   

## Generate and solve maze  
Run ``MazeTester.java`` with arguments  
``mazeType generatorName solverName numRow numCol entranceRow entranceCol exitRow exitCol tunnelList``   

### To compile on server:  
``javac -cp .:mazeSolver/SampleSolver.jar *.java``  

### To run on server:  
``java -cp .:mazeSolver/SampleSolver.jar MazeTester inputFilename n``   
