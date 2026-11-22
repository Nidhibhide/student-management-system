package frontend;

import com.sms.util.ForgotPasswordService;

import javax.swing.*;
import java.awt.*;

// Forgot Password screen — stepped: email → OTP → reset password.
public class ForgotPassword extends JFrame {

    private final ForgotPasswordService service = new ForgotPasswordService();

    private final JTextField emailField = new JTextField();
    private final JTextField otpField = new JTextField();
    private final JPasswordField newPasswordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();

    private final JPanel emailRow = new JPanel(new GridBagLayout());
    private final JPanel otpRow = new JPanel(new GridBagLayout());
    private final JPanel newPasswordRow = new JPanel(new GridBagLayout());
    private final JPanel confirmPasswordRow = new JPanel(new GridBagLayout());

    private final JButton sendOTPButton = new JButton("Send OTP");
    private final JButton verifyOTPButton = new JButton("Verify OTP");
    private final JButton resetPasswordButton = new JButton("Reset Password");
    private final JButton backToLoginButton = new JButton("Back to Login");

    private JPanel contentPanel;

    public ForgotPassword() {

        setTitle("Student Management System - Forgot Password");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Full-page background; form stays a centered fixed column.
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(40, 40, 40, 40));

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setMaximumSize(new Dimension(560, Integer.MAX_VALUE));

        JLabel heading = new JLabel(
                "Forgot Password", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension fieldSize = new Dimension(380, 42);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        emailField.setPreferredSize(fieldSize);
        emailField.setMinimumSize(fieldSize);
        emailField.setFont(fieldFont);
        otpField.setPreferredSize(fieldSize);
        otpField.setMinimumSize(fieldSize);
        otpField.setFont(fieldFont);
        newPasswordField.setPreferredSize(fieldSize);
        newPasswordField.setMinimumSize(fieldSize);
        newPasswordField.setFont(fieldFont);
        confirmPasswordField.setPreferredSize(fieldSize);
        confirmPasswordField.setMinimumSize(fieldSize);
        confirmPasswordField.setFont(fieldFont);

        addFormRow(emailRow, "Registered Email:", emailField);
        addFormRow(otpRow, "Enter OTP:", otpField);
        addFormRow(newPasswordRow, "New Password:", newPasswordField);
        addFormRow(confirmPasswordRow, "Confirm Password:", confirmPasswordField);

        emailRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        otpRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPasswordRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPasswordRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(sendOTPButton);
        buttonPanel.add(verifyOTPButton);
        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(backToLoginButton);

        contentPanel.add(heading);
        contentPanel.add(Box.createVerticalStrut(28));
        contentPanel.add(emailRow);
        contentPanel.add(otpRow);
        contentPanel.add(newPasswordRow);
        contentPanel.add(confirmPasswordRow);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel);
        add(mainPanel);

        sendOTPButton.addActionListener(e -> sendOTP());
        verifyOTPButton.addActionListener(e -> verifyOTP());
        resetPasswordButton.addActionListener(e -> resetPassword());
        backToLoginButton.addActionListener(e -> {
            new SignIn();
            dispose();
        });

        // Step 1: email + Send OTP only.
        showStep(1);

        setVisible(true);
    }

    private void addFormRow(JPanel row, String labelText, JComponent field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        GridBagConstraints fieldConstraints = new GridBagConstraints();

        labelConstraints.gridx = 0;
        labelConstraints.anchor = GridBagConstraints.EAST;
        labelConstraints.insets = new Insets(12, 0, 12, 12);

        fieldConstraints.gridx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1;
        fieldConstraints.insets = new Insets(12, 0, 12, 0);

        row.add(new JLabel(labelText), labelConstraints);
        row.add(field, fieldConstraints);
    }

    // 1 = email, 2 = OTP, 3 = new password only (email/OTP hidden).
    private void showStep(int step) {
        emailRow.setVisible(step == 1 || step == 2);
        otpRow.setVisible(step == 2);
        newPasswordRow.setVisible(step == 3);
        confirmPasswordRow.setVisible(step == 3);

        sendOTPButton.setVisible(step == 1);
        verifyOTPButton.setVisible(step == 2);
        resetPasswordButton.setVisible(step == 3);

        emailField.setEnabled(step == 1);
        otpField.setEnabled(step == 2);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Generate and send OTP.
    private void sendOTP() {

        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter your registered email.");
            return;
        }

        boolean result = service.sendOTP(email);

        if (result) {
            JOptionPane.showMessageDialog(
                    this, "OTP sent successfully. Please check your email.");
            showStep(2);
        } else {
            JOptionPane.showMessageDialog(
                    this, "Unable to send OTP. Please check your email.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Verify OTP before showing password fields.
    private void verifyOTP() {

        String email = emailField.getText().trim();
        String otp = otpField.getText().trim();

        if (otp.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter the OTP.");
            return;
        }

        boolean result = service.verifyOTP(email, otp);

        if (result) {
            JOptionPane.showMessageDialog(
                    this, "OTP verified. Please set a new password.");
            showStep(3);
        } else {
            JOptionPane.showMessageDialog(
                    this, "Invalid OTP or OTP expired.",
                    "Verification Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Verify OTP again and update password.
    private void resetPassword() {

        String email = emailField.getText().trim();
        String otp = otpField.getText().trim();

        String newPassword = new String(newPasswordField.getPassword());

        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this, "Passwords do not match.");
            return;
        }

        if (newPassword.length() < 8) {
            JOptionPane.showMessageDialog(
                    this, "Password must contain at least 8 characters.");
            return;
        }

        boolean result = service.resetPassword(
                email, otp, newPassword);

        if (result) {

            JOptionPane.showMessageDialog(
                    this, "Password reset successfully!");

            new SignIn();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this, "Invalid OTP or OTP expired.",
                    "Reset Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
