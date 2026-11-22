package frontend;

import backend.Queries.Auth.SignInQuery;
import com.sms.gui.StudentDashboard;

import javax.swing.*;
import java.awt.*;

// Login page for existing admin accounts.
public class SignIn extends JFrame {

    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public SignIn() {

        setTitle("Student Management System - Admin Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Full-page background; form stays a centered fixed column.
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        // Width fixed; height grows with content so buttons never overlap.
        contentPanel.setMaximumSize(new Dimension(520, Integer.MAX_VALUE));

        JLabel heading = new JLabel(
                "Admin Login",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fixed-width form — labels next to fields, no stretch.
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        GridBagConstraints fieldConstraints = new GridBagConstraints();

        labelConstraints.gridx = 0;
        labelConstraints.anchor = GridBagConstraints.EAST;
        labelConstraints.insets = new Insets(12, 0, 12, 12);

        fieldConstraints.gridx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1;
        fieldConstraints.insets = new Insets(12, 0, 12, 0);

        Dimension fieldSize = new Dimension(380, 42);
        emailField.setPreferredSize(fieldSize);
        emailField.setMinimumSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);
        emailField.setColumns(32);
        passwordField.setColumns(32);

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        emailField.setFont(fieldFont);
        passwordField.setFont(fieldFont);

        labelConstraints.gridy = 0;
        fieldConstraints.gridy = 0;
        formPanel.add(new JLabel("Email:"), labelConstraints);
        formPanel.add(emailField, fieldConstraints);

        labelConstraints.gridy = 1;
        fieldConstraints.gridy = 1;
        formPanel.add(new JLabel("Password:"), labelConstraints);
        formPanel.add(passwordField, fieldConstraints);

        // Primary + navigation actions in one row.
        JButton signInButton = new JButton("Sign In");
        signInButton.setPreferredSize(new Dimension(120, 36));
        signInButton.setMaximumSize(new Dimension(120, 36));

        JButton signUpButton = new JButton("Don't have an account?");
        JButton forgotPasswordButton = new JButton("Forgot Password?");

        forgotPasswordButton.setPreferredSize(new Dimension(160, 36));
        forgotPasswordButton.setMaximumSize(new Dimension(160, 36));
        signUpButton.setPreferredSize(new Dimension(200, 36));
        signUpButton.setMaximumSize(new Dimension(200, 36));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(signInButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(forgotPasswordButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(signUpButton);

        contentPanel.add(heading);
        contentPanel.add(Box.createVerticalStrut(28));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel);
        add(mainPanel);

        // Login button action.
        signInButton.addActionListener(e -> loginAdmin());
        // Open Forgot Password screen.
        forgotPasswordButton.addActionListener(e -> {
            new ForgotPassword();
            dispose();
        });
        // Go back to registration.
        signUpButton.addActionListener(e -> {
            new SignUp();
            dispose();
        });

        setVisible(true);
    }

    // Verify admin login details.
    private void loginAdmin() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.");
            return;
        }

        // Check credentials in the database.
        SignInQuery query = new SignInQuery();
        int userId = query.signIn(email, password);

        // Login successful: userId is a positive number.
        if (userId > 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Opening Student Dashboard...");

            // Open the Student Dashboard on the Swing UI thread.
            SwingUtilities.invokeLater(() -> {

                try {

                    StudentDashboard dashboard = new StudentDashboard(userId);

                    // Make sure the dashboard is visible.
                    dashboard.setVisible(true);
                    dashboard.toFront();

                    // Close the login page after opening dashboard.
                    dispose();

                } catch (Exception ex) {

                    // Print the actual error in the VS Code terminal.
                    ex.printStackTrace();

                    JOptionPane.showMessageDialog(
                            this,
                            "Login successful, but Student Dashboard "
                                    + "could not be opened.\n\n"
                                    + "Error: " + ex.getMessage(),
                            "Dashboard Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

        } else if (userId == -2) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your account is locked. Please try again after 15 minutes.",
                    "Account Locked",
                    JOptionPane.WARNING_MESSAGE);

        }

        else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Email or Password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(SignIn::new);
    }
}
