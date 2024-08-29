// Aaron Meyerhofer meye3630
// Import section
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.LinkedList;
public class Minefield {
    /**
    Global Section
    */
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE = "\u001B[39m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /* 
     * Class Variable Section
     * 
    */

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */
    
    /**
     * Minefield
     * 
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    private Cell[][] field;
    private int rows;
    private int columns;
    private int flags;
    private int mines;

    // Constructor to initialize the Minefield with specified rows, columns, and flags
    public Minefield(int rows, int columns, int flags) {
        this.columns = columns;
        this.rows = rows;
        this.field = new Cell[rows][columns];

        // Initialize each cell in the field with default values
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                field[i][j] = new Cell(false, "-");
            }
        }
    }

    /**
     * evaluateField
     * 
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     * 
     */
    // Method to check numbers
    public void evaluateField() {
        // Iterate through every cell and count mines to number
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!field[i][j].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
                    int count = countSurroundingMines(i, j);

                    // Set the status and color of the cell based on the number of surrounding mines
                    if (count == 0){
                        field[i][j].setStatus(ANSI_YELLOW+String.valueOf(count)+ANSI_GREY_BACKGROUND);
                    }if (count == 1){
                        field[i][j].setStatus(ANSI_BLUE+String.valueOf(count)+ANSI_GREY_BACKGROUND);
                    }if (count == 2){
                        field[i][j].setStatus(ANSI_GREEN+String.valueOf(count)+ANSI_GREY_BACKGROUND);
                    }if (count == 3){
                        field[i][j].setStatus(ANSI_PURPLE+String.valueOf(count)+ANSI_GREY_BACKGROUND);
                    }if (count >= 4){
                        field[i][j].setStatus(ANSI_WHITE+String.valueOf(count)+ANSI_GREY_BACKGROUND);
                    }
                }
            }
        }
    }

    /**
     * createMines
     * 
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     * 
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    // Method to create mines
    public void createMines(int x, int y, int mines) {
        // Use random to place mines
        Random rand = new Random();
        int minesPlaced = 0;
        // Loop through till specified number of mines are placed
        while (minesPlaced < mines) {
            int randX = rand.nextInt(this.rows);
            int randY = rand.nextInt(this.columns);
            // Check if Coordinates are valid and place mine in location
            if (randX >= 0 && randX < this.rows && randY >= 0 && randY < this.columns && randX != x && randY != y
                    && !field[randX][randY].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
                field[randX][randY].setStatus(ANSI_RED+"M"+ANSI_GREY_BACKGROUND);
                minesPlaced++;
            }
        }
    }

    /**
     * guess
     * 
     * Check if the guessed cell is inbounds (if not done in the Main class). 
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     * 
     * 
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    // Method to process user guess
    public boolean guess(int x, int y, boolean flag) {
        if (flag) {
            // Place a flag
            field[x][y].setRevealed(true);
            field[x][y].setStatus(ANSI_CYAN+"F"+ANSI_GREY_BACKGROUND);
            return false; // Flag placed, no mine found
        }if (field[x][y].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            field[x][y].setRevealed(true);
            return true; // Mine found, game over
        } else if (field[x][y].getStatus().equals(ANSI_YELLOW+"0"+ANSI_GREY_BACKGROUND)) {
            revealZeroes(x, y);
            return false; // No mine found
        } else {
            field[x][y].setRevealed(true);
            return false; // No mine found
        }
    }

    /**
     * gameOver
     * 
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     * 
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    // Method to check if the games is over
    public boolean gameOver() {
        // Check to see if a mine has been revealed
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell currentCell = field[i][j];
                if (currentCell.getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND) && currentCell.getRevealed()) {
                    return true;
                }
            }
        }
        // Check to see if all cells without mines are revealed
        int nonMineCells = rows * columns - mines;
        int revealedNonMineCells = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell currentCell = field[i][j];
                if (!currentCell.getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND) && !currentCell.getStatus().equals("-")) {
                    revealedNonMineCells++;
                }
            }
        }
        return revealedNonMineCells == nonMineCells; // If equal returns true
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    // Method to reveal cells that contain zeros adjacent to other zeros
    public void revealZeroes(int x, int y) {
        // Initialize stack to push locations with zeros
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x, y});
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int currX = current[0];
            int currY = current[1];
            if (isValid(currX, currY) && !field[currX][currY].getRevealed() && field[currX][currY].getStatus().equals(ANSI_YELLOW+"0"+ANSI_GREY_BACKGROUND)) {
                field[currX][currY].setRevealed(true);
                // Loop through adjacent cells (left, right, above, below)
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) { // Exclude the current cell
                            int nextX = currX + i;
                            int nextY = currY + j;
                            if (isValid(nextX, nextY) && !field[nextX][nextY].getRevealed() && field[nextX][nextY].getStatus().equals(ANSI_YELLOW+"0"+ANSI_GREY_BACKGROUND)) {
                                stack.push(new int[]{nextX, nextY}); // Push valid neighboring coordinates to the stack
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * 
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    // Method to reveal a section of the grid so the player can start the game
    public void revealStartingArea(int x, int y) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, y});
        while (!queue.isEmpty()) {
            int[] start = queue.poll();
            int startX = start[0];
            int startY = start[1];
            // Set the corresponding cell's revealed attribute as true
            field[startX][startY].setRevealed(true);
            // If the current cell is a mine, break from the loop
            if (field[startX][startY].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
                field[startX][startY].setRevealed(false);
                break;
            }
            // Enqueue all reachable neighbors that are in bounds and not already visited
            for (int i = startX - 1; i <= startX + 1; i++) {
                for (int j = startY - 1; j <= startY + 1; j++) {
                    if (i >= 0 && i < rows && j >= 0 && j < columns &&
                            !field[i][j].getRevealed()) {
                        queue.add(new int[]{i, j});
                    }
                }
            }
        }
    }

    /**
     * For both printing methods utilize the ANSI color codes provided!
     * 
     * 
     * 
     * 
     * 
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected. 
     */
    // Method to print grid in debug mode (Everything is revealed)
    public void debug() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(field[i][j].getStatus() + " ");
            }
            System.out.println();
        }
    }
    private int countSurroundingMines(int x, int y){
        int count = 0;
        // Left
        if (isValid(x+1, y) && field[x+1][y].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        // Right
        if (isValid(x-1, y) && field[x-1][y].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        // Down
        if (isValid(x, y+1) && field[x][y+1].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        // Up
        if (isValid(x, y-1) && field[x][y-1].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        //Bottom-Right
        if (isValid(x+1, y+1) && field[x+1][y+1].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        //Bottom-Left
        if (isValid(x-1, y+1) && field[x-1][y+1].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        //Top-Right
        if (isValid(x+1, y-1) && field[x+1][y-1].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        //Top-Right
        if (isValid(x-1, y-1) && field[x-1][y-1].getStatus().equals(ANSI_RED+"M"+ANSI_GREY_BACKGROUND)) {
            count++;
        }
        return count;
    }
    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    // Method to return a string representation of the revealed squares in the minefield
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (field[i][j].getRevealed()) {
                    sb.append(field[i][j].getStatus()).append(" ");
                } else {
                    sb.append("- ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    // Method to check if the coordinates are valid within the minefield
    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < columns;
    }

}
