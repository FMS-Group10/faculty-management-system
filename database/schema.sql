-- Create the Database
CREATE DATABASE faculty_management_system;
USE faculty_management_system;

-- 1. Users Table (For Authentication) 
-- Stores login credentials for Admins, Students, and Lecturers
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, 
    role ENUM('ADMIN', 'STUDENT', 'LECTURER') NOT NULL
);

-- 2. Departments Table 
-- Tracks departments and their Head of Department (HOD)
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    head_of_department VARCHAR(100)
);

-- 3. Degrees Table 
-- Linked to departments
CREATE TABLE degrees (
    degree_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL
);

-- 4. Lecturers Table 
-- Linked to a specific User account and Department
CREATE TABLE lecturers (
    lecturer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department_id INT,
    user_id INT UNIQUE, -- Links to the login account
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 5. Students Table 
-- Linked to a User account and their enrolled Degree
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    student_reg_no VARCHAR(20) UNIQUE NOT NULL, 
    email VARCHAR(100) UNIQUE NOT NULL,
    mobile VARCHAR(15),
    degree_id INT,
    user_id INT UNIQUE, -- Links to the login account
    FOREIGN KEY (degree_id) REFERENCES degrees(degree_id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 6. Courses Table
-- Courses are taught by a Lecturer and usually belong to a Degree/Dept
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) UNIQUE NOT NULL, 
    name VARCHAR(100) NOT NULL,
    credits INT DEFAULT 3,
    lecturer_id INT,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id) ON DELETE SET NULL
);

-- 7. Enrollments Table 
-- The "Many-to-Many" link between Students and Courses, storing grades
CREATE TABLE enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    course_id INT,
    grade VARCHAR(2), 
    semester VARCHAR(20),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);