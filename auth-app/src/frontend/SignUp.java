// package frontend;

// import javax.swing.*;
// import backend.Queries.Auth.*;

// public class SignUp extends JFrame {

//   SignUp() {
//     setTitle("Admin SignUp");
//     setSize(450, 400);
//     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//     setLocationRelativeTo(null);

//     // create panel
//     JPanel panel1 = new JPanel();
//     panel1.setLayout(null);
//     add(panel1);

//     JLabel title = new JLabel("Admin Sign Up");
//     title.setBounds(140, 25, 200, 30);
//     panel1.add(title);

//     JLabel nameLabel = new JLabel("Full Name : ");
//     nameLabel.setBounds(50, 90, 100, 30);
//     panel1.add(nameLabel);

//     JTextField nameField = new JTextField();
//     nameField.setBounds(160, 90, 200, 30);
//     panel1.add(nameField);

//     JLabel emailLabel = new JLabel("Email : ");
//     emailLabel.setBounds(50, 140, 100, 30);
//     panel1.add(emailLabel);

//     JTextField emailField = new JTextField();
//     emailField.setBounds(160, 140, 200, 30);
//     panel1.add(emailField);

//     // Password
//     JLabel passwordLabel = new JLabel("Password : ");
//     passwordLabel.setBounds(50, 190, 100, 30);
//     panel1.add(passwordLabel);

//     JPasswordField passwordField = new JPasswordField();
//     passwordField.setBounds(160, 190, 200, 30);
//     panel1.add(passwordField);

//     // Confirm Password
//     JLabel confirmPasswordLabel = new JLabel("Confirm Password : ");
//     confirmPasswordLabel.setBounds(50, 240, 120, 30);
//     panel1.add(confirmPasswordLabel);

//     JPasswordField confirmPasswordField = new JPasswordField();
//     confirmPasswordField.setBounds(160, 240, 200, 30);
//     panel1.add(confirmPasswordField);

//     // Sign Up button
//     JButton button = new JButton("Sign Up");
//     button.setBounds(160, 300, 120, 35);
//     panel1.add(button);

//     button.addActionListener(e -> {

//       String name = nameField.getText();
//       String email = emailField.getText();
//       String password = new String(passwordField.getPassword());
//       String confirmpassword = new String(confirmPasswordField.getPassword());

//       if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
//         JOptionPane.showMessageDialog(this, "Please fill all fields");
//         return;
//       }

//       if (!password.equals(confirmpassword)) {
//         JOptionPane.showMessageDialog(this, "Password do not match");
//         return;
//       }
//       SignUpQuery signUpQuery = new SignUpQuery();

//       boolean result = signUpQuery.register(name, email, password);

//       if (result) {
//         JOptionPane.showMessageDialog(this, "Registration Successful");
//         nameField.setText("");
//         emailField.setText("");
//         passwordField.setText("");
//         confirmPasswordField.setText("");
//         new SignIn();
//         dispose();
//       } else
//         JOptionPane.showMessageDialog(this, "Registration Failed!");

//     });

//     setVisible(true);

//   }

//   public static void main(String[] args) {
//     new SignUp();
//   }
// }



//Registration

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
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel.
        JPanel mainPanel = new JPanel(new BorderLayout(15, 20));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        JLabel title = new JLabel(
                "Admin Sign Up",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));

        mainPanel.add(title, BorderLayout.NORTH);

        // Create registration fields.
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));

        fullnameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(fullnameField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Confirm Password:"));
        formPanel.add(confirmPasswordField);

        // Create both buttons.
        JButton signUpButton = new JButton("Sign Up");

        JButton signInButton = new JButton(
                "Already have an account? Sign In"
        );

        // Display buttons vertically.
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.add(signUpButton);
        buttonPanel.add(signInButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 20));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

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
        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        if (fullname.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields."
            );
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
                    + "One special character"
            );
            return;
        }

        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match."
            );
            return;
        }

        // Save account in MySQL.
        SignUpQuery query = new SignUpQuery();

        boolean result = query.register(fullname, email, password);

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful! Please sign in."
            );

            new SignIn();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. Email may already exist.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Application entry point.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUp::new);
    }
}
