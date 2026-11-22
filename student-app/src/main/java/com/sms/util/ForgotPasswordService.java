package com.sms.util;

import backend.Queries.Auth.ForgotPasswordQuery;

// Handles the forgot password process.
public class ForgotPasswordService {

    // Create an object to access database methods.
    private final ForgotPasswordQuery query = new ForgotPasswordQuery();

    // Generate and send OTP to the user's email.
    public boolean sendOTP(String email) {

        // Check whether the email exists.
        if (!query.emailExists(email)) {
            return false;
        }

        // Generate a 6-digit OTP.
        String otp = OTPGenerator.generateOTP();

        // Save the hashed OTP in the database.
        if (!query.saveOTP(email, otp)) {
            return false;
        }

        // Send OTP to the user's email.
        try {
            EmailService.sendOTP(email, otp);
            return true;

        } catch (Exception e) {
            System.err.println("OTP email sending failed: " + e.getMessage());
            return false;
        }
    }

    // Check whether the entered OTP is valid (does not clear it).
    public boolean verifyOTP(String email, String enteredOTP) {
        return query.verifyOTP(email, enteredOTP);
    }

    // Verify OTP and reset the user's password.
    public boolean resetPassword(String email, String enteredOTP, String newPassword) {

        // First, check whether the entered OTP is correct.
        if (!query.verifyOTP(email, enteredOTP)) {
            return false;
        }

        // If OTP is correct, update the user's password.
        return query.updatePassword(email, newPassword);
    }

}