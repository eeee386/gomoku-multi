import hu.alkfejl.model.DAO.GameStateBean;
import hu.alkfejl.model.DAO.GameStateDAO;
import hu.alkfejl.model.exception.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/LoadController")
public class LoadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("load") != null){
            resp.sendRedirect("load.jsp");
        }
        GameStateDAO dao = new GameStateDAO();
        try {
            List<GameStateBean> gsList = dao.listAllAsBeans();
            req.setAttribute("games", gsList);
            req.setAttribute("width", 3);
        } catch (DatabaseException e) {
            resp.sendRedirect("load.jsp?error=" + e.getMessage());
        }
    }

}
