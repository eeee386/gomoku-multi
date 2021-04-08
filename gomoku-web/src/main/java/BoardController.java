import hu.alkfejl.model.DAO.GameState;
import hu.alkfejl.model.DAO.GameStateBean;
import hu.alkfejl.model.DAO.GameStateDAO;
import hu.alkfejl.model.Game;
import hu.alkfejl.model.exception.BoardSizeException;
import hu.alkfejl.model.exception.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {

    private Game game;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            GameStateDAO dao = new GameStateDAO();
            GameState gs = null;
            gs = dao.getLastInserted();
            req.setAttribute("game", gs.createBean());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
