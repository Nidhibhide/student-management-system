package frontend;

import javax.swing.*;
import backend.Queries.Auth.*;

public class SignUp extends JFrame {

  SignUp() {
    setTitle("Admin SignUp");
    setSize(450, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // create panel
    JPanel panel1 = new JPanel();
    panel1.setLayout(null);
    add(panel1);

    JLabel title = new JLabel("Admin Sign Up");
    title.setBounds(140, 25, 200, 30);
    panel1.add(title);

    JLabel nameLabel = new JLabel("Full Name : ");
    nameLabel.setBounds(50, 90, 100, 30);
    panel1.add(nameLabel);

    JTextField nameField = new JTextField();
    nameField.setBounds(160, 90, 200, 30);
    panel1.add(nameField);

    JLabel emailLabel = new JLabel("Email : ");
    emailLabel.setBounds(50, 140, 100, 30);
    panel1.add(emailLabel);

    JTextField emailField = new JTextField();
    emailField.setBounds(160, 140, 200, 30);
    panel1.add(emailField);

    // Password
    JLabel passwordLabel = new JLabel("Password : ");
    passwordLabel.setBounds(50, 190, 100, 30);
    panel1.add(passwordLabel);

    JPasswordField passwordField = new JPasswordField();
    passwordField.setBounds(160, 190, 200, 30);
    panel1.add(passwordField);

    // Confirm Password
    JLabel confirmPasswordLabel = new JLabel("Confirm Password : ");
    confirmPasswordLabel.setBounds(50, 240, 120, 30);
    panel1.add(confirmPasswordLabel);

    JPasswordField confirmPasswordField = new JPasswordField();
    confirmPasswordField.setBounds(160, 240, 200, 30);
    panel1.add(confirmPasswordField);

    // Sign Up button
    JButton button = new JButton("Sign Up");
    button.setBounds(160, 300, 120, 35);
    panel1.add(button);

    button.addActionListener(e -> {

      String name = nameField.getText();
      String email = emailField.getText();
      String password = new String(passwordField.getPassword());
      String confirmpassword = new String(confirmPasswordField.getPassword());

      if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields");
        return;
      }

      if (!password.equals(confirmpassword)) {
        JOptionPane.showMessageDialog(this, "Password do not match");
        return;
      }
      SignUpQuery signUpQuery = new SignUpQuery();

      boolean result = signUpQuery.register(name, email, password);

      if (result) {
        JOptionPane.showMessageDialog(this, "Registration Successful");
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        new SignIn();
        dispose();
      } else
        JOptionPane.showMessageDialog(this, "Registration Failed!");

    });

    setVisible(true);

  }

  public static void main(String[] args) {
    new SignUp();
  }
}
