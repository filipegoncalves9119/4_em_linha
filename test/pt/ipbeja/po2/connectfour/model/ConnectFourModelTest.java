package pt.ipbeja.po2.connectfour.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipbeja.po2.connectfour.View;
import pt.ipbeja.po2.connectfour.ViewTest;
import pt.ipbeja.po2.connectfour.gui.CellButton;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Filipe Gonçalves (6050)
 * @version 02/05/2019
 */
class ConnectFourModelTest {

    private ConnectFourModel connectFourModel;
    private View view;

    @BeforeEach
    void setUp() {
        this.view = new ViewTest();
        this.connectFourModel = new ConnectFourModel(view);

    }

    @Test
    void teste01() {
        connectFourModel.rollDown(0, 2);
        assertEquals(5, connectFourModel.getRowPos());

    }

    @Test
    void teste02() {

        connectFourModel.rollDown(0, 3);
        connectFourModel.rollDown(0, 3);
        connectFourModel.rollDown(0, 3);
        assertEquals(3, connectFourModel.getRowPos());


    }

    @Test
    void teste03() {

        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 1);

        boolean isEmpty = this.connectFourModel.isFree(0, 1);
        assertFalse(isEmpty);

    }

    @Test
    void teste04() {

        connectFourModel.rollDown(0, 0);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 2);
        connectFourModel.rollDown(0, 3);
        connectFourModel.rollDown(0, 4);
        connectFourModel.rollDown(0, 5);
        connectFourModel.rollDown(0, 6);
        boolean isDraw = this.connectFourModel.winningCondition(3);

        assertFalse(isDraw);


    }

    @Test
    void teste05() {
        connectFourModel.rollDown(0, 0);
        connectFourModel.rollDown(0, 0);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 2);
        connectFourModel.rollDown(0, 2);
        connectFourModel.rollDown(0, 3);

        boolean isLineWin = connectFourModel.hasWinningLine();

        assertTrue(isLineWin);

    }

    @Test
    void teste06() {
        connectFourModel.rollDown(0, 0);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 0);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 0);
        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 0);

        boolean isColWin = connectFourModel.hasWinningColumn(0);

        assertTrue(isColWin);

    }

    @Test
    void teste07() {

        connectFourModel.rollDown(0, 1);
        connectFourModel.rollDown(0, 2);
        connectFourModel.rollDown(0, 2);
        connectFourModel.rollDown(0, 3);
        connectFourModel.rollDown(0, 4);
        connectFourModel.rollDown(0, 3);
        connectFourModel.rollDown(0, 3);
        connectFourModel.rollDown(0, 4);
        connectFourModel.rollDown(0, 5);
        connectFourModel.rollDown(0, 4);
        connectFourModel.rollDown(0, 4);

        boolean isDiagWin = connectFourModel.hasWinningDiagonal();

        assertTrue(isDiagWin);

    }

    @Test
    void teste08() {

        int counter = 0;
        int col = 3;

        for (int j = 0; j < 3; j++) {
            for (int i = (connectFourModel.getRowSize() - 1); i >= 0; i--) {
                connectFourModel.updateBoardState(i, j);
            }
        }
        for (int j = 4; j < 7; j++) {
            for (int i = (connectFourModel.getRowSize() - 1); i >= 0; i--) {
                if (counter < 17) {
                    connectFourModel.updateBoardState(i, j);
                    counter++;
                }
            }
        }
        for (int i = (connectFourModel.getRowSize() - 1); i >= 0; i--) {
            connectFourModel.updateBoardState(i, col);
        }

        connectFourModel.rollDown(0, 6);

        boolean isDraw = connectFourModel.isDraw(6);
        assertTrue(isDraw);

    }

}