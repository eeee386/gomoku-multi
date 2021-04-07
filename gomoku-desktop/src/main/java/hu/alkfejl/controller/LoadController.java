package hu.alkfejl.controller;

import hu.alkfejl.model.DAO.GameState;
import hu.alkfejl.model.DAO.GameStateBean;
import hu.alkfejl.model.DAO.GameStateDAO;
import hu.alkfejl.model.Game;
import hu.alkfejl.model.exception.BoardSizeException;
import hu.alkfejl.model.exception.DatabaseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadController {

    private GameStateDAO dao = new GameStateDAO();

    @FXML
    TableView<GameStateBean> gameTable;

    @FXML
    TableColumn<GameStateBean, Integer> idColumn;

    @FXML
    TableColumn<GameStateBean, String> player1Column;

    @FXML
    TableColumn<GameStateBean, String> player2Column;

    @FXML
    TableColumn<GameStateBean, String> activePlayerColumn;

    @FXML
    TableColumn<GameStateBean, Boolean> againstAIColumn;

    @FXML
    TableColumn<GameStateBean, String> remainingTimeColumn;

    @FXML
    TableColumn<GameStateBean, String> turnTimeColumn;

    @FXML
    TableColumn<GameStateBean, String> remainingTurnTimeColumn;

    @FXML
    Label errorLabel;

    @FXML
    Button goBack;

    private void refreshTable(){
        try {
            gameTable.getItems().setAll(dao.listAllAsBeans());
        } catch (DatabaseException e){
            errorLabel.setText("List all failed");
        }
    }

    public void initialize() {
        refreshTable();
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        player1Column.setCellValueFactory(e -> e.getValue().player1NameProperty());
        player2Column.setCellValueFactory(e -> e.getValue().player2NameProperty());
        activePlayerColumn.setCellValueFactory(e -> e.getValue().isIsPlayer1Active() ? e.getValue().player1NameProperty() : e.getValue().player2NameProperty());
        againstAIColumn.setCellValueFactory(e-> e.getValue().isPlayer2AIProperty());
        remainingTimeColumn.setCellValueFactory(e-> e.getValue().remainingTimeProperty());
        turnTimeColumn.setCellValueFactory(e -> e.getValue().turnTimeProperty());
        remainingTurnTimeColumn.setCellValueFactory(e -> e.getValue().remainingTurnTimeProperty());
    }

    @FXML
    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/choose.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
        Stage stage2 = (Stage) errorLabel.getScene().getWindow();
        stage2.hide();
    }

    @FXML
    public void onLoad() throws BoardSizeException, IOException {
        GameStateBean selectedItem = gameTable.getSelectionModel().getSelectedItems().get(0);
        Game game = new Game(new GameState(selectedItem));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/board.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        BoardController boardController = loader.getController();
        boardController.prepGame(game);
        stage.show();
        Stage stage2 = (Stage) gameTable.getScene().getWindow();
        stage2.hide();
    }

    @FXML
    public void onDelete() {
        //TODO: Add Modal
        GameStateBean selectedItem = gameTable.getSelectionModel().getSelectedItems().get(0);
        try {
            dao.delete(selectedItem.getId());
        } catch (DatabaseException e){
            e.printStackTrace();
            errorLabel.setText("Delete failed");
        }
        refreshTable();
    }
}
