package controller;


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

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {
    private static final String sql2 = "select * from tblRoles";
    private static final String SECRET_KEY = "6Lf6lPMqAAAAAPWIPgV_zsH3lp01XPRq9roQTlX3";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        try {
            String user = request.getParameter("user");
            String password = request.getParameter("pass");
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response"); // Lấy reCAPTCHA response

            if (!verifyRecaptcha(gRecaptchaResponse)) {
                request.setAttribute("mess", "reCAPTCHA failed");
                request.getRequestDispatcher("Login1.jsp").forward(request, response);
                return;
            }

            UserDAO dao = new UserDAO();
            User u = dao.checkLoginU(user, password);

            if (u != null) {
                session.setAttribute("username", u.getFullName());
                session.setAttribute("user", u.getUserID()); 

                if (u.isAdmin()) {
                response.sendRedirect("fill"); 
    } else {
        response.sendRedirect("Home");
    }
            } else {
                request.setAttribute("mess", "Login Failed");
                request.getRequestDispatcher("Login1.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm xác thực reCAPTCHA với Google
    private boolean verifyRecaptcha(String gRecaptchaResponse) throws IOException {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(params);
        writer.flush();
        writer.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String jsonResponse = response.toString();
        return parseJsonResponse(jsonResponse);
    }
    
    private boolean parseJsonResponse(String jsonResponse) {
        int successIndex = jsonResponse.indexOf("\"success\":");
        if (successIndex == -1) {
            return false; 
        }
        String successValue = jsonResponse.substring(successIndex + 10, successIndex + 15).trim();
        return successValue.equals("true"); 
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

