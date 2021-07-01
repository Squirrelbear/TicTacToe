/**
 * Tic Tac Toe
 * Author: Peter Mitchell (2021)
 *
 * TicTacToeAI interface:
 * Used to define any AI behaviour that all have a takeTurn().
 * This should take the current game state and make a single move on the board.
 */
public interface TicTacToeAI {
    /**
     * Takes a turn. This should be implemented to modify the current game state by playing a valid move.
     */
    void takeTurn();
}
