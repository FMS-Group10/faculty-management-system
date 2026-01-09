package com.faculty.view.panels;

import com.faculty.controller.EnrollmentController;
import com.faculty.controller.StudentController;
import com.faculty.model.Enrollment;
import com.faculty.model.Session;
import com.faculty.model.Student;
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

public class StudentEnrollmentsPanel extends JPanel {
    private final Session session;
    private final StudentController studentController = new StudentController();
    private final EnrollmentController enrollmentController = new EnrollmentController();

    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Course Code", "Course Name", "Grade"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    public StudentEnrollmentsPanel(Session session) {
        this.session = session;

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel header = new JLabel("My Courses & Grades");
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
            Student student = studentController.findByUserId(session.getUserId()).orElse(null);
            if (student == null) {
                DialogUtil.error(this, "Student profile not found.");
                return;
            }
            List<Enrollment> list = enrollmentController.listByStudentId(student.getStudentId());
            for (Enrollment e : list) {
                model.addRow(new Object[]{e.getCourseCode(), e.getCourseName(), e.getGrade() == null ? "" : e.getGrade()});
            }
        } catch (SQLException ex) {
            DialogUtil.error(this, "Failed to load enrollments.\n\n" + ex.getMessage());
        }
    }
}
