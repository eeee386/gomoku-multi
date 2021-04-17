package hu.alkfejl.model;

import hu.alkfejl.model.DAO.GameState;
import hu.alkfejl.model.exception.BoardSizeException;
import hu.alkfejl.model.exception.IllegalMoveException;
import hu.alkfejl.model.exception.PlayerException;

public class Game {
    private Integer id = null;
    private final Player player1;
    private final Player player2;
    private final Board board;
    private final Integer allTimeInSeconds;
    private final Integer turnTimeInSeconds;
    private Integer remainingTime;
    private Integer remainingTurnTime;

    public Game(Player player1, Player player2, Board board, Double allTimeInMinutes, Double turnTimeInMinutes) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.allTimeInSeconds = allTimeInMinutes == null ? null : (int) (60 * allTimeInMinutes);
        this.turnTimeInSeconds = turnTimeInMinutes == null ? null : (int) (60*turnTimeInMinutes);
        this.remainingTime = this.allTimeInSeconds;
        this.remainingTurnTime = this.turnTimeInSeconds;
    }

    public Game(GameState gs) throws BoardSizeException {
        this.id = gs.getId();
        this.player1 = new Player(gs.getPlayer1Name(), gs.isPlayer1Active(), PlayerSign.X);
        this.player2 = gs.isPlayer2AI() ? new AIPlayer(gs.getPlayer2Name(), !gs.isPlayer1Active(), PlayerSign.O) : new Player(gs.getPlayer2Name(), !gs.isPlayer1Active(), PlayerSign.O);
        this.board = new Board(gs.getBoardState());
        this.allTimeInSeconds = gs.getRemainingTime();
        this.turnTimeInSeconds = gs.getTurnTime();
        this.remainingTurnTime = gs.getRemainingTurnTime();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Board getBoard() {
        return board;
    }

    public Player getActivePlayer(){
        return this.player1.isActive() ? this.player1 : this.player2;
    }

    public Player getNonActivePlayer(){
        return this.player1.isActive() ? this.player2 : this.player1;
    }

    public Integer getAllTimeInSeconds() {
        return allTimeInSeconds;
    }

    public Integer getTurnTimeInSeconds() {
        return turnTimeInSeconds;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Integer remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Integer getRemainingTurnTime() {
        return remainingTurnTime;
    }

    public void setRemainingTurnTime(Integer remainingTurnTime) {
        this.remainingTurnTime = remainingTurnTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void swapActivePlayer(){
        if(this.player1.isActive()){
            this.player1.setActive(false);
            this.player2.setActive(true);
        } else {
            this.player1.setActive(true);
            this.player2.setActive(false);
        }
    }

    public void playTurn(int height, int width) throws IllegalMoveException {
        this.board.setBoardValue(height, width, getActivePlayer().getSign());
    }

    public boolean hasSomebodyWon(){
        return this.board.isWin(getActivePlayer().getSign());
    }

    public Pair<Integer, Integer> handleAIPlayer(Player player) throws PlayerException, IllegalMoveException{
        Pair<Integer, Integer> move = null;
        if(player instanceof AIPlayer){
            do{
                move = ((AIPlayer) player).move(board.getHeight(), board.getWidth());
            } while (this.board.isPlaceSet(move.getKey(), move.getValue()));
            playTurn(move.getKey(), move.getValue());
            return move;
        } else {
            throw new PlayerException("This is not an AIPlayer");
        }
    }


}
