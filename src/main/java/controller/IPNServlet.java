package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ONLY VNPAY CALLS THIS.
 * Example parameters:
 * <code>?vnp_Amount=1000000&vnp_BankCode=NCB&vnp_BankTranNo=VNP14226112&vnp_CardType=ATM&vnp_OrderInfo=Thanh+toan+don+hang+thoi+gian%3A+2023-12-07+17%3A00%3A44&vnp_PayDate=20231207170112&vnp_ResponseCode=00&vnp_TmnCode=CTTVNP01&vnp_TransactionNo=14226112&vnp_TransactionStatus=00&vnp_TxnRef=166117&vnp_SecureHash=b6dababca5e07a2d8e32fdd3cf05c29cb426c721ae18e9589f7ad0e2db4b657c6e0e5cc8e271cf745162bcb100fdf2f64520554a6f5275bc4c5b5b3e57dc4b4b</code>
 */
public class IPNServlet extends HttpServlet {
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

        String orderId = request.getParameter("vnp_TxnRef");
        BigDecimal paidAmount = BigDecimal.valueOf(Long.parseLong(request.getParameter("vnp_TxnRef")));

        String signValue = config.VNPConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            service.Logging.logger.info("Order {}, amount {}", orderId, paidAmount);
            // TODO: ASK THE DATABASE FOR THE FOLLOWING INFORMATION:
            boolean checkOrderId = true; // Giá trị của vnp_TxnRef tồn tại trong CSDL của merchant
            boolean checkAmount = true; // Kiểm tra số tiền thanh toán do VNPAY phản hồi(vnp_Amount/100) với số tiền của
                                        // đơn hàng merchant tạo thanh toán: giả sử số tiền kiểm tra là đúng.
                                             // dịch khởi tạo chưa có IPN.
            if (checkOrderId) {
                if (checkAmount) {
                    if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                        // Xử lý/Cập nhật tình trạng giao dịch thanh toán "Thành công"
                        // out.print("GD Thanh cong");
                        service.Logging.logger.info("Order {} is valid.", orderId);
                    } else {
                        // Xử lý/Cập nhật tình trạng giao dịch thanh toán "Không thành công"
                        // out.print("GD Khong thanh cong");
                        service.Logging.logger.info("Order {} is invalid.", orderId);
                    }
                } else {
                    // not the same amount
                    service.Logging.logger.info("Order {}'s amount does not match database's.", orderId);
                }
            } else {
                // order id does not match
                service.Logging.logger.info("Order {} does not exist.", orderId);
            }

        } else {
            service.Logging.logger.info("Order {}'s checksum does not match.", orderId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
