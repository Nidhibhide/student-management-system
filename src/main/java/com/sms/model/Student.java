
package com.sms.model;

import java.sql.Date;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String gender;
    private Date dob;
    private String address;
    private String course;
    private Date admissionDate;

    public Student() {
    }

    public Student(String firstName, String lastName, String email,
                   String mobile, String gender, Date dob,
                   String address, String course, Date admissionDate) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.course = course;
        this.admissionDate = admissionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    @Override
    public String toString() {
        return "Student ID: " + id +
                "\nName: " + firstName + " " + lastName +
                "\nEmail: " + email +
                "\nMobile: " + mobile +
                "\nGender: " + gender +
                "\nDOB: " + dob +
                "\nAddress: " + address +
                "\nCourse: " + course +
                "\nAdmission Date: " + admissionDate;
    }
}
