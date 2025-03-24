/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Vector;
import model.Category;
import model.Product;
import dal.ProductDAO;
import dal.CategoryDAO;

@WebServlet(name = "Home", urlPatterns = {"/Home"})
public class Home extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        ProductDAO dao1 = new ProductDAO();
        CategoryDAO dao2 = new CategoryDAO();
         Vector<Product> pro= dao1.getAllProduct("select * from tblProducts");
         Vector<Category> cate= dao2.getAllCategory("select * from tblCategories");
         Product d = dao1.getNewProduct("SELECT TOP 1 * FROM tblProducts ORDER BY productID DESC");
         request.setAttribute("p", d);
         request.setAttribute("data1", pro);
         request.setAttribute("data2", cate);
         request.getRequestDispatcher("Home1.jsp").forward(request, response);
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
