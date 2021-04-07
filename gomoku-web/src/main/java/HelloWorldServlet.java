import hu.alkfejl.model.DAO.GameState;
import hu.alkfejl.model.DAO.GameStateDAO;
import hu.alkfejl.model.exception.DatabaseException;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelloWorldServlet")
public class HelloWorldServlet extends HttpServlet {

    private GameStateDAO dao = new GameStateDAO();

    private List<GameState> refreshTable(){
        try {
            return dao.listAll();
        } catch (DatabaseException e){
            e.printStackTrace();
        }
        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GameState> gs = refreshTable();
        System.out.println(gs.get(0).getPlayer2Name());
        response.getWriter().append("Served at: ").append(gs.get(0).getPlayer2Name());
    }

}