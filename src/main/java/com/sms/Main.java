
package com.sms;

import com.sms.model.Student;
import com.sms.service.StudentService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Create the service object to access Student operations
        StudentService studentService = new StudentService();

        // Keep showing the menu until the user chooses Exit
        while (true) {

            System.out.println("\n================================");
            System.out.println("     Student Management System");
            System.out.println("================================");

            System.out.println("\nChoose an option:");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            try {

                switch (choice) {

                    case "1":
                        addStudent(sc, studentService);
                        break;

                    case "2":
                        viewAllStudents(studentService);
                        break;

                    case "3":
                        searchStudent(sc, studentService);
                        break;

                    case "4":
                        System.out.println("\nThank you for using Student Management System!");
                        sc.close();
                        return;

                    default:
                        System.out.println("\nInvalid choice! Please select 1, 2, 3, or 4.");
                }

            } catch (Exception e) {
                System.out.println("\nError: " + e.getMessage());
            }
        }
    }

    // ADD STUDENT
    private static void addStudent(Scanner sc, StudentService studentService) {

        try {

            Student student = new Student();

            System.out.print("Enter First Name: ");
            student.setFirstName(sc.nextLine());

            System.out.print("Enter Last Name: ");
            student.setLastName(sc.nextLine());

            System.out.print("Enter Email: ");
            student.setEmail(sc.nextLine());

            System.out.print("Enter Mobile: ");
            student.setMobile(sc.nextLine());

            System.out.print("Enter Gender: ");
            student.setGender(sc.nextLine());

            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            student.setDob(Date.valueOf(sc.nextLine()));

            System.out.print("Enter Address: ");
            student.setAddress(sc.nextLine());

            System.out.print("Enter Course: ");
            student.setCourse(sc.nextLine());

            System.out.print("Enter Admission Date (YYYY-MM-DD): ");
            student.setAdmissionDate(Date.valueOf(sc.nextLine()));

            // Call service layer to validate and save the student
            boolean isAdded = studentService.addStudent(student);

            if (isAdded) {
                System.out.println("\nStudent added successfully!");
            } else {
                System.out.println("\nStudent could not be added.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\nInvalid input. Please check your details.");
            System.out.println("Date format must be YYYY-MM-DD (example: 2000-02-02).");
        } catch (Exception e) {
            System.out.println("\nUnable to add student: " + e.getMessage());
        }
    }

    // VIEW ALL STUDENTS
    private static void viewAllStudents(StudentService studentService) {

        try {

            List<Student> students = studentService.getAllStudents();

            System.out.println("\n==================== STUDENT RECORDS ====================");

            if (students.isEmpty()) {
                System.out.println("No student records found.");
                return;
            }

            // Display each student returned by the database
            for (Student student : students) {
                displayStudent(student);
            }

            System.out.println("Total students: " + students.size());

        } catch (Exception e) {
            System.out.println("\nUnable to retrieve students: " + e.getMessage());
        }
    }

    // SEARCH STUDENT BY ID
    private static void searchStudent(Scanner sc, StudentService studentService) {

        try {

            System.out.print("\nEnter Student ID to search: ");

            // Convert the ID entered as text into an integer
            int id = Integer.parseInt(sc.nextLine());

            // Ask the service layer to search for this student
            Student student = studentService.searchStudentById(id);

            if (student != null) {

                System.out.println("\n==================== STUDENT FOUND ====================");
                displayStudent(student);

            } else {

                System.out.println("\nStudent not found with ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.out.println("\nInvalid ID! Please enter a numeric student ID.");

        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {
            System.out.println("\nUnable to search student: " + e.getMessage());
        }
    }

    // COMMON METHOD TO DISPLAY STUDENT DETAILS
    
    private static void displayStudent(Student student) {

        System.out.println("----------------------------------------");
        System.out.println("ID             : " + student.getId());
        System.out.println("First Name     : " + student.getFirstName());
        System.out.println("Last Name      : " + student.getLastName());
        System.out.println("Email          : " + student.getEmail());
        System.out.println("Mobile         : " + student.getMobile());
        System.out.println("Gender         : " + student.getGender());
        System.out.println("Date of Birth  : " + student.getDob());
        System.out.println("Address        : " + student.getAddress());
        System.out.println("Course         : " + student.getCourse());
        System.out.println("Admission Date : " + student.getAdmissionDate());
        System.out.println("----------------------------------------");
    }
}
