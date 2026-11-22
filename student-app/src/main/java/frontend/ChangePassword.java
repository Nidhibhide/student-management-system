
package frontend;

import backend.Queries.Auth.ChangePasswordQuery;

import javax.swing.*;
import java.awt.*;

public class ChangePassword extends JFrame {

    private final int userId;

    private final JPasswordField currentPasswordField =
            new JPasswordField(25);

    private final JPasswordField newPasswordField =
            new JPasswordField(25);

    private final JPasswordField confirmPasswordField =
            new JPasswordField(25);

    public ChangePassword(int userId) {

        this.userId = userId;

        setTitle("Change Password");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        formPanel.add(new JLabel("Current Password:"));
        formPanel.add(currentPasswordField);

        formPanel.add(new JLabel("New Password:"));
        formPanel.add(newPasswordField);

        formPanel.add(new JLabel("Confirm Password:"));
        formPanel.add(confirmPasswordField);

        JButton updateButton = new JButton("Update Password");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Change Password");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(heading);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(formPanel);
        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel);
        add(mainPanel);

        updateButton.addActionListener(e -> updatePassword());

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void updatePassword() {

        String currentPassword =
                new String(currentPasswordField.getPassword());

        String newPassword =
                new String(newPasswordField.getPassword());

        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        if (currentPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all fields."
            );
            return;
        }

        if (newPassword.length() < 8
                || !newPassword.matches(".*[A-Z].*")
                || !newPassword.matches(".*[a-z].*")
                || !newPassword.matches(".*[0-9].*")
                || !newPassword.matches(".*[^a-zA-Z0-9].*")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 8 characters, "
                    + "uppercase, lowercase, number and special character."
            );
            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this, "New passwords do not match."
            );
            return;
        }

        if (currentPassword.equals(newPassword)) {

            JOptionPane.showMessageDialog(
                    this, "New password must be different."
            );
            return;
        }

        ChangePasswordQuery query = new ChangePasswordQuery();

        String result = query.changePassword(
                userId,
                currentPassword,
                newPassword
        );

        switch (result) {

            case "SUCCESS":

                JOptionPane.showMessageDialog(
                        this, "Password changed successfully!"
                );

                dispose();
                break;

            case "INVALID_CURRENT_PASSWORD":

                JOptionPane.showMessageDialog(
                        this, "Current password is incorrect."
                );
                break;

            case "USER_NOT_FOUND":

                JOptionPane.showMessageDialog(
                        this, "User not found."
                );
                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to change password. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
        }
    }
}
