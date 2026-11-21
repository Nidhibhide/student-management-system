// package frontend;

// import javax.swing.*;

// import backend.Queries.Auth.SignInQuery;

// public class SignIn extends JFrame {

//     SignIn() {
//         setTitle("Admin Sign In");
//         setSize(450, 400);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         // Create panel
//         JPanel panel1 = new JPanel();
//         panel1.setLayout(null);
//         add(panel1);

//         // Title
//         JLabel title = new JLabel("Admin Sign In");
//         title.setBounds(160, 60, 150, 30);
//         panel1.add(title);

//         // Email
//         JLabel emailLabel = new JLabel("Email : ");
//         emailLabel.setBounds(70, 140, 80, 30);
//         panel1.add(emailLabel);

//         JTextField emailField = new JTextField();
//         emailField.setBounds(170, 140, 200, 30);
//         panel1.add(emailField);

//         // Password
//         JLabel passwordLabel = new JLabel("Password : ");
//         passwordLabel.setBounds(70, 200, 80, 30);
//         panel1.add(passwordLabel);

//         JPasswordField passwordField = new JPasswordField();
//         passwordField.setBounds(170, 200, 200, 30);
//         panel1.add(passwordField);

//         // Sign In button
//         JButton button = new JButton("Sign In");
//         button.setBounds(170, 270, 120, 35);
//         panel1.add(button);

//         button.addActionListener(e->{
//             String email=emailField.getText();
//             String password=new String(passwordField.getPassword());

//             if(email.isEmpty() || password.isEmpty())
//             {
//                 JOptionPane.showMessageDialog(this, "Please enter email and password");
//                 return;
//             }

//             SignInQuery obj=new SignInQuery();

//             boolean result=obj.signIn(email, password);

//             if(result)
//             {
//                 JOptionPane.showMessageDialog(this, "Login Successful");
//             }
//             else
//             {
//                 JOptionPane.showMessageDialog(this, "Invalid Email or Password");
//             }


//         });

//         setVisible(true);
//     }

//     public static void main(String[] args) {
//         new SignIn();
//     }
// }




//Login 

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
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel.
        JPanel mainPanel = new JPanel(new BorderLayout(15, 20));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        JLabel heading = new JLabel(
                "Admin Login",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(heading, BorderLayout.NORTH);

        // Login form.
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        // Login button.
        JButton signInButton = new JButton("Sign In");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(signInButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 25));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Login button action.
        signInButton.addActionListener(e -> loginAdmin());

        setVisible(true);
    }

    // Verify admin login details.
    private void loginAdmin() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password."
            );
            return;
        }

        // Check credentials in the database.
        SignInQuery query = new SignInQuery();
        boolean result = query.signIn(email, password);

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Opening Student Dashboard..."
            );

            // Open the Student Dashboard on the Swing UI thread.
            SwingUtilities.invokeLater(() -> {

                try {

                    StudentDashboard dashboard = new StudentDashboard();

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
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Email or Password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(SignIn::new);
    }
}
