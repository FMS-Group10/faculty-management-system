package com.faculty.view.panels;

import com.faculty.controller.CourseController;
import com.faculty.controller.LecturerController;
import com.faculty.model.Course;
import com.faculty.model.Lecturer;
import com.faculty.model.Session;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.ModernTable;
import com.faculty.view.components.OutlineButton;
import com.faculty.view.components.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LecturerCoursesPanel extends JPanel {
    private final Session session;
    private final LecturerController lecturerController = new LecturerController();
    private final CourseController courseController = new CourseController();

    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Course Code", "Course Name", "Credits", "Degree"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    public LecturerCoursesPanel(Session session) {
        this.session = session;

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel header = new JLabel("My Courses");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(UITheme.TEXT_DARK);

        OutlineButton refresh = new OutlineButton("Refresh");
        refresh.setPreferredSize(new Dimension(100, 36));
        refresh.addActionListener(e -> load());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(header, BorderLayout.WEST);
        top.add(refresh, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        ModernTable table = new ModernTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        RoundedPanel card = new RoundedPanel(22);
        card.setBackground(UITheme.SURFACE);
        card.setLayout(new BorderLayout());
        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        load();
    }

    private void load() {
        model.setRowCount(0);
        try {
            Lecturer lecturer = lecturerController.findByUserId(session.getUserId()).orElse(null);
            if (lecturer == null) {
                DialogUtil.error(this, "Lecturer profile not found.");
                return;
            }
            List<Course> list = courseController.listByLecturerId(lecturer.getLecturerId());
            for (Course c : list) {
                model.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredits(), c.getDegreeName()});
            }
        } catch (SQLException ex) {
            DialogUtil.error(this, "Failed to load courses.\n\n" + ex.getMessage());
        }
    }
}
