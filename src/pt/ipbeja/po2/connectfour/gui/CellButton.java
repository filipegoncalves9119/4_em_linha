package pt.ipbeja.po2.connectfour.gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Filipe Gonçalves (6050)
 * @version 09/04/2019
 */
public class CellButton extends Button {

    private static final Image PLAY_EMPTY = new Image("/resources/empty.png");
    private static final Image PLAYER_RED = new Image("/resources/player1.png");
    private static final Image PLAYER_GREEN = new Image("/resources/player2.png");
    private int row, col;
    private ImageView imageView;


    /**
     * Contructor of the class
     * @param row row coordiantes
     * @param col col coordiantes
     */
    public CellButton(int row, int col){
        this.row = row;
        this.col = col;
        imageView = new ImageView(PLAY_EMPTY);
        this.setGraphic(imageView);
    }

    /**
     * set image for player1 (red player)
     */
    public void setRed(){
        this.imageView.setImage(PLAYER_RED);
    }

    /**
     * set images for player2 (green player)
     */
    public void setGreen(){
        this.imageView.setImage(PLAYER_GREEN);
    }

    public void setEmpty(){
        this.imageView.setImage(PLAY_EMPTY);
    }

    /**
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return col
     */
    public int getCol() {
        return col;
    }
}
