package com.faculty.view;

import com.faculty.model.Session;
import com.faculty.view.panels.StudentEnrollmentsPanel;
import com.faculty.view.panels.StudentProfilePanel;
import com.faculty.view.panels.StudentTimetablePanel;

public class StudentDashboardView extends DashboardFrame {

    public StudentDashboardView(Session session) {
        super(session, "Student Dashboard - Faculty Management System");
    }

    @Override
    protected void buildPages() {
        addNavItem("profile", "My Profile", new StudentProfilePanel(session));
        addNavItem("timetable", "Timetable", new StudentTimetablePanel(session));
        addNavItem("courses", "Courses & Grades", new StudentEnrollmentsPanel(session));
    }
}
