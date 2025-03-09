package service.vnpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * CAREFUL: ONLY USED BY THE WEB APPLICATION.<br></br>
 * Noteworthy: On a successful query, vnp_ResponseCode is 00.<br></br>
 * The detail of the transaction is in vnp_TransactionStatus, 00 if the transaction was successful<br></br>
 * <strong>Parameters needed: vnp_TxnRef, vnp_CreateDate (MUST MATCH THE ORDER'S)</strong><br></br>
 * Example json sent to VNPay: <code>{"vnp_RequestId":"31280303","vnp_Version":"2.1.0","vnp_Command":"refund","vnp_TmnCode":"E32QASX8","vnp_TransactionType":"02","vnp_TxnRef":"34428014","vnp_Amount":"1000000","vnp_OrderInfo":"Hoan tien GD OrderId:34428014","vnp_TransactionDate":"20250308183518","vnp_CreateBy":"admin","vnp_CreateDate":"20250308184330","vnp_IpAddr":"171.225.185.32","vnp_SecureHash":"hash"}</code><br></br>
 * Example json sent back from VNPay: <code>{"vnp_ResponseId":"fa14474280ea49b9b5987308e35ccb06","vnp_Command":"refund","vnp_ResponseCode":"00","vnp_Message":"Refund success","vnp_TmnCode":"E32QASX8","vnp_TxnRef":"34428014","vnp_Amount":"1000000","vnp_OrderInfo":"Hoan tien GD OrderId:34428014","vnp_BankCode":"VISA","vnp_PayDate":"20250308184331","vnp_TransactionNo":"14836054","vnp_TransactionType":"02","vnp_TransactionStatus":"05","vnp_SecureHash":"6ceafc8fb23f4c329608bf3a7c064278e77978aa771c51cdbf4384b1d7c8c2a7a5d84479df45061ef928e3e7b44353449611345bccfcf2cd7eb92e29f2f7171d"}</code>
 */
public class RefundService {
    public static java.util.Map<String, String> refund(String vnp_IpAddr, String vnp_TransactionType, String vnp_TxnRef, long amount, String vnp_TransactionDate, String vnp_CreateBy) throws IOException {

        //Command: refund
        String vnp_RequestId = config.VNPConfig.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = config.VNPConfig.vnp_TmnCode;
        // String vnp_TransactionType = req.getParameter("trantype");
        // String vnp_TxnRef = req.getParameter("order_id");
        // long amount = Integer.parseInt(req.getParameter("amount"))*100;
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
        String vnp_TransactionNo = ""; //Assuming value of the parameter "vnp_TransactionNo" does not exist on your system.
        // String vnp_TransactionDate = req.getParameter("trans_date");
        // String vnp_CreateBy = req.getParameter("user");
        
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        
        // String vnp_IpAddr = config.VNPConfig.getIpAddress(req);

        JsonObject  vnp_Params = new JsonObject ();

        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        
        if(vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty())
        {
            vnp_Params.addProperty("vnp_TransactionNo", "{get value of vnp_TransactionNo}");
        }
        
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);
        
        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, 
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate, 
                vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        
        String vnp_SecureHash = config.VNPConfig.hmacSHA512(config.VNPConfig.secretKey, hash_Data.toString());
        
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);
        
        URL url = URI.create(config.VNPConfig.vnp_ApiUrl).toURL();
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        service.Logging.logger.info("Sending 'POST' request to URL : {}", url);
        service.Logging.logger.info("Post Data : {}", vnp_Params);
        service.Logging.logger.info("Response Code : {}", responseCode);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
        response.append(output);
        }
        in.close();
        service.Logging.logger.info(response.toString());

        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(response.toString(), type);
    }

    /**
     * Wrapper method to make it look cleaner
     * @param vnp_IpAddr
     * @param vnp_TransactionType
     * @param vnp_TxnRef
     * @param amount
     * @param vnp_TransactionDate
     * @param vnp_CreateBy
     * @return
     * @throws IOException
     */
    public static boolean issueRefund(String vnp_IpAddr, String vnp_TransactionType, String vnp_TxnRef, long amount, String vnp_TransactionDate, String vnp_CreateBy) throws IOException {
        return refund(vnp_IpAddr, vnp_TransactionType, vnp_TxnRef, amount, vnp_TransactionDate, vnp_CreateBy).get("vnp_ResponseCode").equals("00");
    }
}
