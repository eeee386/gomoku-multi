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

    private Exception exp;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("delete") != null){
            try {
                GameStateDAO dao = new GameStateDAO();
                dao.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect("load.jsp");
            } catch (DatabaseException e) {
                resp.sendRedirect("load.jsp?error=" + e.getMessage());
            }
            return;
        }
        if (req.getParameter("create") != null) {
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
        } else if(req.getParameter("load") != null){
            GameState gs = new GameState(
                    Integer.parseInt(req.getParameter("id")),
                    req.getParameter("player1"),
                    req.getParameter("player2"),
                    Boolean.parseBoolean(req.getParameter("isPlayer1Active")),
                    Boolean.parseBoolean(req.getParameter("isPlayer2AI")),
                    getIntegerFromParam(req, "remainingTime"),
                    getIntegerFromParam(req, "remainingTurnTime"),
                    GameState.stringToBoard(req.getParameter("boardState")),
                    getIntegerFromParam(req,"turnTime")
            );

            try {
                game=new Game(gs);
                game.getBoard().cleanUpBoard();
                resp.sendRedirect("board.jsp?turnTime=" + Utils.formatDuration(game.getRemainingTurnTime()));
            } catch (BoardSizeException e) {
                e.printStackTrace();
                resp.sendRedirect("load.jsp?error=" + e.getMessage());
            }
        }
        if(game != null){
            GameState gs = new GameState(game);
            req.setAttribute("game", gs.createBean());
            req.setAttribute("width", game.getBoard().getWidth());
            req.setAttribute("height", game.getBoard().getHeight());
            req.setAttribute("boardState", gs.getBoardState());
            req.setAttribute("hasGameEnded", getHasGameEnded(req));
            req.setAttribute("error", exp == null ? null : exp.getMessage());
            setPlayTime(req);
            setTurnTime(req);
            req.setAttribute("playerNameToShow", getPlayerNameToShow(req));
            req.setAttribute("activeWinner", handleWinnerLabel(req));
        }

    }

    public boolean getHasGameEnded(HttpServletRequest req) {
        return game.hasSomebodyWon() || req.getParameter("isTurnFinished") != null || req.getParameter("isPlayTimeFinished") != null;
    }

    public String handleWinnerLabel(HttpServletRequest req) {
        if(game.hasSomebodyWon() || req.getParameter("isTurnFinished") != null){
            return "Winner: ";
        } else if(req.getParameter("isPlayTimeFinished") != null){
            return "Draw";
        } else {
            return "Active Player: ";
        }
    }

    public String getPlayerNameToShow(HttpServletRequest req){
        if(req.getParameter("isTurnFinished") != null){
            return game.getNonActivePlayer().getName();
        } else if (req.getParameter("isPlayTimeFinished") != null){
            return "";
        } else {
            return game.getActivePlayer().getName();
        }
    }

    public void setTurnTime(HttpServletRequest req){
        if(req.getParameter("turnTime") != null && !"".equals(req.getParameter("turnTime")) && !"null".equals(req.getParameter("turnTime"))) {
            try{
                req.setAttribute("turnTime", Utils.getSecondsFromFormattedString(req.getParameter("turnTime")));
            } catch (Exception e){
                req.setAttribute("turnTime", 60 *Double.parseDouble(req.getParameter("turnTime")));
            }
        } else if(getHasGameEnded(req)){
            req.setAttribute("turnTime", null);
        } else {
            req.setAttribute("turnTime", game.getTurnTimeInSeconds());
        }
    }

    public void setPlayTime(HttpServletRequest req){
        if(getHasGameEnded(req)){
            req.setAttribute("playTime", null);
        } else {
            req.setAttribute("playTime",  game.getRemainingTime());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        exp = null;
        Double playTime = getDoubleFromParam(req, "playTimeInput");
        if (playTime != null) {
            game.setRemainingTime((int) (double) playTime);
        }
        if (req.getParameter("save") != null) {
            Double turnTime = getDoubleFromParam(req, "turnTimeInput");
            String redirect = "board.jsp";
            if(turnTime != null){
                game.setRemainingTurnTime((int) (double) turnTime);
                redirect = "board.jsp?turnTime=" + Utils.formatDuration(game.getRemainingTurnTime());
            }
            try {
                GameState gs = new GameState(game);
                GameStateDAO dao = new GameStateDAO();
                dao.insertOrUpdate(gs);
            } catch (DatabaseException e) {
                e.printStackTrace();
                exp = e;
            }
            resp.setCharacterEncoding("utf-8");
            resp.sendRedirect(redirect);

        } else if (req.getParameter("finish") != null) {
            resp.setCharacterEncoding("utf-8");
            resp.sendRedirect("index.jsp");
        } else {
            try {
                Map<String, String[]> parameters = req.getParameterMap();
                String[] values = {" "};
                for (String parameter : parameters.keySet()) {
                    String[] newValues = parameters.get(parameter);
                    if(newValues[0].contains(":")){
                        values = parameters.get(parameter);
                    }
                }
                String[] props = values[0].split(":");

                game.playTurn(Integer.parseInt(props[0]), Integer.parseInt(props[1]));
                try {
                    game.setRemainingTime(Integer.parseInt(req.getParameter("playTimeInput")));
                } catch (Exception ignored){}
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
                exp = e;
                resp.sendRedirect("board.jsp");
            }

        }
    }

    private Double getDoubleFromParam(HttpServletRequest request, String param) {
        String str = request.getParameter(param);
        return "".equals(str) || str == null ? null : Double.parseDouble(str);
    }
    private Integer getIntegerFromParam(HttpServletRequest request, String param) {
        String str = request.getParameter(param);
        if(str != null && str.contains(":")){
            return Utils.getSecondsFromFormattedString(str);
        }
        return "".equals(str) || str == null ? null : Integer.parseInt(str);
    }
}
