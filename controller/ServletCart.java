package controller;

import dal.CartDao;
import dal.OrderDAO;
import dal.OrderDetailsDao;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.OrderDetail;
import model.Orders;
import dal.ProductDAO;

@WebServlet(name = "ServletCart", urlPatterns = {"/cart"})
public class ServletCart extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        CartDao dao = new CartDao();
        String service = request.getParameter("service");
        if (service == null) {
            service = "showCart";
        }

        if (service.equals("add2Cart")) {
            int pID = Integer.parseInt(request.getParameter("pID"));
            Cart cart = dao.getCart(pID);

            if (cart != null) {
                Object obj = session.getAttribute(pID + "");
                if (obj instanceof Cart) {
                    cart = (Cart) obj;
                    cart.setQuantity(cart.getQuantity() + 1);
                } else {
                    cart.setQuantity(1);
                }
                session.setAttribute(pID + "", cart);
            }
            response.sendRedirect("cart");
        }

        if (service.equals("removeItem")) {
            int pID = Integer.parseInt(request.getParameter("pID"));
            session.removeAttribute(pID + "");
            response.sendRedirect("cart");
        }

        if (service.equals("removeAll")) {
            Enumeration<String> enu = session.getAttributeNames();
            while (enu.hasMoreElements()) {
                String key = enu.nextElement();
                if (!"userID".equals(key)) {
                    session.removeAttribute(key);
                }
            }
            response.sendRedirect("cart");
        }

        if (service.equals("increaseQuantity")) {
            int pID = Integer.parseInt(request.getParameter("pID"));
            Object obj = session.getAttribute(pID + "");
            if (obj instanceof Cart) {
                Cart cart = (Cart) obj;
                if (cart.getQuantity() < dao.getTotal(pID)) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    session.setAttribute(pID + "", cart);
                }
            }
            response.sendRedirect("cart");
        }

        if (service.equals("decreaseQuantity")) {
            int pID = Integer.parseInt(request.getParameter("pID"));
            Object obj = session.getAttribute(pID + "");
            if (obj instanceof Cart) {
                Cart cart = (Cart) obj;
                if (cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    session.setAttribute(pID + "", cart);
                }
            }
            response.sendRedirect("cart");
        }
        
        
         if (service.equals("confirm")) {
    try {
        Vector<Cart> cartItems = new Vector<>();
        Enumeration<String> enu = session.getAttributeNames();
        while (enu.hasMoreElements()) {
            String key = enu.nextElement();
            Object obj = session.getAttribute(key);
            if (obj instanceof Cart) {
                cartItems.add((Cart) obj);
            }
        }
        
                if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("cart.jsp?message=Cart is empty");
            return;
        }
        double total = 0;
        for (Cart cartItem : cartItems) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }

        Orders newOrder = new Orders(new java.sql.Date(System.currentTimeMillis()), total, (String) session.getAttribute("user"));
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.insertOrder(newOrder); 
        int orderID = newOrder.getOrderID();

        OrderDetailsDao orderDetailsDao = new OrderDetailsDao();
        ProductDAO productDAO = new ProductDAO();
        for (Cart cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail(cartItem.getPrice(), cartItem.getQuantity(), orderID, cartItem.getProductID());
            orderDetailsDao.insertOD(orderDetail);

            productDAO.updateProductQuantity(cartItem.getProductID(), cartItem.getQuantity());
        }

        // Xóa các session liên quan đến giỏ hàng
        Enumeration<String> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.matches("\\d+")) { // Chỉ xóa các session có key là số (ID sản phẩm)
                session.removeAttribute(key);
            }
        }

        response.sendRedirect("confirm.jsp");
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("error.jsp");
    }
}

        if (service.equals("showCart")) {
            Vector<Cart> vector = new Vector<>();
            Enumeration<String> enu = session.getAttributeNames();
            while (enu.hasMoreElements()) {
                String key = enu.nextElement();
                Object obj = session.getAttribute(key);
                if (obj instanceof Cart) {
                    vector.add((Cart) obj);
                }
            }
            request.setAttribute("data", vector);
            request.setAttribute("PageTitle", "Product Manager");
            request.setAttribute("tableTitle", "List of Product");
            request.getRequestDispatcher("Cart/showCart.jsp").forward(request, response);
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