/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.ProductDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Asus
 */
@WebServlet(name="fillData", urlPatterns={"/fill"})
public class fillData extends HttpServlet {
    private static final String sql = "select count(*) from tblUsers where roleID = 2";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        UserDAO dao1 = new UserDAO();
        ProductDAO dao2 = new ProductDAO();
        int cnt = dao1.getNumUser("select count(*) from tblUsers where roleID !=1");
        int cnt2= dao2.getNumProduct("select count(*) from tblProducts ");
        request.setAttribute("cnt", cnt);
        request.setAttribute("cnt2", cnt2);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
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
