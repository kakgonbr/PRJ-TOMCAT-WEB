package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class PayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(config.Config.JSPMapper.PAYMENT_PAY_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // required information from user input: locale
        // required information from request/session: ip address, cart id,
        // required information from database: total amount, new order id 

        // TODO

        response.sendRedirect(service.vnpay.PortalService.getLink(request.getRemoteAddr(), "", 10_000 * 100, "vn", "1234"));
    }
}
