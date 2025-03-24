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
@WebServlet(name = "CategoryURL", urlPatterns = {"/CategoryURL"})
public class CategoryURL extends HttpServlet {

    private static final String sqlAll2 = "select * from tblCategories";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        CategoryDAO dao = new CategoryDAO();
        String service = request.getParameter("service");
        if (service == null) {
            service = "listCategory";
        }
   if (service.equals("getID")) {
            String id = request.getParameter("cID");
            Vector<Product> listC = dao.getProductByCate(id);
            request.setAttribute("categoryID", id);
            request.setAttribute("data1", listC);
                    ProductDAO dao1 = new ProductDAO();
         CategoryDAO dao2 = new CategoryDAO();
         Vector<Category> cate= dao2.getAllCategory("select * from tblCategories");
         Product d = dao1.getNewProduct("SELECT TOP 1 * FROM tblProducts ORDER BY productID DESC");
         request.setAttribute("p", d);
         request.setAttribute("data2", cate);
         request.getRequestDispatcher("Home1.jsp").forward(request, response);
        }
        if (service.equals("listCategory")) {
            Vector<Category> list = dao.getAllCategory(sqlAll2);
            request.setAttribute("data", list);
            request.getRequestDispatcher("caJSP/cateJSP.jsp").forward(request, response);
        }

        if (service.equals("deleteCategory")) {
            dao.deleteCategory((String) request.getParameter("cID"));
            response.sendRedirect("ServletCategoryJSP");
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
