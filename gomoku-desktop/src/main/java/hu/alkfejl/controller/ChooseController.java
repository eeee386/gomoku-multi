package hu.alkfejl.controller;

import hu.alkfejl.model.*;
import hu.alkfejl.model.exception.BoardSizeException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseController {

    @FXML
    TextField playerOneName;

    @FXML
    TextField playerTwoName;

    @FXML
    TextField boardHeight;

    @FXML
    TextField boardWidth;

    @FXML
    CheckBox againstAI;

    @FXML
    Label errorLabel;

    @FXML
    TextField playTime;

    @FXML
    TextField turnTime;

    @FXML
    private void onStart() throws IOException {
        try {
            Player player1 = new Player(playerOneName.getText(), true, PlayerSign.X);
            Player player2 = againstAI.isSelected() ? new AIPlayer(playerTwoName.getText(), false, PlayerSign.O) : new Player(playerTwoName.getText(), false, PlayerSign.O);
            Board board = new Board(Integer.parseInt(boardWidth.getText()), Integer.parseInt(boardHeight.getText()));
            Double playTimeInMinutes = "".equals(playTime.getText()) ? null : Double.parseDouble(playTime.getText());
            Double turnTimeInMinutes = "".equals(turnTime.getText()) ? null : Double.parseDouble(turnTime.getText());
            Game game = new Game(player1, player2, board, playTimeInMinutes, turnTimeInMinutes);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/board.fxml"));
            loader.load();
            Parent p = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            BoardController boardController = loader.getController();
            boardController.prepGame(game);
            stage.show();
            Stage stage2 = (Stage) boardHeight.getScene().getWindow();
            stage2.hide();
        } catch (BoardSizeException e){
            errorLabel.setText(e.getMessage());
            errorLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    public void onLoad() throws IOException {
        loadNew("load", boardHeight);
    }

    private void loadNew(String fxmlName, Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/" + fxmlName + ".fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
        Stage stage2 = (Stage) node.getScene().getWindow();
        stage2.hide();
    }

}
