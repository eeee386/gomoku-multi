import hu.alkfejl.model.*;
import hu.alkfejl.model.DAO.GameState;
import hu.alkfejl.model.DAO.GameStateDAO;
import hu.alkfejl.model.exception.BoardSizeException;
import hu.alkfejl.model.exception.DatabaseException;
import hu.alkfejl.model.exception.IllegalMoveException;
import hu.alkfejl.model.exception.PlayerException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {

    private Game game;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("createNew") != null) {
            resp.setCharacterEncoding("utf-8");
            try {
                Player player1 = new Player(req.getParameter("player1"), true, PlayerSign.X);
                Player player2 = req.getParameter("againstAI") != null ? new AIPlayer(req.getParameter("player2"), false, PlayerSign.O) : new Player(req.getParameter("player2"), false, PlayerSign.O);

                int width = Integer.parseInt(req.getParameter("width"));
                int height = Integer.parseInt(req.getParameter("height"));
                Board board = new Board(width, height);
                Double playTime = getDoubleFromParam(req, "playTime");
                Double turnTime = getDoubleFromParam(req, "turnTime");

                game = new Game(player1, player2, board, playTime, turnTime);
                resp.sendRedirect("board.jsp");
            } catch (BoardSizeException e) {
                resp.sendRedirect("index.jsp?error=" + e.getMessage());
            }
        }
        GameState gs = new GameState(game);
        req.setAttribute("game", gs.createBean());
        req.setAttribute("width", game.getBoard().getWidth());
        req.setAttribute("height", game.getBoard().getHeight());
        req.setAttribute("boardState", gs.getBoardState());
        req.setAttribute("hasSomebodyWon", game.hasSomebodyWon());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", null);
        if (req.getParameter("save") != null) {
            try {
                GameState gs = new GameState(game);
                GameStateDAO dao = new GameStateDAO();
                dao.insertOrUpdate(gs);

            } catch (DatabaseException e) {
                e.printStackTrace();
                req.setAttribute("error", e.getMessage());
            }
            resp.setCharacterEncoding("utf-8");
            resp.sendRedirect("board.jsp");
        } else if (req.getParameter("finish") != null) {
            resp.setCharacterEncoding("utf-8");
            resp.sendRedirect("index.jsp");
        } else {
            try {
                Map<String, String[]> parameters = req.getParameterMap();
                String[] values = {" "};
                for (String parameter : parameters.keySet()) {
                    values = parameters.get(parameter);
                }
                String[] props = values[0].split(":");

                game.playTurn(Integer.parseInt(props[0]), Integer.parseInt(props[1]));
                if (!game.hasSomebodyWon()) {
                    game.swapActivePlayer();
                    if (game.getActivePlayer() instanceof AIPlayer) {
                        game.handleAIPlayer(game.getActivePlayer());
                        game.swapActivePlayer();
                    }
                }
                resp.sendRedirect("board.jsp");
            } catch (IllegalMoveException | PlayerException e) {
                e.printStackTrace();
                req.setAttribute("error", e.getMessage());
            }

        }
    }

    private Double getDoubleFromParam(HttpServletRequest request, String param) {
        String str = request.getParameter(param);
        return "".equals(str) || str == null ? null : Double.parseDouble(str);
    }
}
