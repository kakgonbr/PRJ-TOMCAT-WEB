package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderedItem;

/**
 * The servlet responsible for displaying the payment result, redirected by
 * VNPay.<br></br>
 * Example redirect parameters:
 * <code>?vnp_Amount=1000000&vnp_BankCode=VISA&vnp_BankTranNo=7414299775556409503612&vnp_CardType=VISA&vnp_OrderInfo=Thanh+toan+don+hang%3A94878848&vnp_PayDate=20250308173205&vnp_ResponseCode=00&vnp_TmnCode=E32QASX8&vnp_TransactionNo=14835978&vnp_TransactionStatus=00&vnp_TxnRef=94878848&vnp_SecureHash=f88c46d03fb1cbbf7ff6168c909ae96aad4db209e0c3153d49892575d556d20eaa8eda6fe39042a55fbe5eb7b0ff76fdc4c9385ac6f659a8d2bf79d827e4eaed</code>
 */
public class PayReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        java.util.Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName),
                    StandardCharsets.US_ASCII.toString());
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = config.VNPConfig.hashAllFields(fields);

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                request.setAttribute("status", "Success");
            } else {
                request.setAttribute("status", "Failure");
            }
        } else {
            request.setAttribute("status", "Invalid signature");
        }

        request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER_COMPLETE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);   
    }
}
