import java.util.Collections;
import java.util.List;

/**
 * TicTacToe
 * Author: Peter Mitchell (2021)
 *
 * RandomAI:
 * A dumb AI behaviour that will choose
 * moves entirely at random from all those available.
 * Very unlikely to lose against this AI.
 */
public class RandomAI implements TicTacToeAI {
    /**
     * Reference to the GameGrid for evaluation and playing the turn.
     */
    private GameGrid gameGrid;
    /**
     * 1 for X or 2 for O representing the one that is being
     * played by this player.
     */
    private int playAs;

    /**
     * Initialises the AI ready to takeTurn()s.
     *
     * @param gameGrid Reference to the GameGrid for evaluation and playing the turns.
     * @param isX If true the AI will play as X, otherwise will play as O.
     */
    public RandomAI(GameGrid gameGrid, boolean isX) {
        this.gameGrid = gameGrid;
        playAs = isX ? 1 : 2;
    }

    /**
     * Chooses a random move from the valid moves and plays it with no consideration.
     */
    @Override
    public void takeTurn() {
        List<Position> validMoves = gameGrid.getALlValidMoves();
        Collections.shuffle(validMoves);
        gameGrid.getGrid()[validMoves.get(0).x][validMoves.get(0).y].setCellState(playAs);
    }
}
