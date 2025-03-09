package service;

import java.util.Properties;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailService {
    public static void sendMail(int userID, java.util.List<model.CartItem> cartItems, java.sql.Connection connection)
            throws MessagingException, java.sql.SQLException {
        final String username = config.Config.MailConfig.EMAIL_AUTH_USER;
        final String password = config.Config.MailConfig.EMAIL_AUTH_PASSWORD;

        if (username.isBlank() || password.isBlank()) {
            service.Logging.logger.warn("Mailing service cannot send mail, username and password are blank.");

            return;
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        model.User userDetail;
        try {
            userDetail = dao.UserDAO.UserFetcher.getUser(userID);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Cannot find user details for id {}, reason: {}", userID, e.getMessage());

            return;
        }


        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.Config.MailConfig.EMAIL_AUTH_USER));
        message.setRecipients(
                MimeMessage.RecipientType.TO,
                InternetAddress.parse(userDetail.getEmail()));

        // String body = "Dear " + userDetail[0] + ", "
        // + "\n\n Cam on ban da mua nhung mat hang sau: \n\n";

        StringBuilder body = new StringBuilder("<!DOCTYPE html><html><head><title>None</title></head><body>");

        body.append("<h2>Dear ");
        body.append(userDetail.getUsername());
        body.append(", </h2>");
        body.append("<h2>: </h2>");

        body.append("<table border='1'><tr><th>Name</th><th>Quantity</th><th>Price</th></tr>");

        for (final model.CartItem cartItem : cartItems) {
            // TODO: IMPLEMENT AFTER CART ITEMS

            // body += "\n\t" + cartItem.getProduct().getName();
            // body.append("<tr>")
            //         .append("<td>").append(cartItem.getProduct().getName()).append("</td>")
            //         .append("<td>").append(cartItem.getQuantity()).append("</td>")
            //         .append("<td>").append("$").append(cartItem.getProduct().getPrice()).append("</td>")
            //         .append("</tr>");
        }

        body.append("</table></body></html>");

        message.setSubject("[TEST] Cam on ban da mua hang!");

        message.setContent(body.toString(), "text/html; charset=utf-8");
        // message.setText(body.toString());

        Transport.send(message);
    }
}
