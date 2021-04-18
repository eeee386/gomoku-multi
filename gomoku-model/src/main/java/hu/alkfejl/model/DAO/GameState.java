package hu.alkfejl.model.DAO;


import hu.alkfejl.model.AIPlayer;
import hu.alkfejl.model.Game;
import hu.alkfejl.model.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GameState {
    private Integer id;
    private String player1Name;
    private String player2Name;
    private boolean isPlayer1Active;
    private boolean isPlayer2AI;
    private Integer remainingTime;
    private Integer turnTime;
    private Integer remainingTurnTime;
    private char[][] boardState;

    public GameState(Game game) {
        id = game.getId();
        player1Name = game.getPlayer1().getName();
        player2Name = game.getPlayer2().getName();
        isPlayer1Active = game.getActivePlayer().getName().equals(game.getPlayer1().getName());
        isPlayer2AI = game.getPlayer2() instanceof AIPlayer;
        remainingTime = game.getRemainingTime();
        remainingTurnTime = game.getRemainingTurnTime();
        boardState = game.getBoard().getBoardState();
        turnTime = game.getTurnTimeInSeconds();
    }

    public GameState(int id, String player1Name, String player2Name, boolean isPlayer1Active, boolean isPlayer2AI, Integer remainingTime, Integer remainingTurnTime, char[][] boardState, Integer turnTime) {
        this.id = id;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.isPlayer1Active = isPlayer1Active;
        this.isPlayer2AI = isPlayer2AI;
        this.remainingTime = remainingTime;
        this.remainingTurnTime = remainingTurnTime;
        this.boardState = boardState;
        this.turnTime = turnTime;
    }

    public GameState(GameStateBean gsb) {
        this.id = gsb.getId();
        this.player1Name = gsb.getPlayer1Name();
        this.player2Name = gsb.getPlayer2Name();
        this.isPlayer1Active = gsb.isIsPlayer1Active();
        this.isPlayer2AI = gsb.isIsPlayer2AI();
        this.boardState = stringToBoard(gsb.getBoardState());
        this.turnTime = Utils.getSecondsFromFormattedString(gsb.getTurnTime());
        this.remainingTurnTime = Utils.getSecondsFromFormattedString(gsb.getRemainingTurnTime());
        this.remainingTime = Utils.getSecondsFromFormattedString(gsb.getRemainingTime());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public boolean isPlayer1Active() {
        return isPlayer1Active;
    }

    public void setPlayer1Active(boolean player1Active) {
        isPlayer1Active = player1Active;
    }

    public boolean isPlayer2AI() {
        return isPlayer2AI;
    }

    public void setPlayer2AI(boolean player2AI) {
        isPlayer2AI = player2AI;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Integer remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Integer getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(Integer turnTime) {
        this.turnTime = turnTime;
    }

    public Integer getRemainingTurnTime() {
        return remainingTurnTime;
    }

    public void setRemainingTurnTime(Integer remainingTurnTime) {
        this.remainingTurnTime = remainingTurnTime;
    }

    public char[][] getBoardState() {
        return boardState;
    }

    public void setBoardState(char[][] boardState) {
        this.boardState = boardState;
    }

    public String getBoardStateAsString() throws IOException {
        return Arrays.stream(boardState)
                .map(String::new)
                .collect(Collectors.joining(";"));

    }

    public static char[][] stringToBoard(String s) {
        String[] sarr = s.split(";");
        char[][] CArr = new char[sarr.length][sarr[0].length()];
        for (int i = 0; i < sarr.length; i++) {
            CArr[i] = sarr[i].toCharArray();
        }
        return CArr;
    }

    public GameStateBean createBean() {
        GameStateBean bean = new GameStateBean();
        bean.setId(id == null ? -1 : id);
        try {
            bean.setBoardState(getBoardStateAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bean.setPlayer1Name(player1Name);
        bean.setPlayer2Name(player2Name);
        bean.setIsPlayer1Active(isPlayer1Active);
        bean.setIsPlayer2AI(isPlayer2AI);
        bean.setRemainingTime(Utils.formatDuration(remainingTime));
        bean.setTurnTime(Utils.formatDuration(turnTime));
        bean.setRemainingTurnTime(Utils.formatDuration(remainingTurnTime));
        return bean;
    }
}
