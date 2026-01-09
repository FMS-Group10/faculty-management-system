USE faculty_management_system;

-- DEPARTMENTS
INSERT INTO departments(name) VALUES
('Computer Science'),
('Engineering Technology'),
('Information Systems');

-- DEGREES
INSERT INTO degrees(degree_name, dept_id, duration_years) VALUES
('BSc in Computer Science', 1, 4),
('BEng in Engineering Technology', 2, 4),
('BSc in Information Systems', 3, 3);

-- USERS
INSERT INTO users(username, password, role) VALUES
('admin', 'admin123', 'Admin'),
('student1', 'student123', 'Student'),
('lecturer1', 'lecturer123', 'Lecturer');

-- LECTURER PROFILE (bind to lecturer1)
INSERT INTO lecturers(user_id, first_name, last_name, dept_id, email, mobile)
SELECT user_id, 'Nimal', 'Perera', 1, 'nimal.perera@university.lk', '0771234567'
FROM users WHERE username='lecturer1';

-- STUDENT PROFILE (bind to student1)
INSERT INTO students(user_id, first_name, last_name, email, mobile, degree_id)
SELECT user_id, 'Kumar', 'Sangakkara', 'kumar.s@student.lk', '0777654321', 2
FROM users WHERE username='student1';

-- COURSES
-- Use lecturer_id from inserted lecturers
SET @lec_id = (SELECT lecturer_id FROM lecturers LIMIT 1);
INSERT INTO courses(course_code, course_name, credits, lecturer_id, degree_id) VALUES
(21062, 'Object Oriented Programming', 3, @lec_id, 2),
(21052, 'Software Engineering', 3, @lec_id, 2),
(21042, 'Web Technologies', 2, @lec_id, 2);

-- ENROLLMENTS (for student1)
SET @stu_id = (SELECT student_id FROM students LIMIT 1);
INSERT INTO enrollments(student_id, course_code, grade) VALUES
(@stu_id, 21062, 'A+'),
(@stu_id, 21052, 'A'),
(@stu_id, 21042, 'B+');

-- TIMETABLE
INSERT INTO timetable(course_code, day_of_week, start_time, end_time) VALUES
(21062, 'Monday',    '08:00:00', '10:00:00'),
(21062, 'Wednesday', '10:00:00', '12:00:00'),
(21052, 'Tuesday',   '08:00:00', '10:00:00'),
(21042, 'Thursday',  '13:00:00', '15:00:00');
