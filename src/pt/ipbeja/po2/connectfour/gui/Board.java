package pt.ipbeja.po2.connectfour.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pt.ipbeja.po2.connectfour.View;
import pt.ipbeja.po2.connectfour.model.Cell;
import pt.ipbeja.po2.connectfour.model.ConnectFourModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Filipe Gonçalves (6050)
 * @version 09/04/2019
 */
public class Board extends VBox implements View {

    private int row, col;
    private ConnectFourModel gameModel;
    private CellButton[][] buttons;
    private GridPane gridpane = new GridPane();
    private List<String> highScore = new ArrayList<>(3);
    private MenuBar menuBar;
    private MenuItem newGame, undo, redo;
    private CheckMenuItem checkMenuItem;
    private MenuHandler menuHandler;
    private int indice;

    /**
     * Class constructor
     */
    public Board() {
        this.menuHandler= new MenuHandler();
        final Menu menu = new Menu("Game");
        final Menu menu2 = new Menu("Moves");
        this.menuBar = new MenuBar(menu, menu2);
        this.newGame = new MenuItem("New game");
        this.undo = new MenuItem("Undo");
        this.redo = new MenuItem("Redo");
        this.checkMenuItem = new CheckMenuItem("Pop Out");
        menu.getItems().add(this.newGame);
        menu2.getItems().addAll(this.undo, this.redo, this.checkMenuItem);

        this.gameModel = new ConnectFourModel(this);
        this.drawBoard();
        this.getChildren().addAll(menuBar, gridpane);

        this.newGame.setOnAction(this.menuHandler);
        this.undo.setOnAction(this.menuHandler);
        this.redo.setOnAction(this.menuHandler);
    }


    /**
     * Draws the board, according to the Array of arrays from CoonectFourModel class by copying it to a new array of arrays.
     */
    private void updateBoard() {
        for (int i = 0; i < this.gameModel.getRowSize(); i++) {
            for (int j = 0; j < this.gameModel.getColSize(); j++) {
                if (this.gameModel.getCell(i, j) == Cell.PLAYER1) {
                    this.buttons[i][j].setRed();
                } else if (this.gameModel.getCell(i, j) == Cell.PLAYER2) {
                    this.buttons[i][j].setGreen();
                }else if(this.gameModel.getCell(i,j) == Cell.EMPTY) {
                    this.buttons[i][j].setEmpty();
                }
            }
        }
    }

    /**
     * Creates the game  board with a grid of CellButtons
     */
    private void drawBoard() {
        ConnectInFourHandler buttonHandler = new ConnectInFourHandler();
        this.buttons = new CellButton[this.gameModel.getRowSize()][this.gameModel.getColSize()];

        for (int row = 0; row < this.gameModel.getRowSize(); row++) {
            for (int col = 0; col < this.gameModel.getColSize(); col++) {
                CellButton btn = new CellButton(row, col);
                btn.setOnAction(buttonHandler);
                this.buttons[row][col] = btn;
                this.gridpane.add(btn, col, row);

            }
        }
    }


    /**
     * Click handler for the CellButtons
     */
    class ConnectInFourHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            CellButton button = (CellButton) event.getSource();
            row = button.getRow();
            col = button.getCol();

            if (gameModel.isFree(row, col)) {
                gameModel.rollDown(row, col);
                updateBoard();

                if (gameModel.winningCondition(col)) {
                    gridpane.setDisable(true);
                }
            }
            if(!gameModel.isFree(row,col)){
                popOut(col);
            }

        }
    }

    /**
     * Handler for the menu items
     */
    class MenuHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            MenuItem item = (MenuItem) event.getSource();
            if (item.equals(newGame)) {
                resetGame();
            }
            if (item.equals(undo)) {
                gameModel.undo();
            }
            if (item.equals(redo)) {
                gameModel.redo();
            }
        }
    }

    /**
     * Win alert
     * Sort the highscores
     */
    @Override
    public void playerWins() {
        this.updateBoard();
        String name = getName();
        int scores = this.gameModel.getScore();
        this.indice = -1;

        if (this.highScore.size() > 0) {
            for (int i = 0; i < this.highScore.size(); i++) {
                if (scores > Integer.parseInt(this.highScore.get(i).split("--")[1])) {
                    this.highScore.add(i, name + "--" + scores + " pontos");
                    if (this.highScore.size() > 3)
                        this.highScore.remove(highScore.size() - 1);
                    this.indice = i;
                    i = this.highScore.size() + 1;

                } else if (i == this.highScore.size() - 1 && this.highScore.size() < 3) {
                    this.highScore.add(name + "--" + scores + " pontos");
                    this.indice = this.highScore.size() - 1;
                    i = this.highScore.size() + 1;
                }
            }
        } else {
            this.highScore.add(0, name + "--" + scores + " pontos");
            this.indice = this.highScore.size() - 1;
        }
    }


    /**
     * Draw alert
     */
    @Override
    public void gameDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Draw game!");
        alert.show();
    }

    /**
     * shows the highScores in a form of an alert
     */
    @Override
    public void highScores() {
        String s = "";
        for (int i = 0; i < this.highScore.size(); i++) {
            s += this.highScore.get(i);
            if (i == this.indice) {
                s += "***";
            }
            s+="\n";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.show();

    }

    /**
     * Method called to undo a play
     * @param row coordinate
     * @param col coordinate
     */
    @Override
    public void undo(int row, int col) {
        CellButton btn = this.buttons[row][col];
        btn.setEmpty();
    }

    /**
     * Method called to redo a play
     * @param row coordinate
     * @param col coordinate
     */
    @Override
    public void redo(int row, int col) {
        CellButton btn = this.buttons[row][col];
        Cell cell = this.gameModel.getMovesRedo().get(this.gameModel.getMovesRedo().size() - 1).getCell();
        if (cell == Cell.PLAYER1) {
            btn.setRed();
        }
        if (cell == Cell.PLAYER2) {
            btn.setGreen();
        }
    }

    /**
     * Restart the Gridpane
     */
    private void resetGame() {

        this.getChildren().remove(this.gridpane);

        this.gridpane = new GridPane();
        this.gameModel = new ConnectFourModel(this);
        this.drawBoard();
        this.getChildren().addAll(this.gridpane);
    }


    /**
     * Ask the winner for a name
     * verify if name is valid(can't be greater than 8 digits)
     */
    private String getName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("FourInLine");
        dialog.setHeaderText("Congratulations!");
        dialog.setContentText("You won player " + this.gameModel.getPlayer() + " please type your name");
        Optional<String> name = dialog.showAndWait();
        String hasName = dialog.getResult();

        if (name.isPresent()) {
            while (hasName.length() > 8) {
                dialog.showAndWait();
                hasName = dialog.getResult();
            }
        }
        return hasName;
    }

    /**
     * Method used to set true the PopOut option
     * @param col coordinate
     */
    private void popOut(int col){
        if(this.checkMenuItem.isSelected()){
            this.gameModel.poppingOut(col);
            this.updateBoard();
            if(this.gameModel.winningCondition(col)){
                this.gridpane.setDisable(true);
            }
        }
    }


}

