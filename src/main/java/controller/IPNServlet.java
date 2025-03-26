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
 * ONLY VNPAY CALLS THIS.<br></br>
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

        int orderId = Integer.parseInt(request.getParameter("vnp_TxnRef"));
        BigDecimal paidAmount = BigDecimal.valueOf(Long.parseLong(request.getParameter("vnp_Amount")) / 100);
        String date = request.getParameter("vnp_PayDate");

        String signValue = config.VNPConfig.hashAllFields(fields);

        boolean success = false;
        if (signValue.equals(vnp_SecureHash)) {
            service.Logging.logger.info("Order {}, amount {}, date {}", orderId, paidAmount, date);

            if (isOrderValid(orderId, paidAmount)) {
                if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                    try {
                        dao.OrderDAO.OrderManager.markCompleted(orderId);
                        success = true;
                        //add notif
                        if (success) {
                            try {
                                model.ProductOrder order = dao.OrderDAO.OrderManager.getOrder(orderId);
                                model.Notification notification = new model.Notification();
                                
                                notification.setUserId(order.getUserId());  
                                notification.setTitle("THANH TOAN DON HANG THANH CONG");
                                notification.setBody("Bạn đã Thanh toán thành công cho đơn hàng #" + orderId);
                                notification.setIsRead(false); 
                                
                                dao.NotificationDAO.NotificationManager.add(notification);
                                
                                service.Logging.logger.info("Notification created for user {} regarding order {}", order.getUserId(), orderId);
                            } catch (java.sql.SQLException e) {
                                service.Logging.logger.error("FAILED TO CREATE NOTIFICATION FOR ORDER {}, REASON: {}", orderId, e.getMessage());
                            }
                        }
                        }catch (java.sql.SQLException e) {
                        service.Logging.logger.error("FAILED TO MARK ORDER {} AS COMPLETED, REASON: {}", orderId, e.getMessage());

                        try {
                            if (!service.vnpay.RefundService.issueRefund(request.getRemoteAddr(), "02", Integer.toString(orderId), paidAmount.longValue() * 100, date, "Admin")) throw new IOException("Response code is not 00");
                        } catch (IOException ioe) {
                            // TODO: uh oh
                            service.Logging.logger.error("FAILED TO ISSUE A REFUND FOR ORDER {}, REASON: {}", orderId, ioe.getMessage());
                        }
                        return;
                    }

                        service.Logging.logger.info("Order {} was successful.", orderId);
                    }else {
                    // Xử lý/Cập nhật tình trạng giao dịch thanh toán "Không thành công"
                    // out.print("GD Khong thanh cong");
                    service.Logging.logger.info("Order {} failed.", orderId);
                }
                } else {
                    service.Logging.logger.info("Order {} does not exist, or paid amount does not match", orderId);
                }
            } else {
                service.Logging.logger.info("Order {}'s checksum does not match.", orderId);
            }

            if (!success) {
                deleteUponFailure(orderId);
            }
        }

        @Override
        protected void doPost
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            doGet(request, response);
        }

    

    private static boolean isOrderValid(int id, BigDecimal amount) {
        try {
            model.ProductOrder order = dao.OrderDAO.OrderManager.getOrder(id);

            return !order.isStatus() && order.getFinalPrice().longValue() == amount.longValue(); // false (order not yet completed) -> true AND amount matches (true)
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("The order {} does not exist, or retrieving it has resulted in an error, reason: {}", id, e.getMessage());

            return false;
        }
    }

    private static void deleteUponFailure(int orderId) {
        try {
            service.OrderConcurrencyService.removeFromOrder(orderId);

            dao.OrderDAO.OrderManager.deleteOrder(orderId);

        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("FAILED TO DELETE ORDER ID {}, REASON: {}", orderId, e.getMessage());
        }
    }
}
