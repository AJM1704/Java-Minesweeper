// Aaron Meyerhofer meye3630
//Import Section
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 * 
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */

public class main {
    public static void main(String[] args) {
        boolean debugMode = false;
        int rows, columns, flags, mines;
        Scanner scanner = new Scanner(System.in);
        // Intro message and difficulty selection
        System.out.println("Welcome to Minesweeper!");
        System.out.println("Select game mode (easy, medium, or hard): ");
        String user = scanner.nextLine();
        // Set game parameters based on the chosen mode
        if (user.equals("easy")) {
            rows = columns = mines = flags = 5;
        }
        else if (user.equals("medium")) {
            rows = columns = 9;
            mines = flags = 12;
        }
        else if (user.equals("hard")) {
            rows = columns = 20;
            mines = flags = 40;
        }
        // Abilty to play debug mode by typing "debug" in the difficulty selection screen
        else if (user.equals("debug")) {
            debugMode = true;
            Scanner scannerDebug = new Scanner(System.in);
            String userDebug = scannerDebug.nextLine();
            if (userDebug.equals("easy")) {
                rows = columns = mines = flags = 5;
            }
            else if (userDebug.equals("medium")) {
                rows = columns = 9;
                mines = flags = 12;
            }
            else if (userDebug.equals("hard")) {
                rows = columns = 20;
                mines = flags = 40;
            }
            else {
                // Automatically start game on easy if incorrect words are typed
                System.out.println("Invalid input, Setting game difficulty to easy");
                rows = columns = mines = flags = 5;
            }
        }
        else {
            // Automatically start game on easy if incorrect words are typed
            System.out.println("Invalid input, Setting game difficulty to easy");
            rows = columns = mines = flags = 5;
        }
        // Create new minefield with given parameters
        Minefield minefield = new Minefield(rows, columns, flags);
        minefield.createMines(0, 0, mines);
        minefield.evaluateField();
        minefield.revealStartingArea(0, 0);
        int x = 0;
        int y = 0;
        // Game loop
        while (!minefield.gameOver()) {
            // Print minefield in debug mode if specified
            if (debugMode) {
                minefield.debug();
            } else {
                System.out.println(minefield);
            }
            // flag input
            boolean flag = false;
            while (true) {
                System.out.print("Do you want to put a flag down? (y/n): ");
                String input = scanner.next();
                if (input.equalsIgnoreCase("y")) {
                    flag = true;
                    break; // Break loop when "true" is entered
                } else if (input.equalsIgnoreCase("n")) {
                    flag = false;
                    break; // Break loop when "false" is entered
                } else {
                    System.out.println("Invalid input. Please enter y or n.");
                }
            }
            // x coordinate input
            System.out.println("Enter x coordinate: ");
            x = scanner.nextInt();
            while (x < 0 || x > rows-1) {
                System.out.println("Invalid y coordinate entered. Please try again.");
                System.out.println("Enter valid y coordinates: ");
                x = scanner.nextInt();
            }
            // y coordinate input
            System.out.println("Enter y coordinate: ");
            y = scanner.nextInt();
            while (y < 0 || y > columns-1) {
                System.out.println("Invalid x coordinate entered. Please try again.");
                System.out.println("Enter valid x coordinates: ");
                y = scanner.nextInt();
            }
            // check for mine hits
            boolean mineHit = minefield.guess(x, y, flag);
            if (mineHit) {
                System.out.println(minefield);
                System.out.println("Oops! You hit a mine. Game over!");
                break;
            }
            else if (minefield.gameOver()) {
                System.out.println(minefield);
                System.out.println("Congratulations! You've won the game!");
                break;
            }
        }
        scanner.close();
    }
}
