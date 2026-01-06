package com.faculty.view;

import com.faculty.model.Session;
import com.faculty.view.panels.LecturerCoursesPanel;
import com.faculty.view.panels.LecturerProfilePanel;
import com.faculty.view.panels.LecturerTimetablePanel;

public class LecturerDashboardView extends DashboardFrame {

    public LecturerDashboardView(Session session) {
        super(session, "Lecturer Dashboard - Faculty Management System");
    }

    @Override
    protected void buildPages() {
        addNavItem("profile", "My Profile", new LecturerProfilePanel(session));
        addNavItem("courses", "My Courses", new LecturerCoursesPanel(session));
        addNavItem("timetable", "Timetable", new LecturerTimetablePanel(session));
    }
}
