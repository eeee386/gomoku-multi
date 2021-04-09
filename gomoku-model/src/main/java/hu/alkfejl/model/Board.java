package hu.alkfejl.model;


import hu.alkfejl.model.exception.BoardSizeException;
import hu.alkfejl.model.exception.IllegalMoveException;

public class Board {
    private final int width;
    private final int height;
    private final char[][] boardState;

    public Board(int width, int height) throws BoardSizeException {
        this.width = width;
        this.height = height;
        if(width < 10 || height < 10){
            throw new BoardSizeException("Board too small, it has to be at least 10x10");
        }
        this.boardState = new char[height][width];

    }

    public Board(char[][] boardState){
        this.width = boardState[0].length;
        this.height = boardState.length;
        this.boardState = boardState;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getBoardState() {
        return boardState;
    }

    public void setBoardValue(int hIndex, int wIndex, PlayerSign sign) throws IllegalMoveException {
        if(hIndex > this.height || wIndex > this.width || hIndex < 0 || wIndex < 0){
            throw new IllegalMoveException("No such place on the board");
        }
        if(isPlaceSet(hIndex, wIndex)){
            throw new IllegalMoveException("Place is already set!");
        } else {
            this.boardState[hIndex][wIndex] = sign.getValue();
        }
    }

    public boolean isWin(PlayerSign sign){
        for(int hh = 0; hh < this.height; hh++){
            for(int wh = 4; wh < this.width; wh++){
                boolean flagHorizontal = true;
                for(int ih = 0; ih < 5; ih++){
                    flagHorizontal &= this.boardState[hh][wh-ih] == sign.getValue();
                }
                if(flagHorizontal){
                    return true;
                }
            }
        }
        for(int hv = 4; hv < this.height; hv++){
            for(int wv = 0; wv < this.width; wv++){
                boolean flagVertical = true;
                for(int iv = 0; iv < 5; iv++){
                    flagVertical &= this.boardState[hv-iv][wv] == sign.getValue();
                }
                if(flagVertical){
                    return true;
                }
            }
        }
        for(int hud = 4; hud < this.height; hud++){
            for(int wud = 4; wud < this.width; wud++){
                boolean flagUpperDiagonal = true;
                for(int iud = 0; iud < 5; iud++){
                    flagUpperDiagonal &= this.boardState[hud-iud][wud-iud] == sign.getValue();
                }
                if(flagUpperDiagonal){
                    return true;
                }
            }
        }
        for(int hld = 0; hld < this.height-4; hld++){
            for(int wld = 4; wld < this.width; wld++){
                boolean flagLowerDiagonal = true;
                for(int ild = 0; ild < 5; ild++){
                    flagLowerDiagonal &= this.boardState[hld+ild][wld-ild] == sign.getValue();
                }
                if(flagLowerDiagonal){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPlaceSet(int hIndex, int wIndex){
        return this.boardState[hIndex][wIndex] == PlayerSign.X.getValue() || this.boardState[hIndex][wIndex] == PlayerSign.O.getValue();
    }

    public void cleanUpBoard(){
        for(int h = 0; h < height; h++){
            for(int w = 0; w < width; w++){
                if(this.boardState[h][w] != PlayerSign.O.getValue() && this.boardState[h][w] != PlayerSign.X.getValue()){
                    this.boardState[h][w] = ' ';
                }
            }
        }
    }
}
