package frontend;

import backend.Queries.Auth.SignUpQuery;

import javax.swing.*;
import java.awt.*;

// Registration window for new admin accounts.
public class SignUp extends JFrame {

    private JTextField fullnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignUp() {

        setTitle("Admin Registration");
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
        contentPanel.setMaximumSize(new Dimension(560, Integer.MAX_VALUE));

        JLabel title = new JLabel(
                "Admin Sign Up",
                SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fixed-width form — labels next to fields, no stretch.
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        GridBagConstraints fieldConstraints = new GridBagConstraints();

        labelConstraints.gridx = 0;
        labelConstraints.anchor = GridBagConstraints.EAST;
        labelConstraints.insets = new Insets(10, 0, 10, 12);

        fieldConstraints.gridx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1;
        fieldConstraints.insets = new Insets(10, 0, 10, 0);

        fullnameField = new JTextField(32);
        emailField = new JTextField(32);
        passwordField = new JPasswordField(32);
        confirmPasswordField = new JPasswordField(32);

        Dimension fieldSize = new Dimension(380, 42);
        fullnameField.setPreferredSize(fieldSize);
        fullnameField.setMinimumSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        emailField.setMinimumSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);
        confirmPasswordField.setPreferredSize(fieldSize);
        confirmPasswordField.setMinimumSize(fieldSize);

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        fullnameField.setFont(fieldFont);
        emailField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        confirmPasswordField.setFont(fieldFont);

        labelConstraints.gridy = 0;
        fieldConstraints.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), labelConstraints);
        formPanel.add(fullnameField, fieldConstraints);

        labelConstraints.gridy = 1;
        fieldConstraints.gridy = 1;
        formPanel.add(new JLabel("Email:"), labelConstraints);
        formPanel.add(emailField, fieldConstraints);

        labelConstraints.gridy = 2;
        fieldConstraints.gridy = 2;
        formPanel.add(new JLabel("Password:"), labelConstraints);
        formPanel.add(passwordField, fieldConstraints);

        labelConstraints.gridy = 3;
        fieldConstraints.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), labelConstraints);
        formPanel.add(confirmPasswordField, fieldConstraints);

        // Primary + navigation actions in one row.
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(120, 36));
        signUpButton.setMaximumSize(new Dimension(120, 36));

        JButton signInButton = new JButton("Already have an account?");
        signInButton.setPreferredSize(new Dimension(220, 36));
        signInButton.setMaximumSize(new Dimension(220, 36));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(signUpButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(signInButton);

        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(28));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel);
        add(mainPanel);

        // Register button action.
        signUpButton.addActionListener(e -> registerAdmin());

        // Existing users can open the login page.
        signInButton.addActionListener(e -> {
            new SignIn();
            dispose();
        });

        setVisible(true);
    }

    // Register a new admin account.
    private void registerAdmin() {

        String fullname = fullnameField.getText().trim();
        String email = emailField.getText().trim();

        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullname.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.");
            return;
        }

        if (password.length() < 8
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*[0-9].*")
                || !password.matches(".*[^a-zA-Z0-9].*")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain:\n"
                            + "At least 8 characters\n"
                            + "One uppercase letter\n"
                            + "One lowercase letter\n"
                            + "One number\n"
                            + "One special character");
            return;
        }

        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.");
            return;
        }

        // Save account in MySQL.
        SignUpQuery query = new SignUpQuery();

        boolean result = query.register(fullname, email, password);

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful! Please sign in.");

            new SignIn();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. Email may already exist.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Application entry point.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUp::new);
    }
}
