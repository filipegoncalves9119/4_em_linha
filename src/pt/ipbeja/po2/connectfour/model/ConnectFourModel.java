package pt.ipbeja.po2.connectfour.model;

import pt.ipbeja.po2.connectfour.View;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Filipe Gonçalves (6050)
 * @version 09/04/2019
 */
public class ConnectFourModel {
    private Cell[][] dataBoard;
    private final View VIEW;
    private static final int ROW_SIZE = 6;
    private static final int COL_SIZE = 7;
    private int turnCounter = 0;
    private int player;
    private int score;
    private List<Plays> moves = new ArrayList<>();
    private List<Plays> movesRedo = new ArrayList<>();


    /**
     * Class contructor
     *
     * @param view the Board View
     */
    public ConnectFourModel(View view) {
        this.VIEW = view;
        this.dataBoard = new Cell[this.ROW_SIZE][this.COL_SIZE];
        this.fillBoard();
    }


    /**
     * Fills the array of arrays with empty cells
     */
    private void fillBoard() {
        for (int i = 0; i < this.ROW_SIZE; i++) {
            for (int j = 0; j < this.COL_SIZE; j++) {
                this.dataBoard[i][j] = Cell.EMPTY;
            }
        }
    }


    /**
     * move the piece last available Cell of a chosen collumn
     * win and draw method call
     * @param row coordinates
     * @param col coordinates
     */
    public void rollDown(int row, int col) {
        int i = this.ROW_SIZE - 1;
        if (this.isFree(row, col)) {
            while (i >= row) {
                if (this.isFree(i, col)) {
                    this.updateBoardState(i, col);

                    i = row - 1;
                }
                i--;
            }
        }
        if (this.inWinnableTurn(this.turnCounter)) {
            if (this.winningCondition(col)) {
                this.VIEW.playerWins();
                this.VIEW.highScores();
            }
        }
        if (this.isDraw(col)) {
            this.VIEW.gameDraw();
        }
    }

    /**
     * Switch between players
     *
     * @param row coordinates
     * @param col coordinates
     */
    public void updateBoardState(int row, int col) {

        if (this.turnCounter % 2 == 0) {
            this.dataBoard[row][col] = Cell.PLAYER1;
            this.moves.add(new Plays(row, col, Cell.PLAYER1));
            this.player = 1;
        } else {
            this.dataBoard[row][col] = Cell.PLAYER2;
            this.moves.add(new Plays(row, col, Cell.PLAYER2));
            this.player = 2;
        }
        this.movesRedo.clear();
        this.turnCounter++;
    }


    /**
     * Check line for winning condition
     *
     * @return true if a winning condition was found
     */
    public boolean hasWinningLine() {
        int fourCounter = 1;

        for (int line = this.ROW_SIZE - 1; line > 0; line--) {
            fourCounter = 1;
            for (int i = 0; i < this.COL_SIZE - 1; i++) {
                if (this.dataBoard[line][i] != Cell.EMPTY) {
                    if (this.dataBoard[line][i] == this.dataBoard[line][i + 1]) {
                        fourCounter++;
                        if (fourCounter == 4) {
                            this.setScoreNumber();
                            return true;
                        }
                    }
                    if (this.dataBoard[line][i] != this.dataBoard[line][i + 1]) {
                        fourCounter = 1;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Check column for winning condition
     *
     * @param col coordinate
     * @return true if a winning condition was found
     */
    public boolean hasWinningColumn(int col) {
        int fourCounter = 1;

        for (int i = 0; i < this.ROW_SIZE - 1; i++) {
            if (this.dataBoard[i][col] != Cell.EMPTY) {
                if (this.dataBoard[i][col] == this.dataBoard[i + 1][col]) {
                    fourCounter++;
                    if (fourCounter == 4) {
                        this.setScoreNumber();

                        return true;
                    }
                }
                if (this.dataBoard[i][col] != this.dataBoard[i + 1][col]) {
                    fourCounter = 1;
                }
            }
        }
        return false;
    }


    /**
     * checks for win condition in the diagonal
     *
     * @return true if winning condition was found
     */
    public boolean hasWinningDiagonal() {
        for (int i = 0; i < this.ROW_SIZE; i++) {
            for (int j = 0; j < this.COL_SIZE; j++) {
                if (this.diagNeighboor(i, j) == 4 || this.antiDiagNeighboor(i, j) == 4) {
                    this.setScoreNumber();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Search for adjacent diagonal cells that are a match
     *
     * @param row coordinate
     * @param col coordinate
     * @return
     */
    private int antiDiagNeighboor(int row, int col) {
        int fourCounter = 1;
        while ((row - 1 >= 0 && col - 1 >= 0)) {
            if (this.dataBoard[row][col] == this.dataBoard[row - 1][col - 1] && this.dataBoard[row][col] != Cell.EMPTY) {
                fourCounter++;
                if (fourCounter == 4)
                    return fourCounter;
            } else return fourCounter;
            row--;
            col--;
        }
        return fourCounter;
    }

    /**
     * Search for adjacent diagonal cells that are a match
     *
     * @param row coordinate
     * @param col coordinate
     * @return
     */
    private int diagNeighboor(int row, int col) {
        int fourCounter = 1;
        while ((row - 1 >= 0 && col + 1 <= this.COL_SIZE - 1)) {
            if (this.dataBoard[row][col] == this.dataBoard[row - 1][col + 1] && this.dataBoard[row][col] != Cell.EMPTY) {
                fourCounter++;
                if (fourCounter == 4)
                    return fourCounter;
            } else return fourCounter;
            row--;
            col++;
        }
        return fourCounter;
    }

    /**
     * Method to save last played cell to a list and remove it from the gridpane
     */
    public void undo() {
        if (this.moves.size() > 0) {
            Plays plays = this.moves.get(this.moves.size() - 1);
            int row = plays.getRow();
            int col = plays.getCol();


            this.dataBoard[row][col] = Cell.EMPTY;
            this.movesRedo.add(plays);

            this.moves.remove(plays);
            this.VIEW.undo(row, col);
            this.turnCounter--;
        }
    }

    /**
     * Method to redo last removed cell back into the gridpane from an array list
     */
    public void redo() {
        if (this.movesRedo.size() > 0) {
            Plays plays = this.movesRedo.get(this.movesRedo.size() - 1);
            int row = plays.getRow();
            int col = plays.getCol();

            this.dataBoard[row][col] = plays.getCell();
            this.moves.add(plays);

            this.VIEW.redo(row, col);
            this.movesRedo.remove(plays);
            this.turnCounter++;


        }
    }

    /**
     * Delete piece on the last row and roll down the remain ones in same collumn
     * @param col coordinate
     */
    public void poppingOut(int col) {
        int row = this.ROW_SIZE - 1;

        while (row > 0) {
            Cell cell = getCell(row - 1, col);
            this.dataBoard[row][col] = cell;
            this.dataBoard[row - 1][col] = Cell.EMPTY;
            row--;

        }

        if (this.winningCondition(col)){
            this.VIEW.playerWins();
            this.VIEW.highScores();
        }
    }

    /**
     * Gather all winning conditions
     *
     * @param col coordinate
     * @return true whenever a condition is found
     */
    public boolean winningCondition(int col) {
        return hasWinningColumn(col) || this.hasWinningLine() || this.hasWinningDiagonal();
    }


    /**
     * Checks if it is possible to win in this turn
     *
     * @param turn the turn to check
     * @return true if can win false otherwise
     */
    private boolean inWinnableTurn(int turn) {
        return turn >= (this.COL_SIZE);
    }

    /**
     *  when game is end and is draw
     * @param col ccoordinate
     * @return true if is draw
     */
    public boolean isDraw(int col) {
        if (this.turnCounter == 42 && !this.winningCondition(col)) {
            return true;
        }
        return false;
    }


    /**
     * Check if it's free position
     *
     * @param row row coordinates
     * @param col line coordinates
     * @return
     */
    public boolean isFree(int row, int col) {
        return this.dataBoard[row][col] == Cell.EMPTY;
    }

    /**
     * used in test class
     * @return row of a cell
     */
    public int getRowPos() {
        for (int i = 1; i < ROW_SIZE; i++) {
            for (int j = 1; j < COL_SIZE; j++) {
                if (this.dataBoard[i][j] != Cell.EMPTY) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @param row row coordinates
     * @param col col coordinates
     * @return the dataBoard value
     */
    public Cell getCell(int row, int col) {
        return dataBoard[row][col];
    }


    /**
     * sets the Score number when a player wins
     */
    private void setScoreNumber() {
        this.score = (this.ROW_SIZE * this.COL_SIZE) - this.turnCounter;
    }

    public int getRowSize() {
        return this.ROW_SIZE;
    }

    public int getColSize() {
        return this.COL_SIZE;
    }


    public int getScore() {
        return this.score;
    }

    public int getPlayer() {
        return this.player;
    }



    public List<Plays> getMovesRedo() {
        return this.movesRedo;
    }
}
