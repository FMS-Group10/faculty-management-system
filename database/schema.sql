-- Faculty Management System schema (MySQL)

CREATE DATABASE IF NOT EXISTS FMS;
USE FMS;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS timetable;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS lecturers;
DROP TABLE IF EXISTS degrees;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- USERS
CREATE TABLE users (
  user_id   INT AUTO_INCREMENT PRIMARY KEY,
  username  VARCHAR(50) NOT NULL UNIQUE,
  password  VARCHAR(255) NOT NULL,
  role      ENUM('Admin','Student','Lecturer') NOT NULL
);

-- DEPARTMENTS
CREATE TABLE departments (
  dept_id INT AUTO_INCREMENT PRIMARY KEY,
  name    VARCHAR(100) NOT NULL UNIQUE
);

-- DEGREES
CREATE TABLE degrees (
  degree_id      INT AUTO_INCREMENT PRIMARY KEY,
  degree_name    VARCHAR(120) NOT NULL,
  dept_id        INT NOT NULL,
  duration_years INT NOT NULL,
  CONSTRAINT fk_degrees_departments
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- LECTURERS
CREATE TABLE lecturers (
  lecturer_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id     INT NOT NULL UNIQUE,
  first_name  VARCHAR(80) NOT NULL,
  last_name   VARCHAR(80) NOT NULL,
  dept_id     INT NOT NULL,
  email       VARCHAR(120) NOT NULL,
  mobile      VARCHAR(30) NOT NULL,
  CONSTRAINT fk_lecturers_users
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_lecturers_departments
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- STUDENTS
CREATE TABLE students (
  student_id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(80) NOT NULL,
  last_name  VARCHAR(80) NOT NULL,
  user_id    INT NOT NULL UNIQUE,
  email      VARCHAR(120) NOT NULL,
  mobile     VARCHAR(30) NOT NULL,
  degree_id  INT NOT NULL,
  CONSTRAINT fk_students_users
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_students_degrees
    FOREIGN KEY (degree_id) REFERENCES degrees(degree_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- COURSES
-- NOTE: ERD shows course_code as int. Here we keep it as INT PK.
CREATE TABLE courses (
  course_code INT PRIMARY KEY,
  course_name VARCHAR(150) NOT NULL,
  credits     INT NOT NULL,
  lecturer_id INT NULL,
  degree_id   INT NOT NULL,
  CONSTRAINT fk_courses_lecturers
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_courses_degrees
    FOREIGN KEY (degree_id) REFERENCES degrees(degree_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ENROLLMENTS
CREATE TABLE enrollments (
  enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
  student_id    INT NOT NULL,
  course_code   INT NOT NULL,
  grade         VARCHAR(10) NULL,
  UNIQUE KEY uq_student_course (student_id, course_code),
  CONSTRAINT fk_enrollments_students
    FOREIGN KEY (student_id) REFERENCES students(student_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_enrollments_courses
    FOREIGN KEY (course_code) REFERENCES courses(course_code)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- TIMETABLE
CREATE TABLE timetable (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  course_code INT NOT NULL,
  day_of_week ENUM('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday') NOT NULL,
  start_time  TIME NOT NULL,
  end_time    TIME NOT NULL,
  CONSTRAINT fk_timetable_courses
    FOREIGN KEY (course_code) REFERENCES courses(course_code)
    ON UPDATE CASCADE ON DELETE CASCADE
);
