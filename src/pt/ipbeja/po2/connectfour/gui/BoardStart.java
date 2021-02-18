package pt.ipbeja.po2.connectfour.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Filipe Gonçalves (6050)
 * @version 09/04/2019
 */
public class BoardStart extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        Board board = new Board();
        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
