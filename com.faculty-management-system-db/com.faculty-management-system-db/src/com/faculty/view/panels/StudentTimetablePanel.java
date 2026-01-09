package com.faculty.view.panels;

import com.faculty.controller.StudentController;
import com.faculty.controller.TimetableController;
import com.faculty.model.Session;
import com.faculty.model.Student;
import com.faculty.model.TimetableEntry;
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

public class StudentTimetablePanel extends JPanel {
    private final Session session;
    private final StudentController studentController = new StudentController();
    private final TimetableController timetableController = new TimetableController();

    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Course", "Day", "Start", "End"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    public StudentTimetablePanel(Session session) {
        this.session = session;

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel header = new JLabel("My Timetable");
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
            List<TimetableEntry> list = timetableController.listByDegreeId(student.getDegreeId());
            for (TimetableEntry t : list) {
                model.addRow(new Object[]{
                        t.getCourseName() + " (" + t.getCourseCode() + ")",
                        t.getDayOfWeek(),
                        t.getStartTime() == null ? "" : t.getStartTime().toString(),
                        t.getEndTime() == null ? "" : t.getEndTime().toString()
                });
            }
        } catch (SQLException ex) {
            DialogUtil.error(this, "Failed to load timetable.\n\n" + ex.getMessage());
        }
    }
}
