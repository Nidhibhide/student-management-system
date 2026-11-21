package com.sms;

import com.sms.model.Student;
import com.sms.service.StudentService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

// Main class: Entry point of the Student Management System.
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        StudentService studentService = new StudentService();

        // Keep displaying the menu until the user chooses Exit
        while (true) {

            System.out.println("\n========== STUDENT MANAGEMENT SYSTEM ==========");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.println("===============================================");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {

                switch (choice) {

                    case "1":
                        addStudent(scanner, studentService);
                        break;

                    case "2":
                        viewAllStudents(studentService);
                        break;

                    case "3":
                        searchStudent(scanner, studentService);
                        break;

                    case "4":
                        updateStudent(scanner, studentService);
                        break;

                    case "5":
                        deleteStudent(scanner, studentService);
                        break;

                    case "6":
                        System.out.println("Thank you for using Student Management System!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice! Please select between 1 and 6.");
                }

            } catch (Exception e) {

                System.out.println("Something went wrong: " + e.getMessage());
            }
        }
    }

    // Add a new student
    private static void addStudent(
            Scanner scanner,
            StudentService studentService) {

        try {

            Student student = readStudentDetails(scanner);

            boolean result = studentService.addStudent(student);

            if (result) {
                System.out.println("\nStudent added successfully!");
            } else {
                System.out.println("\nUnable to add student.");
            }

        } catch (IllegalArgumentException e) {

            System.out.println("Invalid input: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Unable to add student: " + e.getMessage());
        }
    }

    // View all students
    private static void viewAllStudents(StudentService studentService) {

        try {

            List<Student> students = studentService.getAllStudents();

            if (students.isEmpty()) {

                System.out.println("\nNo students found.");

            } else {

                System.out.println("\n========== STUDENT LIST ==========");

                for (Student student : students) {
                    displayStudent(student);
                }

                System.out.println("Total Students: " + students.size());
            }

        } catch (Exception e) {

            System.out.println("Unable to retrieve students: " + e.getMessage());
        }
    }

    // Search student using ID
    private static void searchStudent(
            Scanner scanner,
            StudentService studentService) {

        try {

            System.out.print("\nEnter Student ID to search: ");

            int id = Integer.parseInt(scanner.nextLine().trim());

            Student student = studentService.searchStudentById(id);

            if (student != null) {

                System.out.println("\nStudent found:");
                displayStudent(student);

            } else {

                System.out.println("Student not found with ID: " + id);
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid ID! Please enter a numeric student ID.");

        } catch (Exception e) {

            System.out.println("Unable to search student: " + e.getMessage());
        }
    }

    // Update an existing student's details
    private static void updateStudent(
            Scanner scanner,
            StudentService studentService) {

        try {

            System.out.print("\nEnter Student ID to update: ");

            int id = Integer.parseInt(scanner.nextLine().trim());

            // First check whether the student exists
            Student student = studentService.searchStudentById(id);

            if (student == null) {

                System.out.println("Student not found with ID: " + id);
                return;
            }

            System.out.println("\nCurrent Student Details:");
            displayStudent(student);

            System.out.println("\nEnter the new student details:");

            // Read the updated details
            Student updatedDetails = readStudentDetails(scanner);

            // Keep the original database ID
            updatedDetails.setId(id);

            boolean result = studentService.updateStudent(updatedDetails);

            if (result) {

                System.out.println("\nStudent updated successfully!");
                displayStudent(updatedDetails);

            } else {

                System.out.println("Unable to update student.");
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid ID! Please enter a numeric student ID.");

        } catch (IllegalArgumentException e) {

            System.out.println("Invalid input: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Unable to update student: " + e.getMessage());
        }
    }

    // Delete an existing student
    private static void deleteStudent(
            Scanner scanner,
            StudentService studentService) {

        try {

            System.out.print("\nEnter Student ID to delete: ");

            int id = Integer.parseInt(scanner.nextLine().trim());

            // Check whether the student exists before deleting
            Student student = studentService.searchStudentById(id);

            if (student == null) {

                System.out.println("Student not found with ID: " + id);
                return;
            }

            // Display the student details before deletion
            System.out.println("\nStudent details selected for deletion:");
            displayStudent(student);

            // Ask for confirmation before permanently deleting
            System.out.print("\nAre you sure you want to delete this student? (Y/N): ");

            String confirmation = scanner.nextLine().trim();

            if (confirmation.equalsIgnoreCase("Y")) {

                boolean result = studentService.deleteStudent(id);

                if (result) {

                    System.out.println("\nStudent deleted successfully!");

                } else {

                    System.out.println("Unable to delete student.");
                }

            } else {

                System.out.println("\nDelete operation cancelled.");
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid ID! Please enter a numeric student ID.");

        } catch (IllegalArgumentException e) {

            System.out.println("Invalid input: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Unable to delete student: " + e.getMessage());
        }
    }

    // Read student information from the console
    private static Student readStudentDetails(Scanner scanner) {

        Student student = new Student();

        System.out.print("Enter First Name: ");
        student.setFirstName(scanner.nextLine());

        System.out.print("Enter Last Name: ");
        student.setLastName(scanner.nextLine());

        System.out.print("Enter Email: ");
        student.setEmail(scanner.nextLine());

        System.out.print("Enter Mobile: ");
        student.setMobile(scanner.nextLine());

        System.out.print("Enter Gender: ");
        student.setGender(scanner.nextLine());

        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        student.setDob(Date.valueOf(scanner.nextLine().trim()));

        System.out.print("Enter Address: ");
        student.setAddress(scanner.nextLine());

        System.out.print("Enter Course: ");
        student.setCourse(scanner.nextLine());

        System.out.print("Enter Admission Date (YYYY-MM-DD): ");
        student.setAdmissionDate(Date.valueOf(scanner.nextLine().trim()));

        return student;
    }

    // Display student information in a readable format
    private static void displayStudent(Student student) {

        System.out.println("---------------------------------------");
        System.out.println("Student ID     : " + student.getId());
        System.out.println("First Name     : " + student.getFirstName());
        System.out.println("Last Name      : " + student.getLastName());
        System.out.println("Email          : " + student.getEmail());
        System.out.println("Mobile         : " + student.getMobile());
        System.out.println("Gender         : " + student.getGender());
        System.out.println("Date of Birth  : " + student.getDob());
        System.out.println("Address        : " + student.getAddress());
        System.out.println("Course         : " + student.getCourse());
        System.out.println("Admission Date : " + student.getAdmissionDate());
        System.out.println("---------------------------------------");
    }
}