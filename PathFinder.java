/*
THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
CODE WRITTEN BY OTHER STUDENTS OR COPIED FROM ONLINE RESOURCES. _Jackson Dietz_
*/

import java.util.ArrayList;
import java.util.Stack;
import java.util.ArrayDeque;
import java.io.*;
import java.util.*;


/*
 * self-referential class to represent a position in a path
 */
class Position{
    public int i;     //row
    public int j;     //column
    public char val;  //1, 0, or 'X'

    // reference to the previous position (parent) that leads to this position on a path
    Position parent;

    Position(int x, int y, char v){
        i=x; j = y; val=v;
    }

    Position(int x, int y, char v, Position p){
        i=x; j = y; val=v;
        parent=p;
    }

    @Override //Override the equals method from the object
    // class in order to compare position objects with the correct logic
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) { return false;}
        else {
            Position newPos = (Position) obj; // Cast the input object to a position object
            if(this.i == newPos.i && this.j == newPos.j) //If the coordinates are equals
                return true; //Return true
            return false;  //Otherwise return false

        }
    }


}


public class PathFinder {

    // main method: reads in maze file and finds path using both stackSearch and queueSearch
    public static void main(String[] args) throws IOException {
        if(args.length<1){
            System.err.println("***Usage: java PathFinder maze_file");
            System.exit(-1);
        }

        char [][] maze;
        maze = readMaze(args[0]);
        printMaze(maze);
        Position [] path = stackSearch(maze);
        if( path == null ){
            System.out.println("Maze is NOT solvable (no valid path identified in stackSearch).");
        } else {
            System.out.println("stackSearch Solution:");
            printPath(path);
            printMaze(maze);
        }

        char [][] maze2 = readMaze(args[0]);
        path = queueSearch(maze2);
        if( path == null ){
            System.out.println("Maze is NOT solvable (no valid path identified in queueSearch).");
        } else {
            System.out.println("queueSearch Solution:");
            printPath(path);
            printMaze(maze2);
        }
    }


    public static Position[] stackSearch(char[][] maze) {
        Stack<Position> positions = new Stack<Position>();// Creates a stack to keep track of positions
        ArrayList<Position> visitedPositions = new ArrayList<Position>(); // Creates an ArrayList, keeping track of positions that have been visited
        ArrayList<Position> pathList = new ArrayList<Position>();// Creates an ArrayList, keeping track of the path from start to end
        Position[] path; // Initializes a Position array which will later hold the path for the code output

        int MZ = maze.length; // Simplified variable for maze.length to be used later

        // Push (0, 0) onto the stack which is the starting point
        positions.add(new Position(0, 0, maze[0][0], null));
        Position current; //Variable to represent the current position

        // While loop runs while there are more positions to explore
        while (!positions.empty()) {
            // Pop the top position from the stack and store it in current
            current = positions.pop();

            // This if statement checks if we are at the end of the maze... Use the overridden equals method
            if (current.equals(new Position(MZ - 1, maze[0].length - 1, maze[MZ - 1][maze[0].length - 1], current))) {
                // Traces back the path from exit to start
                while (current.parent != null) {  //while the current position is not the start
                    maze[current.i][current.j] = 'X';
                    pathList.add(current);
                    current = current.parent; //references previous position
                }
                maze[current.i][current.j] = 'X'; //marks the start position as X because it has no parent
                pathList.add(current); // adds the start position to the path

                path = new Position[pathList.size()]; // Creates an array for the final path of said paths size
                for (int i = pathList.size() - 1; i >= 0; i--) { //increment backwards
                    path[path.length - 1 - i] = pathList.get(i); //gets/recovers the contents from the end to the beginning
                }                                               // so that the path is displayed in the correct order
                return path; //return the path array


            } else if (current.val != '1') { //If the current position is not a wall

                // Check if the spot below is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.i != MZ - 1 && !visitedPositions.contains(current)) {
                    positions.push(new Position(current.i + 1, current.j, maze[current.i + 1][current.j], current));
                }
                // Check if the spot above is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.i != 0 && !visitedPositions.contains(current)) {
                    positions.push(new Position(current.i - 1, current.j, maze[current.i - 1][current.j], current));
                }
                // Check if the spot to the right is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.j != maze[0].length - 1 && !visitedPositions.contains(current)) {
                    positions.push(new Position(current.i, current.j + 1, maze[current.i][current.j + 1], current));
                }
                // Check if the spot below is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.j != 0 && !visitedPositions.contains(current)) {
                    positions.push(new Position(current.i, current.j - 1, maze[current.i][current.j - 1], current));
                }

                visitedPositions.add(current);  // Add current to the list of visited positions, so we can advance
            }
        }

        // If no path is found
        return null;
    }



    public static Position[] queueSearch(char[][] maze) {
        ArrayDeque<Position> positions = new ArrayDeque<Position>(); // Creates a queue to keep track of positions
        ArrayList<Position> visitedPositions = new ArrayList<Position>(); // Creates an ArrayList, keeping track of positions that have been visited
        ArrayList<Position> pathList = new ArrayList<Position>(); // Creates an ArrayList, keeping track of the path from start to end
        Position[] path; // Initializes a Position array which will later hold the path for the code output

        int MZ = maze.length; // Simplified variable for maze.length to be used later

        // add (0, 0) to the stack which is the starting point
        positions.add(new Position(0, 0, maze[0][0], null));
        Position current; //Variable to represent the current position

        // While loop runs while there are more positions to explore
        while (!positions.isEmpty()) {
            // Dequeue the top position from the stack and store it in current
            current = positions.pollFirst();

            // This if statement checks if we are at the end of the maze... ... Use the overridden equals method
            if (current.equals(new Position(MZ - 1, maze[0].length - 1, maze[MZ - 1][maze[0].length - 1], current))) {
                // Traces back the path from exit to start
                while (current.parent != null) { //while the current position is not the start
                    maze[current.i][current.j] = 'X';
                    pathList.add(current);
                    current = current.parent; //references previous position
                }
                // Add the starting position to the path
                maze[current.i][current.j] = 'X';  //marks the start position as X because it has no parent
                pathList.add(current);// adds the start position to the path

                // Create an array to store the final path
                path = new Position[pathList.size()]; // Creates an array for the final path of said paths size
                for (int i = pathList.size() - 1; i >= 0; i--) {//increment backwards
                    path[path.length - 1 - i] = pathList.get(i);//gets/recovers the contents from the end to the beginning
                }                                                // so that the path is displayed in the correct order
                return path; //return the path array


            } else if (current.val != '1') {//If the current position is not a wall
                // Check if the spot below is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.i != MZ - 1 && !visitedPositions.contains(current)) {
                    positions.add(new Position(current.i + 1, current.j, maze[current.i + 1][current.j], current));
                }
                // Check if the spot above is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.i != 0 && !visitedPositions.contains(current)) {
                    positions.add(new Position(current.i - 1, current.j, maze[current.i - 1][current.j], current));
                }
                // Check if the spot to the right is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.j != maze[0].length - 1 && !visitedPositions.contains(current)) {
                    positions.add(new Position(current.i, current.j + 1, maze[current.i][current.j + 1], current));
                }
                // Check if the spot below is unvisited or a boundary... push the item if it's not visited and not a boundary
                if (current.j != 0 && !visitedPositions.contains(current)) {
                    positions.add(new Position(current.i, current.j - 1, maze[current.i][current.j - 1], current));
                }

                visitedPositions.add(current); // Add current to the list of visited positions, so we can advance
            }
        }
        // If no path is found
        return null;
    }


    // prints path through maze
    public static void printPath(Position [] path){
        System.out.print("Path: ");
        for(Position p : path){
            System.out.print("(" + p.i + "," + p.j + ") ");
        }
        System.out.println();
    }

    // reads in maze from file
    public static char [][] readMaze(String filename) throws IOException{
        char [][] maze;
        Scanner scanner;
        try{
            scanner = new Scanner(new FileInputStream(filename));
        }
        catch(IOException ex){
            System.err.println("*** Invalid filename: " + filename);
            return null;
        }

        int N = scanner.nextInt();
        scanner.nextLine();
        maze = new char[N][N];
        int i=0;
        while(i < N && scanner.hasNext()){
            String line =  scanner.nextLine();
            String [] tokens = line.split("\\s+");
            int j = 0;
            for (; j< tokens.length; j++){
                maze[i][j] = tokens[j].charAt(0);
            }
            if(j!=N){
                System.err.println("*** Invalid line: " + i + " has wrong # columns: " + j);
                return null;
            }
            i++;
        }
        if(i!=N){
            System.err.println("*** Invalid file: has wrong number of rows: " + i);
            return null;
        }
        return maze;
    }

    // prints maze array
    public static void printMaze(char[][] maze){
        System.out.println("Maze: ");
        if(maze==null || maze[0] == null){
            System.err.println("*** Invalid maze array");
            return;
        }

        for(int i=0; i< maze.length; i++){
            for(int j = 0; j< maze[0].length; j++){
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

}
