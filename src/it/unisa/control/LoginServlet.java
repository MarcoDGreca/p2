package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import it.unisa.model.UserBean;
import it.unisa.model.UserDao;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDao usDao = new UserDao();

        try {
            String username = request.getParameter("un");
            String plainPwd = request.getParameter("pw");

            UserBean user = usDao.doRetrieveByUsername(username);

            if (user != null && BCrypt.checkpw(plainPwd, user.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser", user);
                String checkout = request.getParameter("checkout");
                if (checkout != null)
                    response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
                else
                    response.sendRedirect(request.getContextPath() + "/Home.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error");
            }

        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
