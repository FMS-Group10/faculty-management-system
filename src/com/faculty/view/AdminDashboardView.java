package com.faculty.view;

import com.faculty.model.Session;
import com.faculty.view.panels.*;

public class AdminDashboardView extends DashboardFrame {

    public AdminDashboardView(Session session) {
        super(session, "Admin Dashboard - Faculty Management System");
    }

    @Override
    protected void buildPages() {
        addNavItem("overview", "Overview", new AdminOverviewPanel());
        addNavItem("users", "Users", new UsersPanel());
        addNavItem("students", "Students", new StudentsPanel());
        addNavItem("lecturers", "Lecturers", new LecturersPanel());
        addNavItem("courses", "Courses", new CoursesPanel());
        addNavItem("departments", "Departments", new DepartmentsPanel());
        addNavItem("degrees", "Degrees", new DegreesPanel());
        addNavItem("enrollments", "Enrollments", new EnrollmentsPanel());
        addNavItem("timetable", "Timetable", new TimetablePanel());
    }
}
