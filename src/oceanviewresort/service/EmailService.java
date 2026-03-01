package oceanviewresort.service;

public interface EmailService {

    void sendReservationConfirmation(String toEmail, String message);

    void sendBillReceipt(String toEmail, String subject, String message);
}