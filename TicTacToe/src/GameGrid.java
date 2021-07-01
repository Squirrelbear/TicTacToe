import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tic Tac Toe
 * Author: Peter Mitchell (2021)
 *
 * GameGrid class:
 * Manages a grid of GridCells providing methods to
 * draw and check the state of them.
 */
public class GameGrid extends Rectangle {
    /**
     * The grid of cells tracking current game state.
     */
    private GridCell[][] grid;

    /**
     * Creates a grid of GridCells with the specified offset and sizing.
     *
     * @param position Top left corner offset of the grid.
     * @param width Width of the grid.
     * @param height Height of the grid.
     * @param gridWidth Number of grid cells horizontally.
     * @param gridHeight Number of grid cells vertically.
     */
    public GameGrid(Position position, int width, int height, int gridWidth, int gridHeight) {
        super(position, width, height);
        grid = new GridCell[gridWidth][gridHeight];
        int cellWidth = (width-position.x)/gridWidth;
        int cellHeight = (height-position.y)/gridHeight;
        for(int x = 0; x < gridWidth; x++) {
            for(int y = 0; y < gridHeight; y++) {
                grid[x][y] = new GridCell(new Position(position.x+cellWidth*x, position.y+cellHeight*y),
                                            cellWidth, cellHeight);
            }
        }
    }

    /**
     * Forces all the GridCells to reset back to their default state.
     */
    public void reset() {
        for(int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].reset();
            }
        }
    }

    /**
     * Gets the grid cell data.
     *
     * @return The array of all grid cells.
     */
    public GridCell[][] getGrid() {
        return grid;
    }

    /**
     * Searches the grid to find all currently valid moves.
     *
     * @return A list containing all valid moves.
     */
    public List<Position> getALlValidMoves() {
        List<Position> validMoves = new ArrayList<>();
        for(int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if(grid[x][y].getCellState() == 0) {
                    validMoves.add(new Position(x,y));
                }
            }
        }
        return validMoves;
    }

    /**
     * Searches the grid to determine if the mouse position matches with a valid GridCell.
     *
     * @param mousePosition Position where the mouse cursor is located.
     * @return A reference to the selected GridCell, or null if none was found.
     */
    public GridCell getGridCellAt(Position mousePosition) {
        int gridX = (mousePosition.x- position.x)/grid[0][0].width;
        int gridY = (mousePosition.y- position.y)/grid[0][0].height;
        if(gridX >= grid.length || gridX < 0 || gridY >= grid[0].length || gridY < 0) {
            return null;
        }
        return grid[gridX][gridY];
    }

    /**
     * Checks all cells to find if any are empty.
     *
     * @return True if all cells have been filled.
     */
    public boolean isFull() {
        for(int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if(grid[x][y].getCellState() == 0) return false;
            }
        }
        return true;
    }

    /**
     * Checks if the specified playerID has given the current board state.
     *
     * @param playerID The playerID to check against for victory.
     * @return True if the specified player has won.
     */
    public boolean checkForWin(int playerID) {
        // Check the rows
        for(int y = 0; y < grid[0].length; y++) {
            int matchCount = 0;
            for(int x = 0; x < grid.length; x++) {
                if(grid[x][y].getCellState() == playerID) {
                    matchCount++;
                }
            }
            if(matchCount == grid.length) return true;
        }

        // Check the columns
        for(int x = 0; x < grid.length; x++) {
            int matchCount = 0;
            for(int y = 0; y < grid[0].length; y++) {
                if(grid[x][y].getCellState() == playerID) {
                    matchCount++;
                }
            }
            if(matchCount == grid[0].length) return true;
        }

        // Check left to right diagonal
        int matchCount = 0;
        for(int x = 0, y = 0; x < grid.length; x++, y++) {
            if(grid[x][y].getCellState() == playerID) {
                matchCount++;
            }
        }
        if(matchCount == grid.length) return true;

        // Check other diagonal
        matchCount = 0;
        for(int x = 0, y = grid[0].length-1; x < grid.length; x++, y--) {
            if(grid[x][y].getCellState() == playerID) {
                matchCount++;
            }
        }
        if(matchCount == grid.length) return true;

        // No match found
        return false;
    }

    /**
     * Draws grid lines to box in the cells, and then draws the content of all GridCells.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        drawGridLines(g);
        for(int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].paint(g);
            }
        }
    }

    /**
     * Draws lines to create boxes around all the positions where GridCells are located.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawGridLines(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = position.y+height;
        int y1 = position.y;
        for(int x = 0; x < grid.length+1; x++)
            g.drawLine(position.x+x * grid[0][0].width, y1, position.x+x * grid[0][0].width, y2);

        // Draw horizontal lines
        int x2 = position.x+width;
        int x1 = position.x;
        for(int y = 0; y < grid[0].length+1; y++)
            g.drawLine(x1, position.y+y * grid[0][0].height, x2, position.y+y * grid[0][0].height);
    }
}
