package frontend;

import javax.swing.*;

import backend.Queries.Auth.SignInQuery;

public class SignIn extends JFrame {

    SignIn() {
        setTitle("Admin Sign In");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create panel
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        add(panel1);

        // Title
        JLabel title = new JLabel("Admin Sign In");
        title.setBounds(160, 60, 150, 30);
        panel1.add(title);

        // Email
        JLabel emailLabel = new JLabel("Email : ");
        emailLabel.setBounds(70, 140, 80, 30);
        panel1.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(170, 140, 200, 30);
        panel1.add(emailField);

        // Password
        JLabel passwordLabel = new JLabel("Password : ");
        passwordLabel.setBounds(70, 200, 80, 30);
        panel1.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(170, 200, 200, 30);
        panel1.add(passwordField);

        // Sign In button
        JButton button = new JButton("Sign In");
        button.setBounds(170, 270, 120, 35);
        panel1.add(button);

        button.addActionListener(e->{
            String email=emailField.getText();
            String password=new String(passwordField.getPassword());

            if(email.isEmpty() || password.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please enter email and password");
                return;
            }

            SignInQuery obj=new SignInQuery();

            boolean result=obj.signIn(email, password);

            if(result)
            {
                JOptionPane.showMessageDialog(this, "Login Successful");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Invalid Email or Password");
            }


        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new SignIn();
    }
}
