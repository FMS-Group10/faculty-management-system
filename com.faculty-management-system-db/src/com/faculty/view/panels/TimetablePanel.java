package com.faculty.view.panels;

import com.faculty.controller.CourseController;
import com.faculty.controller.TimetableController;
import com.faculty.model.Course;
import com.faculty.model.TimetableEntry;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TimetablePanel extends BaseCrudPanel {
    private final TimetableController controller = new TimetableController();
    private final CourseController courseController = new CourseController();

    private static final String[] DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    public TimetablePanel() {
        super("Timetable Management", new String[]{"ID", "Course", "Day", "Start", "End"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<TimetableEntry> list = controller.listAll();
        for (TimetableEntry t : list) {
            String hay = (t.getId() + " " + t.getCourseCode() + " " + t.getCourseName() + " " + t.getDayOfWeek()).toLowerCase();
            if (!filter.isEmpty() && !hay.contains(filter.toLowerCase())) continue;
            model.addRow(new Object[]{t.getId(), t.getCourseName() + " (" + t.getCourseCode() + ")", t.getDayOfWeek(),
                    t.getStartTime() == null ? "" : t.getStartTime().toString(),
                    t.getEndTime() == null ? "" : t.getEndTime().toString()});
        }
    }

    @Override
    protected void onAdd() {
        try {
            List<Course> courses = courseController.listAll();
            TimetableForm form = new TimetableForm(null, courses);
            if (!form.showDialog(this, "Add Timetable Entry")) return;
            controller.create(form.getEntry());
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Create failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        int id = (int) model.getValueAt(row, 0);
        try {
            TimetableEntry existing = controller.findById(id).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "Timetable entry not found.");
                return;
            }
            List<Course> courses = courseController.listAll();
            TimetableForm form = new TimetableForm(existing, courses);
            if (!form.showDialog(this, "Edit Timetable Entry")) return;
            controller.update(form.getEntry());
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int id = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete timetable entry ID " + id + "?")) return;
        try {
            controller.delete(id);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    private static class TimetableForm {
        private final JComboBox<CourseItem> courseCombo;
        private final JComboBox<String> dayCombo = new JComboBox<>(DAYS);
        private final StyledTextField start = new StyledTextField();
        private final StyledTextField end = new StyledTextField();
        private final TimetableEntry base;

        TimetableForm(TimetableEntry base, List<Course> courses) {
            this.base = base;
            courseCombo = new JComboBox<>(courses.stream().map(CourseItem::new).toArray(CourseItem[]::new));
            courseCombo.setFont(UITheme.FONT_REGULAR);
            dayCombo.setFont(UITheme.FONT_REGULAR);

            start.setPreferredSize(new Dimension(320, 38));
            end.setPreferredSize(new Dimension(320, 38));
            start.setText("08:00");
            end.setText("10:00");

            if (base != null) {
                selectCourse(base.getCourseCode());
                dayCombo.setSelectedItem(base.getDayOfWeek());
                start.setText(base.getStartTime() == null ? "" : base.getStartTime().toString());
                end.setText(base.getEndTime() == null ? "" : base.getEndTime().toString());
            }
        }

        private void selectCourse(int courseCode) {
            for (int i = 0; i < courseCombo.getItemCount(); i++) {
                if (courseCombo.getItemAt(i).course.getCourseCode() == courseCode) {
                    courseCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        boolean showDialog(Component parent, String title) {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.insets = new Insets(6, 6, 6, 6);
            c.anchor = GridBagConstraints.WEST;

            int y = 0;
            addRow(panel, c, y++, "Course", courseCombo);
            addRow(panel, c, y++, "Day", dayCombo);
            addRow(panel, c, y++, "Start Time (HH:MM)", start);
            addRow(panel, c, y++, "End Time (HH:MM)", end);

            int ok = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return false;

            // validate times
            try {
                parse(start.getText().trim());
                parse(end.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(parent, "Invalid time format. Use HH:MM (e.g., 08:30)");
                return false;
            }
            return true;
        }

        private LocalTime parse(String s) {
            if (s.length() == 5) return LocalTime.parse(s);
            // allow HH:MM:SS
            return LocalTime.parse(s);
        }

        private void addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
            c.gridy = row * 2;
            JLabel l = new JLabel(label);
            l.setForeground(UITheme.TEXT_MUTED);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.add(l, c);

            c.gridy = row * 2 + 1;
            field.setPreferredSize(new Dimension(320, 38));
            panel.add(field, c);
        }

        TimetableEntry getEntry() {
            TimetableEntry t = base == null ? new TimetableEntry() : base;
            CourseItem c = (CourseItem) courseCombo.getSelectedItem();
            t.setCourseCode(c == null ? 0 : c.course.getCourseCode());
            t.setDayOfWeek((String) dayCombo.getSelectedItem());
            t.setStartTime(parse(start.getText().trim()));
            t.setEndTime(parse(end.getText().trim()));
            return t;
        }

        private static class CourseItem {
            final Course course;
            CourseItem(Course c) { this.course = c; }
            @Override public String toString() { return course.getCourseCode() + " - " + course.getCourseName(); }
        }
    }
}
