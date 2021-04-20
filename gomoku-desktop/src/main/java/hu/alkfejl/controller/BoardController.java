package hu.alkfejl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hu.alkfejl.model.*;
import hu.alkfejl.model.DAO.GameState;
import hu.alkfejl.model.DAO.GameStateDAO;
import hu.alkfejl.model.exception.DatabaseException;
import hu.alkfejl.model.exception.IllegalMoveException;
import hu.alkfejl.model.exception.PlayerException;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BoardController {

    @FXML
    VBox boardWrapper;

    @FXML
    Label activePlayer;

    @FXML
    Label errorLabel;

    @FXML
    Label activeWinner;

    @FXML
    Label timerLabel;

    @FXML
    Label turnTimerLabel;

    @FXML
    Label remainingTime;

    @FXML
    Label remainingTurnTime;

    private Timer playTimer;
    private Timer turnTimer;

    private Game game;


    public void prepGame(Game game){
        setGame(game);
        setBoard();
        setPlayTimer();
        setTurnTimer();
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public void setPlayTimer() {
        if(game.getRemainingTime() == null){
            return;
        }
        playTimer = new Timer();
        playTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(game.getRemainingTime() > 0)
                {
                    Platform.runLater(() -> timerLabel.setText(Utils.formatDuration(game.getRemainingTime())));
                    System.out.println(game.getRemainingTime());
                    game.setRemainingTime(game.getRemainingTime()-1);
                }
                else{
                    Platform.runLater(() -> setEndedGame(null));
                    playTimer.cancel();
                    if(turnTimer != null)turnTimer.cancel();

                }
            }
        }, 1000,1000);
    }

    public void setTurnTimer() {
        if(game.getRemainingTurnTime() == null){
            return;
        }
        turnTimer = new Timer();
        turnTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(game.getRemainingTurnTime() > 0)
                {
                    Platform.runLater(() -> turnTimerLabel.setText(Utils.formatDuration(game.getRemainingTurnTime())));
                    System.out.println(game.getRemainingTurnTime());
                    game.setRemainingTurnTime(game.getRemainingTurnTime()-1);
                }
                else{
                    Platform.runLater(() -> setEndedGame(game.getNonActivePlayer()));
                    turnTimer.cancel();
                    if(playTimer != null)playTimer.cancel();

                }
            }
        }, 1000,1000);
    }

    private void setEndedGame(Player playerWhoWon){
        activeWinner.setText(playerWhoWon == null ? "Draw" : "Winner: ");
        activePlayer.setText(playerWhoWon == null ? "" : playerWhoWon.getName());
        for (Node h : boardWrapper.getChildren()) {
            for (Node b : ((HBox) h).getChildren()) {
                Button bu = (Button) b;
                bu.setDisable(true);
            }
        }
    }

    private void handleAIPlayer() throws IllegalMoveException {
        try {
            PlayerSign activeSign = game.getActivePlayer().getSign();
            Pair<Integer, Integer> move = game.handleAIPlayer(game.getActivePlayer());
            HBox h = (HBox)boardWrapper.getChildren().get(move.getKey());
            Button b = (Button)h.getChildren().get(move.getValue());
            System.out.println(b);
            b.setText(Character.toString(activeSign.getValue()));
            if(game.hasSomebodyWon()){
                this.setEndedGame(game.getActivePlayer());
            } else {
                game.swapActivePlayer();
            }
        } catch (PlayerException e){
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void clickOnCell(Event event) {
        errorLabel.setText("");
        try {
            PlayerSign activeSign = game.getActivePlayer().getSign();
            Button clickedButton = (Button) event.getSource();
            String[] props = clickedButton.idProperty().getValue().split(":");
            game.playTurn(Integer.parseInt(props[0]), Integer.parseInt(props[1]));
            clickedButton.setText(Character.toString(activeSign.getValue()));
            if(game.hasSomebodyWon()){
                this.setEndedGame(game.getActivePlayer());
            } else {
                game.swapActivePlayer();
                if(game.getActivePlayer() instanceof AIPlayer){
                    handleAIPlayer();
                }
                activePlayer.setText(game.getActivePlayer().getName());
            }
            game.setRemainingTurnTime(game.getTurnTimeInSeconds());
        } catch (IllegalMoveException e){
            errorLabel.setText(e.getMessage());
            errorLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    public void onFinishGame(ActionEvent event) throws IOException{
        this.game = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/choose.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
        Node target = (Node) event.getTarget();
        Stage stage2 = (Stage) target.getScene().getWindow();
        stage2.hide();
    }

    @FXML
    public void onSaveState() {
        GameStateDAO dao = new GameStateDAO();
        GameState gs = new GameState(game);

        try {
            gs = dao.insertOrUpdate(gs);
            if(game.getId() == null){
                game.setId(gs.getId());
            }
        } catch (DatabaseException e){
            errorLabel.setText("Could not save data");
        }
    }

    private void setBoard(){
        int height = game.getBoard().getHeight();
        int width = game.getBoard().getWidth();
        ArrayList<HBox> hboxes = new ArrayList<>(height);
        for(int h = 0; h < height; h++){
            HBox addableHBox = new HBox();
            ArrayList<Button> buttons = new ArrayList<>();
            for(int w = 0; w < width; w++){
                Button addableButton = new Button();
                addableButton.idProperty().setValue(h+ ":" + w);

                addableButton.setText(String.valueOf(game.getBoard().getBoardState()[h][w]));
                addableButton.setOnAction(this::clickOnCell);
                addableButton.setMinWidth(30);
                addableButton.setMinHeight(30);
                buttons.add(addableButton);
            }
            addableHBox.getChildren().addAll(buttons);
            hboxes.add(addableHBox);
        }
        boardWrapper.getChildren().addAll(hboxes);
        activePlayer.setText(game.getActivePlayer().getName());
        if(game.getAllTimeInSeconds() == null){
            remainingTime.setText("");
        }
        if(game.getTurnTimeInSeconds() == null){
            remainingTurnTime.setText("");
        }
    }
}