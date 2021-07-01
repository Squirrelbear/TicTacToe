import java.awt.*;

/**
 * Tic Tac Toe
 * Author: Peter Mitchell (2021)
 *
 * GridCell class:
 * Manages a single grid cell stored as 0, 1 or 2.
 * 0 means the cell is unfilled, 1 is an X, and 2 is an O.
 */
public class GridCell extends Rectangle {
    /**
     * The state of the grid cell. 0=empty, 1=X, 2=O.
     */
    private int cellState;

    /**
     * Initialises the GridCell and defaults to empty.
     *
     * @param position Position to draw at.
     * @param width Width of the cell.
     * @param height Height of the cell.
     */
    public GridCell(Position position, int width, int height) {
        super(position, width, height);
        reset();
    }

    /**
     * Resets to the default of empty.
     */
    public void reset() {
        cellState = 0;
    }

    /**
     * Changes the state to the specified value.
     *
     * @param newState The new state to change to.
     */
    public void setCellState(int newState) {
        this.cellState = newState;
    }

    /**
     * Gets the current cell state.
     *
     * @return The current cell state.
     */
    public int getCellState() {
        return cellState;
    }

    /**
     * Draws either an X, O, or nothing depending on cellState.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        if(cellState == 1) {
            drawX(g);
        } else if(cellState == 2) {
            drawO(g);
        }
    }

    /**
     * Draws the X by splitting the given area into an 8x8 grid.
     * The coordinates are then offset based on the 8x8 internal grid
     * to draw an X using a polygon.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawX(Graphics g) {
        g.setColor(Color.BLACK);
        int x = position.x;
        int y = position.y;
        int sValX = width / 8;
        int sValY = height / 8;
        int[] xXCoordinates = {x+sValX,x+2*sValX,x+4*sValX,x+6*sValX,x+7*sValX,
                                x+5*sValX,x+7*sValX,x+6*sValX,x+4*sValX,x+2*sValX,x+sValX,x+3*sValX};
        int[] xYCoordinates = {y+2*sValY,y+sValY,y+3*sValY,y+sValY,y+2*sValY,y+4*sValY,
                                y+6*sValY,y+7*sValY,y+5*sValY,y+7*sValY,y+6*sValY,y+4*sValY};
        g.fillPolygon(xXCoordinates,xYCoordinates,xXCoordinates.length);
    }

    /**
     * Draws the O by drawing a large oval as red, and then
     * drawing a smaller oval in the middle of it matching the background colour.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawO(Graphics g) {
        g.setColor(Color.RED);
        int visibleDiameter = 15;
        g.fillOval(position.x+5, position.y+5, width-10,height-10);
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(position.x+5+visibleDiameter, position.y+5+visibleDiameter,
                width-10-visibleDiameter*2,height-10-visibleDiameter*2);
    }
}
