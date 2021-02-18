package pt.ipbeja.po2.connectfour;

/**
 * @author Filipe Gonçalves (6050)
 * @version 09/04/2019
 */
public interface View {

    void playerWins();

    void gameDraw();

    void highScores();


    void undo(int row, int col);

    void redo(int row, int col);
}
