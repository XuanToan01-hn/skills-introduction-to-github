
package controller;

import controller.GoogleLogin;
import dal.RoleDAO;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import model.Role;
import model.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import model.GoogleAccount;

/**
 *
 * @author Asus
 */
@WebServlet(name = "Login1", urlPatterns = {"/login1"})
public class Login1 extends HttpServlet {

protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
                HttpSession session = request.getSession(true);

    String code = request.getParameter("code");
    if (code != null) {
        try {
            String accessToken = GoogleLogin.getToken(code);
            GoogleAccount googleAccount = GoogleLogin.getUserInfo(accessToken);
            
            UserDAO dao = new UserDAO();
            User u = dao.getUserByEmail(googleAccount.getEmail());

            if (u == null) {
                u = new User();
                u.setEmail(googleAccount.getEmail());
                u.setFullName(googleAccount.getName());
                dao.insertUser(u);
            session.setAttribute("username", u.getFullName());
            session.setAttribute("userG", u);
            if (u.isAdmin()) {
                response.sendRedirect("fill"); 
    } else {
        response.sendRedirect("Home");
    }
            }
            
        } catch (Exception e) {
            request.setAttribute("mess", "Google login failed");
            request.getRequestDispatcher("Login1.jsp").forward(request, response);
        }
    } 
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
