public class GameOfLife implements GameInterface {
    private boolean[][] originalMap,    // The initial cell configuration
                                map,    // The current cell configuration
                             newMap;    // The next generation configuration
    private GameGUI gui;
    private int generation = 0;
    private FileIO fileIO;

    // GameOfLife constructor
    public GameOfLife() {
        originalMap = new boolean[MAX_ROWS][MAX_COLS];
        map = new boolean[MAX_ROWS][MAX_COLS];
        newMap = new boolean[MAX_ROWS][MAX_COLS];
        gui = new GameGUI(this, map);
        gui.setTitle("CIS 181 Array Based Game Of Life");
        fileIO = new FileIO("life", "Game of Life Text Files");
        readExample(2);
    }

    //  ====>>>>> Complete the methods below this line! <<<<<====

    // copyMap:
    // Precondtions: None.
    // Postcondtion: 'map' is a deep copy of 'sourceMap'.
    //
    private void copyMap(boolean sourceMap[][]) {

        // ==> 1. Add your code here!
        
        // Loop through every row.
        for(int r = 0; r < sourceMap.length; r++) {
            // Loop through every column.
            for(int c = 0; c < sourceMap[r].length; c++) {
                // Set map[r][c] equal to the corresponding value of sourceMap[r][c].
                map[r][c] = sourceMap[r][c];
            }
        }

    }

    // clearMap:
    // Precondtions: None.
    // Postcondtion: Sets all cells of the 'targetMap' to DEAD.
    //
    private void clearMap(boolean targetMap[][]) {

        // ==> 2. Add your code here!

        // Loop through every row.
        for(int r = 0; r < targetMap.length; r++) {
            // Loop through every column.
            for(int c = 0; c < targetMap[r].length; c++) {
                // Set targetMap[r][c] to DEAD.
                targetMap[r][c] = DEAD;
            }
        }

    }

    //  getFlatNeighborCount:
    //  Precondtions:  0 <= row < MAX_ROWS and 0 <= col < MAX_COLS.
    //  Postcondtion:  A count of all LIVE neighbors of the cell at [row, col] is
    //                 returned where its neighbors are all the ADJACENT cells
    //                 including those
    //                 a) In the rows BELOW and ABOVE the cell (if any exist).
    //                 b) In the columns LEFT and RIGHT of the cell (if any exist).
    //                 Thus, a cell adjacent to a board edge (or corner) has
    //                 fewer neighbors than other cells.
    //
    private int getFlatNeighborCount(int row, int col){
        int count = 0;

        // ==> 3. Add your code here!

        // Loop through rows (row - 1) to (row + 1).
        for(int r = row - 1; r <= row + 1; r++) {
            // Loop through columns (col - 1) to (col + 1).
            for(int c = col - 1; c <= col + 1; c++) {
                // Ensure the cell at (r, c) is within the grid.
                if(r < 0 || r >= MAX_ROWS || c < 0 || c >= MAX_COLS) continue;

                // Ensure the cell at (r, c) is not the cell at (row, col).
                if(r == row && c == col) continue;

                // Check if the cell at (r, c) is alive, and increment count if true.
                if(map[r][c] == ALIVE) count++;
            }
        }

        // Return the number of adjacent alive cells.
        return count;
    }


    // nextGenerationForFlatGrid:
    // Precondtions: None
    // Postcondtion: The next generation of live and dead cells is calculated using
    //               a) the FLAT neighbor counts.
    //               b) the current birth, survival and death count rules.
    //               c) the rules are applied to the counts obtained from the current
    //                  generation's configuration of live and dead cells.
    //               The current 'map' is updated to the next generation's configuration
    //               of live and dead cells.
    //
    public void nextGenerationForFlatGrid() {

        // ==> 4. Add your code here!

        // Loop through every row.
        for(int r = 0; r < map.length; r++) {
            // Loop through every column.
            for(int c = 0; c < map[r].length; c++) {
                // Check to see if the current cell is dead.
                if(map[r][c] == DEAD) {
                    // Check to see if this cell has exactly 3 living neighbors (Birth rule).
                    if(getFlatNeighborCount(r, c) == 3) {
                        // With 3 living neighbors, the cell is reborn.
                        newMap[r][c] = ALIVE;
                    } else {
                        // Otherwise, the cell is dead.
                        newMap[r][c] = DEAD;
                    }
                }

                // Otherwise, the cell is alive.
                else {
                    // Check to see if this cell has exactly 2 or 3 living neighbors (Survival rule).
                    if(getFlatNeighborCount(r, c) == 2 || getFlatNeighborCount(r, c) == 3) {
                        // With 2 or 3 living neighbors, the cell survives.
                        newMap[r][c] = ALIVE;
                    } else {
                        // Otherwise, the cell dies (Death rule).
                        newMap[r][c] = DEAD;
                    }
                }
            }
        }

        // Update the current map with the values from newMap.
        copyMap(newMap);

        // Clear the newMap.
        clearMap(newMap);

    }


    // ==> 5. Implement the game of life for torus grid.

    // nextGenerationForTorusGrid:
    // Precondtions: None
    // Postcondtion: The next generation of live and dead cells is calculated using
    //               a) the TORUS neighbor counts.
    //               b) the current birth, survival and death count rules.
    //               c) the rules are applied to the counts obtained from the current
    //                  generation's configuration of live and dead cells.
    //               The current 'map' is updated to the next generation's configuration
    //               of live and dead cells.
    //
    public  void nextGenerationForTorusGrid() {
        // Loop through every row.
        for(int r = 0; r < map.length; r++) {
            // Loop through every column.
            for(int c = 0; c < map[r].length; c++) {
                // Initialize a counter for the number of alive neighbors.
                int neighborCount = 0;

                // Loop through rows (r - 1) to (r + 1).
                for(int r1 = r - 1; r1 <= r + 1; r1++) {
                    // Loop through columns (c - 1) to (c + 1).
                    for(int c1 = c - 1; c1 <= c + 1; c1++) {
                        // Initialize a row variable to hold the correct row.
                        int row = r1 < 0 ? MAX_ROWS + r1 : (r1 >= MAX_ROWS ? r1 - MAX_ROWS : r1);

                        // Initialize a col variable to hold the correct column.
                        int col = c1 < 0 ? MAX_COLS + c1 : (c1 >= MAX_COLS ? c1 - MAX_COLS : c1);

                        // Ensure the cell at (row, col) is not the cell at (r, c).
                        if(row == r && col == c) continue;

                        // Check if the cell at (row, col) is alive, and increment count if true.
                        if(map[row][col] == ALIVE) neighborCount++;
                    }
                }

                // Check to see if the current cell is dead.
                if(map[r][c] == DEAD) {
                    // Check to see if this cell has exactly 3 living neighbors (Birth rule).
                    if(neighborCount == 3) {
                        // With 3 living neighbors, the cell is reborn.
                        newMap[r][c] = ALIVE;
                    } else {
                        // Otherwise, the cell is dead.
                        newMap[r][c] = DEAD;
                    }
                }

                // Otherwise, the cell is alive.
                else {
                    // Check to see if this cell has exactly 2 or 3 living neighbors (Survival rule).
                    if(neighborCount == 2 || neighborCount == 3) {
                        // With 2 or 3 living neighbors, the cell survives.
                        newMap[r][c] = ALIVE;
                    } else {
                        // Otherwise, the cell dies (Death rule).
                        newMap[r][c] = DEAD;
                    }
                }
            }
        }

        // Update the current map with the values from newMap.
        copyMap(newMap);

        // Clear the newMap.
        clearMap(newMap);
    }




    //  ====>>>>> Don't touch the code below this line! <<<<<====

    // Return the next generation
    public int getGeneration() {
        return generation;
    }

    // Reset the map to the original map
    public void reset() {
        copyMap(originalMap);
        generation = 0;
        gui.repaint();
    }

    // Game of life examples 1-4: Fish, Plus, Glider, FlyingMachine
    public void readExample(int n) {
        System.out.println("Initializing with example " + n + " ...");
        clearMap(originalMap);

        switch (n) {
           case 1: // Example 1: Fish
             for (int col = 23; col <= 26; col++)
                 originalMap[13][col] = ALIVE;
             originalMap[14][22] = ALIVE;
             originalMap[14][26] = ALIVE;
             originalMap[15][26] = ALIVE;
             originalMap[16][22] = ALIVE;
             originalMap[16][25] = ALIVE;
             break;
           case 2: // Example 2: Plus
               for (int col = 6; col < 43; col++)
                 originalMap[24][col] = ALIVE;
             for (int row = 6; row < 43; row++)
                 originalMap[row][24] = ALIVE;
             break;
           case 3: // Example 3: Glider
                originalMap[14][23] = ALIVE;
             originalMap[15][24] = ALIVE;
             for (int row = 13; row <= 15; row++)
                 originalMap[row][25] = ALIVE;
             break;
           case 4: // Example 4: FlyingMachine
                for (int col = 22; col <= 25; col++) {
                 originalMap[11][col] = ALIVE;
                 originalMap[19][col] = ALIVE;
             }
             for (int row = 14; row <= 16; row++)
                 for (int col = 17; col <= 18; col++)
                     originalMap[row][col] = ALIVE;
             originalMap[15][19] = ALIVE;
             for (int row = 12; row <= 18; row = row+2)
                 originalMap[row][21] = ALIVE;
             originalMap[14][24] = ALIVE;
             originalMap[16][24] = ALIVE;
             originalMap[12][25] = ALIVE;
             originalMap[13][25] = ALIVE;
             originalMap[17][25] = ALIVE;
             originalMap[18][25] = ALIVE;
             break;
           default: // Default Example: ClearSpace
             break;
        }

        copyMap(originalMap);
        generation = 0;
        gui.repaint();
    }

    //  Read map from file
    public void readInMap() {
        clearMap(originalMap);
        if (fileIO.read(originalMap)) {
            copyMap(originalMap);
            generation = 0;
        } else
            readExample(2);
        gui.repaint();
    }

    // Write map to file
    public void writeMap() {
       fileIO.write(map);
    }

    // Change the state of a cell
    public void updateMap(int row, int col) {
        map[row][col] = !map[row][col];

    }

    // Destroy the GUI window
    public void destroy() {
        gui.dispose();
    }

    // The main method of GameOfLife
    public static void  main(String[] args) {
        GameOfLife game = new GameOfLife();
    }
}
