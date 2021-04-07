package hu.alkfejl.model.DAO;

import hu.alkfejl.model.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameStateDAO implements DAOInterface {
    static String SELECT_ALL = "SELECT * FROM game_state";
    static String INSERT_NEW = "INSERT INTO game_state (player1Name, player2Name, isPlayer1Active, isPlayer2AI, boardState, remainingTime, remainingTurnTime, turnTime) VALUES (?,?,?,?,?,?,?,?)";
    static String UPDATE = "UPDATE game_state SET player1Name=?, player2Name=?, isPlayer1Active=?, isPlayer2AI=?, boardState=?, remainingTime=?, remainingTurnTime=?, turnTime=? WHERE gameID=?";
    static String DELETE = "DELETE FROM game_state WHERE gameID=?";
    private final String connectionURL;


    public GameStateDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionURL = ConfigHelper.getValue("db.url"); // obtaining DB URL
    }

    @Override
    public List<GameState> listAll() throws DatabaseException {
        List<GameState> gameStateList = new ArrayList<>();
        try(Connection c = DriverManager.getConnection(connectionURL);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL)
        ) {
            while(rs.next()){
                GameState gs = new GameState(
                        rs.getInt("gameID"),
                        rs.getString("player1Name"),
                        rs.getString("player2Name"),
                        rs.getInt("isPlayer1Active") > 0,
                        rs.getInt("isPlayer2AI") > 0,
                        handleSelectNullableInteger(rs, "remainingTime"),
                        handleSelectNullableInteger(rs, "remainingTurnTime"),
                        GameState.stringToBoard(rs.getString("boardState")),
                        handleSelectNullableInteger(rs, "turnTime")
                );
                gameStateList.add(gs);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }
        return gameStateList;
    }

    @Override
    public List<GameStateBean> listAllAsBeans() throws DatabaseException {
        return this.listAll().stream().map(GameState::createBean).collect(Collectors.toList());
    }

    @Override
    public GameState insertOrUpdate(GameState gameState) throws DatabaseException {
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = gameState.getId() == null ? c.prepareStatement(INSERT_NEW, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(UPDATE)
        ){
            if(gameState.getId() != null){ // UPDATE
                stmt.setInt(9, gameState.getId());
            }

            stmt.setString(1, gameState.getPlayer1Name());
            stmt.setString(2, gameState.getPlayer2Name());
            stmt.setInt(3, gameState.isPlayer1Active() ? 1 : 0);
            stmt.setInt(4, gameState.isPlayer2AI() ?  1 : 0);
            stmt.setString(5, gameState.getBoardStateAsString());
            handleNullableInteger(stmt, gameState.getRemainingTime(), 6);
            handleNullableInteger(stmt, gameState.getRemainingTurnTime(), 7);
            handleNullableInteger(stmt, gameState.getTurnTime(), 8);

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                return null;
            }

            if(gameState.getId() == null){ // INSERT
                ResultSet genKeys = stmt.getGeneratedKeys();
                if(genKeys.next()){
                    gameState.setId(genKeys.getInt(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }

        return gameState;
    }

    @Override
    public void delete(int id) throws DatabaseException {
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = c.prepareStatement(DELETE);
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void handleNullableInteger(PreparedStatement stmt, Integer value, int p) throws SQLException {
        if (value== null) {
            stmt.setNull(p, Types.INTEGER);
        } else {
            stmt.setInt(p, value);
        }
    }

    private Integer handleSelectNullableInteger(ResultSet rs, String propName) throws SQLException{
        Integer iVal = rs.getInt(propName);
        if (rs.wasNull()) {
            iVal = null;
        }
        return iVal;
    }
}
