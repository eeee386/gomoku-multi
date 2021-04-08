import hu.alkfejl.model.Board;
import hu.alkfejl.model.DAO.GameState;
import hu.alkfejl.model.DAO.GameStateDAO;
import hu.alkfejl.model.Game;
import hu.alkfejl.model.Player;
import hu.alkfejl.model.PlayerSign;
import hu.alkfejl.model.exception.BoardSizeException;
import hu.alkfejl.model.exception.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ChooseController")
public class ChooseController extends HttpServlet {
    private Double getDoubleFromParam(HttpServletRequest request, String param) {
        String str = request.getParameter(param);
        System.out.println(str);
        return "".equals(str) || str == null ? null : Double.parseDouble(str);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("utf-8");
        try {
            Player player1 = new Player(request.getParameter("player1"), true, PlayerSign.X);
            Player player2 = new Player(request.getParameter("player2"), false, PlayerSign.O);

            Board board = new Board(Integer.parseInt(request.getParameter("width")), Integer.parseInt(request.getParameter("height")));
            Double playTime = getDoubleFromParam(request, "playTime");
            Double turnTime = getDoubleFromParam(request, "turnTime");

            Game game = new Game(player1, player2, board, playTime, turnTime);
            GameState gs = new GameState(game);

            GameStateDAO dao = new GameStateDAO();
            dao.insertOrUpdate(gs);
            response.sendRedirect("board.jsp");

        } catch (BoardSizeException | DatabaseException e) {
            e.printStackTrace();
        }


    }
}
