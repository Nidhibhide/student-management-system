package com.sms.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

// This class sends OTP emails using Gmail.
public class EmailService {

    public static void sendOTP(String recipientEmail, String otp)
            throws MessagingException {
        // Gmail address (temporary value).
        String senderEmail = "nidhibhide144@gmail.com";

        // Gmail App Password (temporary value).
        String appPassword = "qpdl pnnu aqwx fdsa";

        // Gmail SMTP settings.
        Properties properties = new Properties();

        // Gmail SMTP server address.
        properties.put("mail.smtp.host", "smtp.gmail.com");

        // Gmail SMTP port.
        properties.put("mail.smtp.port", "587");

        // Enable email authentication.
        properties.put("mail.smtp.auth", "true");

        // Enable TLS security.
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a Gmail session using email and password.
        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                senderEmail,
                                appPassword);
                    }
                });

        // Create the email message.
        Message message = new MimeMessage(session);

        // Set sender email.
        message.setFrom(new InternetAddress(senderEmail));

        // Set receiver email.
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail));

        // Set email subject.
        message.setSubject("Password Reset OTP");

        // Set email content.
        message.setText(
                "Your password reset OTP is: " + otp
                        + "\n\nThis OTP is valid for 5 minutes.");

        // Send the email.
        Transport.send(message);
    }
}