/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.RoleDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import model.Role;
import model.User;

@WebServlet(name = "UserURL", urlPatterns = {"/UserURL"})
public class UserURL extends HttpServlet {

    private static final String sql1 = "select * from tblUsers";
    private static final String sql2 = "select * from tblRoles";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        UserDAO dao = new UserDAO();
        String service = request.getParameter("service");
        if (service == null) {
            service = "listUser";
        }
        if (service.equals("addUser")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                RoleDAO daoU = new RoleDAO();
                Vector<Role> vUser = daoU.getAllRole(sql2);
                request.setAttribute("r", vUser);
                request.getRequestDispatcher("User/insert.jsp").forward(request, response);
            } else {
                String id = request.getParameter("userID").trim();
                String fullName = request.getParameter("fullName").trim();
                String pass = request.getParameter("password").trim();
                int roleID = Integer.parseInt(request.getParameter("roleID"));
                String address = request.getParameter("address").trim();
                String phone = request.getParameter("phone").trim();
                String email = request.getParameter("email").trim();
                int active = Integer.parseInt(request.getParameter("status"));
                User checkDup = dao.searchUser(id);
                if (checkDup != null) {
                    request.setAttribute("error", "Category ID already exists!");
                    request.getRequestDispatcher("User/insert.jsp").forward(request, response);
                }
                User u = new User(id, fullName, pass, roleID, address, phone, email, active);
                dao.insertUser(u);
                response.sendRedirect("UserURL");
            }
        }
        if (service.equals("updateUser")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                String uID = request.getParameter("pID");
                User u = dao.searchUser(uID);

                RoleDAO rd = new RoleDAO();
                Vector<Role> vector = rd.getAllRole(sql2);
                request.setAttribute("vector", vector);
                request.setAttribute("u", u);
                request.getRequestDispatcher("User/update.jsp").forward(request, response);
            } else {
                String id = request.getParameter("userID");
                String fullName = request.getParameter("fullName");
                String pass = request.getParameter("password");
                int roleID = Integer.parseInt(request.getParameter("roleID"));
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                int active = Integer.parseInt(request.getParameter("active"));
                User u = new User(id, fullName, pass, roleID, address, phone, email, active);
                dao.updateUser(u);
                response.sendRedirect("UserURL");

            }
        }

        if (service.equals("deleteUser")) {
            dao.deleteUser1((String) request.getParameter("pID"));
            response.sendRedirect("UserURL");
        }
        if (service.equals("listUser")) {
            Vector<User> list = dao.getAllUser(sql1);
            request.setAttribute("listP", list);
            request.getRequestDispatcher("User/manager.jsp").forward(request, response);
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
