package hu.alkfejl.model.DAO;


import hu.alkfejl.model.exception.DatabaseException;

import java.util.List;

public interface DAOInterface {
    List<GameState> listAll() throws DatabaseException;
    List<GameStateBean> listAllAsBeans() throws DatabaseException;
    GameState insertOrUpdate(GameState gameState) throws DatabaseException;
    void delete(int id) throws DatabaseException;
}
