package oceanviewresort.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    //  CHANGE THESE
    private static final String FROM_EMAIL =
            System.getenv("OVR_EMAIL");

    private static final String APP_PASSWORD =
            System.getenv("OVR_EMAIL_PASSWORD");

    private static Session createSession() {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                    }
                });
    }

    // ====================================
    // Generic Email Sender
    // ====================================
    public static void sendEmail(String toEmail, String subject, String content) {

        try {
            Session session = createSession();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (Exception e) {
            System.out.println("Email sending failed.");
            e.printStackTrace();
        }
    }

    // ====================================
    // Verification Email
    // ====================================
    public static void sendVerificationEmail(String toEmail, String token) {

        String verifyLink =
                "http://localhost:8080/OceanViewResort/verify?token=" + token;

        String subject = "Verify Your Ocean View Resort Account";

        String content =
                "Welcome to Ocean View Resort!\n\n" +
                        "Click the link below to verify your account:\n\n" +
                        verifyLink;

        sendEmail(toEmail, subject, content);
    }
}