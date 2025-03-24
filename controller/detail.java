/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.CategoryDAO;
import dal.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Vector;
import model.Category;
import model.Product;

/**
 *
 * @author Asus
 */
@WebServlet(name="detail", urlPatterns={"/detail"})
public class detail extends HttpServlet {
   

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
                   String service = request.getParameter("service");
        int id = Integer.parseInt(request.getParameter("pID"));
        ProductDAO dao = new ProductDAO();
        CategoryDAO dao2 = new CategoryDAO();

        Product p = dao.getProductByID(id);
        Vector<Category> categories = dao2.getAllCategory("select * from tblCategories");
        Product p1 = dao.getNewProduct("SELECT TOP 1 * FROM tblProducts ORDER BY productID DESC");
        Category cc = dao2.search(p1.getCategoryID());
        request.setAttribute("p", p);
        request.setAttribute("last", p1);
        request.setAttribute("listcc", categories);
        request.getRequestDispatcher("Detail.jsp").forward(request, response);
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
