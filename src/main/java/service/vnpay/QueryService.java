package service.vnpay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

/**
 * CAREFUL: ONLY USED BY THE WEB APPLICATION.<br></br>
 * Noteworthy: On a successful query, vnp_ResponseCode is 00.<br></br>
 * The detail of the transaction is in vnp_TransactionStatus, 00 if the transaction was successful<br></br>
 * <strong>Parameters needed: vnp_IpAddr, vnp_TxnRef, vnp_TransDate (MUST MATCH THE ORDER'S)</strong><br></br>
 * Example json sent to VNPay: <code>{"vnp_RequestId":"41735318","vnp_Version":"2.1.0","vnp_Command":"querydr","vnp_TmnCode":"E32QASX8","vnp_TxnRef":"34428014","vnp_OrderInfo":"Kiem tra ket qua GD OrderId:34428014","vnp_TransactionDate":"20250308183518","vnp_CreateDate":"20250308184308","vnp_IpAddr":"171.225.185.32","vnp_SecureHash":"hashhere"}</code><br></br>
 * Example json sent back from VNPay: <code>{"vnp_ResponseId":"282a3735af2c4eb782a753dafd007b25","vnp_Command":"querydr","vnp_ResponseCode":"00","vnp_Message":"QueryDR Success","vnp_TmnCode":"E32QASX8","vnp_TxnRef":"34428014","vnp_Amount":"1000000","vnp_OrderInfo":"Thanh toan don hang:34428014","vnp_BankCode":"VISA","vnp_PayDate":"20250308183518","vnp_TransactionNo":"14836051","vnp_TransactionType":"01","vnp_TransactionStatus":"00","vnp_SecureHash":"hashhere"}</code>
 */
public class QueryService {
    public static java.util.Map<String, String> query(String vnp_IpAddr, String vnp_TxnRef, String vnp_TransDate) throws IOException {
        String vnp_RequestId = config.VNPConfig.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = config.VNPConfig.vnp_TmnCode;
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;
        //String vnp_TransactionNo = req.getParameter("transactionNo");
        
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        
        JsonObject  vnp_Params = new JsonObject ();
        
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        //vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);
        
        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
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
}
