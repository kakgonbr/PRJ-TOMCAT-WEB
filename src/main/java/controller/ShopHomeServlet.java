package controller;
 
 import java.io.IOException;
 import java.io.PrintWriter;
 import jakarta.servlet.ServletException;
 import jakarta.servlet.http.HttpServlet;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;
 
 /**
  *
  * @author hoahtm
  */
 public class ShopHomeServlet extends HttpServlet {
 
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String shopIdParam = request.getParameter("shopId");
 
         if (shopIdParam == null || shopIdParam.isEmpty()) {
             response.sendRedirect(request.getContextPath() + "/shop-signup");
             return;
         }
 
         int shopId = Integer.parseInt(shopIdParam);
         request.setAttribute("shopId", shopId);
         request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response); 
     }
 
     @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
     }
 }