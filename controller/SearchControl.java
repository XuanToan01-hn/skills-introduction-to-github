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
@WebServlet(name="SearchControl", urlPatterns={"/SearchControl"})
public class SearchControl extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    ProductDAO dao = new ProductDAO();

    try {
        String name = request.getParameter("txt");
        String categoryID = request.getParameter("categoryID");
        Vector<Product> list = dao.searchProductName(name);
        
        request.setAttribute("data1", list);
                ProductDAO dao1 = new ProductDAO();
        CategoryDAO dao2 = new CategoryDAO();
         Vector<Category> cate= dao2.getAllCategory("select * from tblCategories");
         Product d = dao1.getNewProduct("SELECT TOP 1 * FROM tblProducts ORDER BY productID DESC");
         request.setAttribute("p", d);
         request.setAttribute("data2", cate);
         request.getRequestDispatcher("Home1.jsp").forward(request, response);

    } catch (Exception e) {
        e.printStackTrace();
    }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
