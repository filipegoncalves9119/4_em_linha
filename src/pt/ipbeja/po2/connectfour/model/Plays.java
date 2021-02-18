package pt.ipbeja.po2.connectfour.model;

/**
 * @author Filipe Gonçalves (6050)
 * @version 05/05/2019
 */
public class Plays {

    private int row;
    private int col;
    private Cell cell;

    public Plays(int row, int col, Cell cell) {
        this.row = row;
        this.col = col;
        this.cell = cell;

    }

    public Cell getCell(){
        return cell;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    @Override
    public String toString() {
        return "(" + this.row + "," + this.col + ")";
    }
}
