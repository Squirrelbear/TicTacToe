import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Tic Tac Toe
 * Author: Peter Mitchell (2021)
 *
 * GamePanel class:
 * Controls the game state through clicks to iterate between
 * current turns and changing to a game over state once the game ends.
 */
public class GamePanel extends JPanel implements MouseListener {
    /**
     * The states the game can be in.
     * XTurn means that player 1 is placing an X.
     * OTurn means that player 2 is placing a O. (if using AI this will be instant)
     * Draw means the game has ended due to all positions being filled.
     * XWins means there is a sequence of 3 Xs in a row.
     * OWins means there is a sequence of 3 Os in a row.
     */
    public enum GameState {XTurn,OTurn,Draw,XWins,OWins}

    /**
     * Height of the panel.
     */
    private static final int PANEL_HEIGHT = 600;
    /**
     * Width of the panel.
     */
    private static final int PANEL_WIDTH = 500;

    /**
     * The grid of positions controlling maintaining the game state of the board.
     */
    private GameGrid gameGrid;
    /**
     * Reference to an ai behaviour if there is one defined, or null if there is no AI (pvp)
     */
    private TicTacToeAI aiBehaviour;
    /**
     * The current game state.
     */
    private GameState gameState;
    /**
     * A string representing the current game state. It is set when changing game states with setGameState().
     */
    private String gameStateStr;

    /**
     * Configures the game ready to be played including selection of playing against either
     * AI or another player.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.LIGHT_GRAY);

        gameGrid = new GameGrid(new Position(0,0), PANEL_WIDTH, PANEL_HEIGHT-100, 3, 3);
        setGameState(GameState.XTurn);
        chooseAIType();
        addMouseListener(this);
    }

    /**
     * Draws the game grid and draws the message at the bottom showing a string representing the game state.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        super.paint(g);
        gameGrid.paint(g);
        drawGameState(g);
    }

    /**
     * Resets the grid and returns the turn back to default.
     */
    public void restart() {
        gameGrid.reset();
        setGameState(GameState.XTurn);
    }

    /**
     * Pressing Escape will exit. Pressing R will reset.
     * Pressing A will swap the AI mode. Note that it will
     * stay as player input for the O turn if it was O
     * when the AI was swapped.
     *
     * @param keyCode The key that was pressed.
     */
    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if(keyCode == KeyEvent.VK_R) {
            restart();
            repaint();
        } else if(keyCode == KeyEvent.VK_A) {
            chooseAIType();
        }
    }

    /**
     * Handles a click at a location attempting to apply a move.
     * If the click is invalid or the cell is already filled the move
     * is ignored. Otherwise the current player's X or O is placed
     * in the selected cell and the game state moves to the next
     * turn.
     *
     * @param mousePosition Position where the mouse is currently located.
     */
    private void handleTurn(Position mousePosition) {
        GridCell selectedCell = gameGrid.getGridCellAt(mousePosition);
        if(selectedCell == null || selectedCell.getCellState() != 0) {
            return;
        } else if(gameState == GameState.OTurn) {
            selectedCell.setCellState(2);
            setGameState(GameState.XTurn);
        } else if(gameState == GameState.XTurn) {
            selectedCell.setCellState(1);
            setGameState(GameState.OTurn);
        }
    }

    /**
     * Changes the state and modifies the message to display
     * at the bottom of the game to show the current state.
     *
     * @param newState The new state to set.
     */
    private void setGameState(GameState newState) {
        gameState = newState;
        switch (gameState) {
            case XTurn: gameStateStr = "Player 1 Turn"; break;
            case OTurn: gameStateStr = "Player 2 Turn"; break;
            case XWins: gameStateStr = "Player 1 Wins! Press R."; break;
            case OWins: gameStateStr = "Player 2 Wins! Press R."; break;
            case Draw: gameStateStr = "Draw! Press R."; break;
        }
    }

    /**
     * Tests for draws, and either player winning, then changes
     * the game state appropriately.
     */
    private void testForEndGame() {
        if(gameGrid.checkForWin(1)) {
            setGameState(GameState.XWins);
        } else if(gameGrid.checkForWin(2)) {
            setGameState(GameState.OWins);
        } else if(gameGrid.isFull()) {
            setGameState(GameState.Draw);
        }
    }

    /**
     * Only does something if state is XTurn or OTurn.
     * Attempts to place the X or O and then checks for change in
     * game state. If the AI is enabled and it is their turn
     * after a valid move they are told to take a turn with
     * the game state again evaluated after their turn.
     *
     * @param e Information about the mouse event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(gameState == GameState.XTurn || gameState == GameState.OTurn) {
            Position mousePosition = new Position(e.getX(), e.getY());
            handleTurn(mousePosition);
            testForEndGame();

            if(gameState == GameState.OTurn && aiBehaviour != null) {
                aiBehaviour.takeTurn();
                setGameState(GameState.XTurn);
                testForEndGame();
            }
        }

        repaint();
    }

    /**
     * Draws the text showing the current game state centered at the bottom
     *  of the window.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawGameState(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        int strWidth = g.getFontMetrics().stringWidth(gameStateStr);
        g.drawString(gameStateStr, PANEL_WIDTH/2-strWidth/2, PANEL_HEIGHT-40);
    }

    /**
     * Shows a dialog box with options to select PvP or PvAI with Random or Hard.
     * Choosing PvP leaves the AI behaviour unset, and otherwise creates
     * an instance of the appropriate AI.
     */
    private void chooseAIType() {
        String[] options = new String[] {"Player vs Player", "Player vs Random AI", "Player vs Hard AI"};
        String message = "Select the game mode you would like to use.";
        int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                "Choose how to play.",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch(difficultyChoice) {
            case 0: // Remove the AI so it becomes PvP
                aiBehaviour = null;
                break;
            case 1:
                aiBehaviour = new RandomAI(gameGrid, false);
                break;
            case 2:
                aiBehaviour = new MinimaxAI(gameGrid, false);
                break;
        }
    }

    /**
     * Not set.
     *
     * @param e Not set.
     */
    @Override
    public void mouseClicked(MouseEvent e) {}
    /**
     * Not set.
     *
     * @param e Not set.
     */
    @Override
    public void mouseReleased(MouseEvent e) {}
    /**
     * Not set.
     *
     * @param e Not set.
     */
    @Override
    public void mouseEntered(MouseEvent e) {}
    /**
     * Not set.
     *
     * @param e Not set.
     */
    @Override
    public void mouseExited(MouseEvent e) {}
}
