# Algorithm.Assn2
Algorithm and Analysis assignment two, in collaboration with <a href="https://github.com/CY1223">Chin</a>.  
The program generate maze use Hunt and Kill algorithm, and Kruskal algorithm.   
Then solve maze use normal solver and recursive backtracker solver.  

## Generate and solve maze  
Run ``MazeTester.java`` with arguments  
``mazeType generatorName solverName numRow numCol entranceRow entranceCol exitRow exitCol tunnelList``   

### To compile on server:  
``javac -cp .:mazeSolver/SampleSolver.jar *.java``  

### To run on server:  
``java -cp .:mazeSolver/SampleSolver.jar MazeTester inputFilename n``   
