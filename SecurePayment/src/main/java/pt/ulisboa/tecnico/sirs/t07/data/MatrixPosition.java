package pt.ulisboa.tecnico.sirs.t07.data;

/**
 * Created by trosado on 05/12/16.
 */
public class MatrixPosition {
    private String row;
    private Integer col;
    private Integer pos;

    private MatrixPosition(){}

    public MatrixPosition(String row, Integer col, Integer pos){
        this.col=col;
        this.row=row;
        this.pos=pos;
    }

    public String getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getPos() {
        return pos;
    }
}
