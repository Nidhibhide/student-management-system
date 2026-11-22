package com.sms.gui;

import com.sms.model.Student;
import com.sms.service.StudentService;
import frontend.SignIn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import frontend.ChangePassword;

// Main Swing window for managing student records.
public class StudentDashboard extends JFrame {

    // Connect the GUI to the existing service layer.
    private final StudentService studentService = new StudentService();

    // Student form fields
    private final JTextField idField = new JTextField();
    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField mobileField = new JTextField();
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Select", "Male", "Female", "Other" });
    private final JTextField dobField = new JTextField();
    private final JTextArea addressArea = new JTextArea(3, 15);
    private final JTextField courseField = new JTextField();
    private final JTextField admissionDateField = new JTextField();

    // Search field
    private final JTextField searchField = new JTextField(10);

    // Table to display student records
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] {
                    "ID", "First Name", "Last Name", "Email",
                    "Mobile", "Gender", "DOB", "Address",
                    "Course", "Admission Date"
            }, 0) {
        // Prevent users from directly editing table cells.
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable studentTable = new JTable(tableModel);

    public StudentDashboard() {
        this(-1);
    }

    public StudentDashboard(int userId) {

        setTitle("Student Management System");
        setSize(1250, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use a main panel to organize the dashboard.
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Dashboard heading
        JLabel heading = new JLabel(
                "STUDENT MANAGEMENT SYSTEM",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        // Account dropdown
        JButton accountButton = new JButton("Account ▼");

        JPopupMenu accountMenu = new JPopupMenu();

        JMenuItem changePasswordItem = new JMenuItem("Change Password");
        JMenuItem logoutItem = new JMenuItem("Logout");

        accountMenu.add(changePasswordItem);
        accountMenu.addSeparator();
        accountMenu.add(logoutItem);

        accountButton.addActionListener(e -> accountMenu.show(accountButton, 0, accountButton.getHeight()));

        logoutItem.addActionListener(e -> {
            new SignIn();
            dispose();
        });
        changePasswordItem.addActionListener(e -> {
            if (userId == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "User information not available.");
                return;
            }

            new ChangePassword(userId);
        });
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(heading, BorderLayout.CENTER);
        headerPanel.add(accountButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Student form
        JPanel formPanel = createFormPanel();

        // Student table
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        studentTable.setRowHeight(25);

        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder("Student Records"));

        // Divide the form and table into two sections.
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                formPanel,
                tableScrollPane);

        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.35);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Search and action buttons
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load student records when the dashboard opens.
        refreshTable();

        // When a row is selected, show its details in the form.
        studentTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                loadSelectedStudent();
            }
        });

        setVisible(true);
    }

    // Create the student details form.
    private JPanel createFormPanel() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(
                BorderFactory.createTitledBorder("Student Details"));

        // Student ID is generated automatically by MySQL.
        idField.setEditable(false);

        addFormField(panel, "Student ID:", idField);
        addFormField(panel, "First Name:", firstNameField);
        addFormField(panel, "Last Name:", lastNameField);
        addFormField(panel, "Email:", emailField);
        addFormField(panel, "Mobile:", mobileField);
        addFormField(panel, "Gender:", genderCombo);
        addFormField(panel, "DOB (yyyy-mm-dd):", dobField);

        // Address uses a text area because it can contain multiple lines.
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addFormField(panel, "Address:", addressScroll);

        addFormField(panel, "Course:", courseField);
        addFormField(panel, "Admission Date:", admissionDateField);

        return panel;
    }

    // Add a label and input component to the form.
    private void addFormField(JPanel panel, String label, Component field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    // Create search and CRUD buttons.
    private JPanel createBottomPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton searchButton = new JButton("Search by ID");
        JButton refreshButton = new JButton("Refresh");
        JButton clearButton = new JButton("Clear Form");

        panel.add(new JLabel("Student ID:"));
        panel.add(searchField);

        panel.add(searchButton);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(clearButton);

        // Connect buttons to their respective methods.
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        searchButton.addActionListener(e -> searchStudent());
        refreshButton.addActionListener(e -> refreshTable());
        clearButton.addActionListener(e -> clearForm());

        return panel;
    }

    // Read the form and create a Student object.
    private Student getStudentFromForm() {

        Student student = new Student();

        student.setFirstName(firstNameField.getText().trim());
        student.setLastName(lastNameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setMobile(mobileField.getText().trim());

        String gender = (String) genderCombo.getSelectedItem();

        if ("Select".equals(gender)) {
            student.setGender(null);
        } else {
            student.setGender(gender);
        }

        student.setDob(parseDate(dobField.getText(), "DOB"));
        student.setAddress(addressArea.getText().trim());
        student.setCourse(courseField.getText().trim());
        student.setAdmissionDate(
                parseDate(admissionDateField.getText(), "Admission Date"));

        return student;
    }

    // Convert a yyyy-mm-dd string into a SQL Date.
    // An empty date field is treated as null.
    private Date parseDate(String value, String fieldName) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    fieldName + " must use yyyy-mm-dd format.");
        }
    }

    // Add a new student.
    private void addStudent() {

        try {
            Student student = getStudentFromForm();

            boolean result = studentService.addStudent(student);

            if (result) {
                JOptionPane.showMessageDialog(
                        this, "Student added successfully!");

                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(
                        this, "Unable to add student.");
            }

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // Update the selected student's details.
    private void updateStudent() {

        try {
            if (idField.getText().isBlank()) {
                showError("Please select a student from the table first.");
                return;
            }

            Student student = getStudentFromForm();
            student.setId(Integer.parseInt(idField.getText()));

            boolean result = studentService.updateStudent(student);

            if (result) {
                JOptionPane.showMessageDialog(
                        this, "Student updated successfully!");

                clearForm();
                refreshTable();
            } else {
                showError("Student could not be updated.");
            }

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // Delete the selected student.
    private void deleteStudent() {

        try {
            if (idField.getText().isBlank()) {
                showError("Please select a student from the table first.");
                return;
            }

            int id = Integer.parseInt(idField.getText());

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete student ID " + id + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            boolean result = studentService.deleteStudent(id);

            if (result) {
                JOptionPane.showMessageDialog(
                        this, "Student deleted successfully!");

                clearForm();
                refreshTable();
            } else {
                showError("Student not found or could not be deleted.");
            }

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // Search for a student using their ID.
    private void searchStudent() {

        try {
            String searchText = searchField.getText().trim();

            if (searchText.isEmpty()) {
                showError("Please enter a student ID.");
                return;
            }

            int id = Integer.parseInt(searchText);

            Student student = studentService.searchStudentById(id);

            if (student == null) {
                showError("No student found with ID " + id);
                return;
            }

            displayStudent(student);

            // Select the matching row in the table, if present.
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (Integer.parseInt(
                        tableModel.getValueAt(row, 0).toString()) == id) {
                    studentTable.setRowSelectionInterval(row, row);
                    studentTable.scrollRectToVisible(
                            studentTable.getCellRect(row, 0, true));
                    break;
                }
            }

        } catch (NumberFormatException e) {
            showError("Student ID must be a number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // Load all students from the database into the table.
    private void refreshTable() {

        try {
            List<Student> students = studentService.getAllStudents();

            // Remove old rows before loading fresh records.
            tableModel.setRowCount(0);

            for (Student student : students) {

                tableModel.addRow(new Object[] {
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getMobile(),
                        student.getGender(),
                        student.getDob(),
                        student.getAddress(),
                        student.getCourse(),
                        student.getAdmissionDate()
                });
            }

        } catch (SQLException e) {
            showError("Unable to load students: " + e.getMessage());
        }
    }

    // Fill the form when the user selects a table row.
    private void loadSelectedStudent() {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);

        idField.setText(getCellValue(modelRow, 0));
        firstNameField.setText(getCellValue(modelRow, 1));
        lastNameField.setText(getCellValue(modelRow, 2));
        emailField.setText(getCellValue(modelRow, 3));
        mobileField.setText(getCellValue(modelRow, 4));

        String gender = getCellValue(modelRow, 5);

        if (gender.isEmpty()) {
            genderCombo.setSelectedIndex(0);
        } else {
            genderCombo.setSelectedItem(gender);
        }

        dobField.setText(getCellValue(modelRow, 6));
        addressArea.setText(getCellValue(modelRow, 7));
        courseField.setText(getCellValue(modelRow, 8));
        admissionDateField.setText(getCellValue(modelRow, 9));
    }

    // Safely get a table cell value as text.
    private String getCellValue(int row, int column) {

        Object value = tableModel.getValueAt(row, column);

        return value == null ? "" : value.toString();
    }

    // Display a Student object in the form.
    private void displayStudent(Student student) {

        idField.setText(String.valueOf(student.getId()));
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        emailField.setText(student.getEmail());
        mobileField.setText(student.getMobile());

        if (student.getGender() == null) {
            genderCombo.setSelectedIndex(0);
        } else {
            genderCombo.setSelectedItem(student.getGender());
        }

        dobField.setText(
                student.getDob() == null ? "" : student.getDob().toString());

        addressArea.setText(student.getAddress());
        courseField.setText(student.getCourse());

        admissionDateField.setText(
                student.getAdmissionDate() == null
                        ? ""
                        : student.getAdmissionDate().toString());
    }

    // Clear all form fields.
    private void clearForm() {

        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        mobileField.setText("");
        genderCombo.setSelectedIndex(0);
        dobField.setText("");
        addressArea.setText("");
        courseField.setText("");
        admissionDateField.setText("");
        searchField.setText("");

        studentTable.clearSelection();
    }

    // Show an error message in a dialog.
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // Start the GUI on Swing's Event Dispatch Thread.
    public static void main(String[] args) {

        SwingUtilities.invokeLater(StudentDashboard::new);
    }
}
