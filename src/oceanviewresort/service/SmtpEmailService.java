package oceanviewresort.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class SmtpEmailService implements EmailService {

    private final String username;
    private final String password;

    public SmtpEmailService() {

        this.username = System.getenv("MAIL_USERNAME");
        this.password = System.getenv("MAIL_PASSWORD");

        if (username == null || password == null) {
            throw new RuntimeException(
                    "Environment variables MAIL_USERNAME or MAIL_PASSWORD not set."
            );
        }
    }

    @Override
    public void sendReservationConfirmation(String toEmail, String message) {
        sendEmail(toEmail, "Reservation Confirmation", message);
    }

    @Override
    public void sendBillReceipt(String toEmail, String subject, String message) {
        sendEmail(toEmail, subject, message);
    }

    private void sendEmail(String toEmail, String subject, String content) {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}