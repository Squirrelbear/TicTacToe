/**
 * TicTacToe
 * Author: Peter Mitchell (2021)
 *
 * MinimaxAI class:
 * An AI Behaviour to play TicTacToe.
 * Attempts to choose a move that optimises the position of
 * the AI player.
 *
 * This is based on the work at:
 * //https://gsurma.medium.com/tic-tac-toe-creating-unbeatable-ai-with-minimax-algorithm-8af9e52c1e7d
 */
public class MinimaxAI implements TicTacToeAI {
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
    public MinimaxAI(GameGrid gameGrid, boolean isX) {
        this.gameGrid = gameGrid;
        playAs = isX ? 1 : 2;
    }

    /**
     * Takes the turn by choosing the best move using a minimax algorithm.
     *
     * TODO: This method is flawed. It does NOT take into account the enemy player having a winning move that needs blocking.
     */
    @Override
    public void takeTurn() {
        int[] board = squashGrid();
        ResultPair action = minimax(board, 1);
        int actionX = action.b % 3;
        int actionY = action.b / 3;
        gameGrid.getGrid()[actionX][actionY].setCellState(playAs);
    }

    /**
     * Stores a pair of ints used to store the score and move for minimax.
     */
    private class ResultPair {
        /**
         * Paired values.
         */
        public int a, b;

        /**
         * Pairs a pair of values.
         *
         * @param a First value.
         * @param b Second value.
         */
        public ResultPair(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    /**
     * Based on: https://gsurma.medium.com/tic-tac-toe-creating-unbeatable-ai-with-minimax-algorithm-8af9e52c1e7d
     *
     * Recursively compares board states with changes to find the best scoring move.
     *
     * @param board The current board state.
     * @param player The player that is taking a turn.
     * @return A pair with score and move.
     */
        private ResultPair minimax(int[] board, int player) {
        int winner = getWinner(board);
        if (winner != 0) {
            return new ResultPair(winner * player,0); // -1 * -1 || 1 * 1
        }

        ResultPair score = new ResultPair(-2, -1);

        for (int i = 0; i < 9; i++) { // For all moves
            if (board[i] == 0) { // Only possible moves
                int[] boardWithNewMove = board.clone();
                boardWithNewMove[i] = player; // Try the move
                ResultPair scoreForTheMove = minimax(boardWithNewMove, -player);
                scoreForTheMove.a = -scoreForTheMove.a;
                if (scoreForTheMove.a > score.a) {
                    score.a = scoreForTheMove.a;
                    score.b = i;
                }
            }
        }
        if (score.b == -1) {
            return new ResultPair(0, -1); // No move - it's a draw
        }
        return score;
    }

    /**
     * Squashes the grid assuming 3x3 to 9x1. And changes the indexing to
     * having the AI player as 1, the opponent as -1, and 0 stays as 0.
     *
     * @return Squashed grid.
     */
    private int[] squashGrid() {
        GridCell[][] gridCells = gameGrid.getGrid();
        int[] result = new int[9];
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                result[x+y*3] = gridCells[x][y].getCellState();
                if(gridCells[x][y].getCellState() == 0) result[x+y*3] = 0;
                else if(gridCells[x][y].getCellState() == playAs) result[x+y*3] = 1;
                else result[x+y*3] = -1;
            }
        }
        return result;
    }

    /**
     * Checks for a winner. Returns either the current winner (1,-1) or 0 if no winner.
     *
     * @param board Reference to the board to check for a winner.
     * @return 1 if the AI has won this state, -1 if the player won, 0 for anything else.
     */
    private int getWinner(int[] board) {
        // Check the rows
        for(int y = 0; y < 3; y++) {
            boolean isMatch = true;
            for(int x = 0; x < 3; x++) {
                if(board[x+y*3] == 0 || board[x+y*3] != board[y*3]) {
                    isMatch = false;
                }
            }
            if(isMatch) return board[y*3];
        }

        // Check the columns
        for(int x = 0; x < 3; x++) {
            boolean isMatch = true;
            for(int y = 0; y < 3; y++) {
                if(board[x+y*3] == 0 || board[x+y*3] != board[x]) {
                    isMatch = false;
                }
            }
            if(isMatch) return board[x];
        }

        // Check left to right diagonal
        boolean isMatch = true;
        for(int x = 0, y = 0; x < 3; x++, y++) {
            if(board[x+y*3] == 0 || board[x+y*3] != board[0]) {
                isMatch = false;
            }
        }
        if(isMatch) return board[0];

        // Check other diagonal
        isMatch = true;
        for(int x = 0, y = 2; x < 3; x++, y--) {
            if(board[x+y*3] == 0 || board[x+y*3] != board[8]) {
                isMatch = false;
            }
        }
        if(isMatch) return board[8];

        // No match found
        return 0;
    }
}