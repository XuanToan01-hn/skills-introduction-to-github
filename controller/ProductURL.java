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
import java.sql.Date;
import java.util.Vector;
import model.Category;
import model.Product;

/**
 *
 * @author Asus
 */
@WebServlet(name = "ProductURL", urlPatterns = {"/ProductURL"})
public class ProductURL extends HttpServlet {

    private static final String sqlAll1 = "select * from tblProducts";
    private static final String sqlAll2 = "select * from tblCategories";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ProductDAO dao = new ProductDAO();
        String service = request.getParameter("service");
        if (service == null) {
            service = "listProduct";
        }
        if (service.equals("addProduct")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                CategoryDAO daoC = new CategoryDAO();
                Vector<Category> vector = daoC.getAllCategory(sqlAll2);
                request.setAttribute("vector", vector);
                request.getRequestDispatcher("product/insertProduct.jsp").forward(request, response);
            } else {
                // Insert product to database
                String productName = request.getParameter("productName");
                String image = request.getParameter("image");
                double price = Double.parseDouble(request.getParameter("price"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                String categoryID = request.getParameter("categoryID");
                Date importDate = Date.valueOf(request.getParameter("importDate"));
                Date usingDate = Date.valueOf(request.getParameter("usingDate"));
                int status = Integer.parseInt(request.getParameter("status"));

                Product p = new Product(productName, image, price, quantity, categoryID, importDate, usingDate, status);
                dao.insertProduct2(p);

                // Reload list of product after insert
                Vector<Product> list = dao.getAllProduct(sqlAll1);
                request.setAttribute("listP", list);
                request.getRequestDispatcher("product/ManagerProduct.jsp").forward(request, response);

            }
        }

        if (service.equals("deleteProduct")) {
            dao.deleteProduct1(Integer.parseInt(request.getParameter("pID")));
            response.sendRedirect("ProductURL");
        }

        if (service.equals("updateProduct")) {
            String submit = request.getParameter("submit");
            if (submit == null) { // Show UpdateProduct.JSP
                int id = Integer.parseInt(request.getParameter("pID"));
                Product p = dao.searchProducts(id);
                CategoryDAO daoC = new CategoryDAO();
                Vector<Category> vector = daoC.getAllCategory(sqlAll2);
                request.setAttribute("vector", vector);
                request.setAttribute("p", p);
                request.getRequestDispatcher("product/update.jsp").forward(request, response);
            } else {
                try {
                    int productID = Integer.parseInt(request.getParameter("productID"));
                    String productName = request.getParameter("productName");
                    String image = request.getParameter("image");
                    double price = Double.parseDouble(request.getParameter("price"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    String categoryID = request.getParameter("categoryID");
                    Date importDate = Date.valueOf(request.getParameter("importDate"));
                    Date usingDate = Date.valueOf(request.getParameter("usingDate"));
                    int status = Integer.parseInt(request.getParameter("status"));

                    Product p = new Product(productID, productName, image, price, quantity, categoryID, importDate, usingDate, status);
                    dao.updateProduct(p);

                    Vector<Product> list = dao.getAllProduct(sqlAll1);
                    request.setAttribute("listP", list);
                    request.getRequestDispatcher("product/ManagerProduct.jsp").forward(request, response);
                } catch (Exception e) {
                    response.sendRedirect("ErrorProduct.jsp");
                }

            }
        }

        if (service.equals("listProduct")) {
            Vector<Product> list = dao.getAllProduct(sqlAll1);
            //setdataFor view
            request.setAttribute("listP", list);
            //select view
            request.getRequestDispatcher("product/ManagerProduct.jsp").forward(request, response);
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
