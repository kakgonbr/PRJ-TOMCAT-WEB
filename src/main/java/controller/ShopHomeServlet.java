package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author hoahtm
 */
public class ShopHomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer shopId = (Integer) session.getAttribute("shopId");
        if (shopId == null) {
            response.sendRedirect(request.getContextPath() + "/shop-signup");
            return;
        }

        request.setAttribute("shopId", shopId);
        request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
