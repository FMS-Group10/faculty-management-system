package com.faculty.model;

public class Lecturer {
    private int lecturerId;
    private int userId;
    private String firstName;
    private String lastName;
    private int deptId;
    private String email;
    private String mobile;

    // joined
    private String deptName;
    private String username;

    public Lecturer() {}

    public Lecturer(int lecturerId, int userId, String firstName, String lastName, int deptId, String email, String mobile) {
        this.lecturerId = lecturerId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deptId = deptId;
        this.email = email;
        this.mobile = mobile;
    }

    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName); }
}
