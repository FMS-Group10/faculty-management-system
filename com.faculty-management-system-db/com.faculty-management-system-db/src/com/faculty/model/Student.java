package com.faculty.model;

public class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private int userId;
    private String email;
    private String mobile;
    private int degreeId;

    // Convenience (joined)
    private String degreeName;
    private String username;

    public Student() {}

    public Student(int studentId, String firstName, String lastName, int userId, String email, String mobile, int degreeId) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
        this.mobile = mobile;
        this.degreeId = degreeId;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public int getDegreeId() { return degreeId; }
    public void setDegreeId(int degreeId) { this.degreeId = degreeId; }

    public String getDegreeName() { return degreeName; }
    public void setDegreeName(String degreeName) { this.degreeName = degreeName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName); }
}
