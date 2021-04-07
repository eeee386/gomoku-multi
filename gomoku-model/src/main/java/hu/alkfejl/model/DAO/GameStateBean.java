package hu.alkfejl.model.DAO;

import javafx.beans.property.*;

public class GameStateBean {
    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty player1Name = new SimpleStringProperty(this, "player1Name");
    private StringProperty player2Name = new SimpleStringProperty(this, "player2Name");
    private BooleanProperty isPlayer1Active = new SimpleBooleanProperty(this, "isPlayer1Active");
    private BooleanProperty isPlayer2AI = new SimpleBooleanProperty(this, "isPlayer2AI");
    private StringProperty remainingTime = new SimpleStringProperty(this, "remainingTime");
    private StringProperty turnTime = new SimpleStringProperty(this, "turnTime");
    private StringProperty remainingTurnTime = new SimpleStringProperty(this, "remainingTurnTime");
    private StringProperty boardState = new SimpleStringProperty(this, "boardState");

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getPlayer1Name() {
        return player1Name.get();
    }

    public StringProperty player1NameProperty() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name.set(player1Name);
    }

    public String getPlayer2Name() {
        return player2Name.get();
    }

    public StringProperty player2NameProperty() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name.set(player2Name);
    }

    public boolean isIsPlayer1Active() {
        return isPlayer1Active.get();
    }

    public BooleanProperty isPlayer1ActiveProperty() {
        return isPlayer1Active;
    }

    public void setIsPlayer1Active(boolean isPlayer1Active) {
        this.isPlayer1Active.set(isPlayer1Active);
    }

    public boolean isIsPlayer2AI() {
        return isPlayer2AI.get();
    }

    public BooleanProperty isPlayer2AIProperty() {
        return isPlayer2AI;
    }

    public void setIsPlayer2AI(boolean isPlayer2AI) {
        this.isPlayer2AI.set(isPlayer2AI);
    }

    public String getRemainingTime() {
        return remainingTime.get();
    }

    public StringProperty remainingTimeProperty() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime.set(remainingTime);
    }

    public String getTurnTime() {
        return turnTime.get();
    }

    public StringProperty turnTimeProperty() {
        return turnTime;
    }

    public void setTurnTime(String turnTime) {
        this.turnTime.set(turnTime);
    }

    public String getRemainingTurnTime() {
        return remainingTurnTime.get();
    }

    public StringProperty remainingTurnTimeProperty() {
        return remainingTurnTime;
    }

    public void setRemainingTurnTime(String remainingTurnTime) {
        this.remainingTurnTime.set(remainingTurnTime);
    }

    public String getBoardState() {
        return boardState.get();
    }

    public StringProperty boardStateProperty() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState.set(boardState);
    }
}
