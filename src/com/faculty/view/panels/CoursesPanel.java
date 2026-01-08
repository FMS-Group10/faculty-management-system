package com.faculty.view.panels;

import com.faculty.controller.CourseController;
import com.faculty.controller.DegreeController;
import com.faculty.controller.LecturerController;
import com.faculty.model.Course;
import com.faculty.model.Degree;
import com.faculty.model.Lecturer;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CoursesPanel extends BaseCrudPanel {
    private final CourseController controller = new CourseController();
    private final LecturerController lecturerController = new LecturerController();
    private final DegreeController degreeController = new DegreeController();

    public CoursesPanel() {
        super("Course Management", new String[]{"Course Code", "Course Name", "Credits", "Lecturer", "Degree"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<Course> list = controller.listAll();
        for (Course c : list) {
            String hay = (c.getCourseCode() + " " + c.getCourseName() + " " + c.getCredits() + " " + c.getLecturerName() + " " + c.getDegreeName()).toLowerCase();
            if (!filter.isEmpty() && !hay.contains(filter.toLowerCase())) continue;
            model.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredits(),
                    c.getLecturerName() == null ? "-" : c.getLecturerName(), c.getDegreeName()});
        }
    }

    @Override
    protected void onAdd() {
        try {
            List<Lecturer> lecturers = lecturerController.listAll();
            List<Degree> degrees = degreeController.listAll();
            CourseForm form = new CourseForm(null, lecturers, degrees);
            if (!form.showDialog(this, "Add Course")) return;
            controller.create(form.getCourse());
            reload();
            DialogUtil.info(this, "Course created successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Create failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        int code = (int) model.getValueAt(row, 0);
        try {
            Course existing = controller.findByCode(code).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "Course not found.");
                return;
            }
            List<Lecturer> lecturers = lecturerController.listAll();
            List<Degree> degrees = degreeController.listAll();
            CourseForm form = new CourseForm(existing, lecturers, degrees);
            if (!form.showDialog(this, "Edit Course")) return;
            controller.update(form.getCourse());
            reload();
            DialogUtil.info(this, "Course updated successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int code = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete course code " + code + "?")) return;
        try {
            controller.delete(code);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    private static class CourseForm {
        private final StyledTextField code = new StyledTextField();
        private final StyledTextField name = new StyledTextField();
        private final StyledTextField credits = new StyledTextField();
        private final JComboBox<LecturerItem> lecturerCombo;
        private final JComboBox<DegreeItem> degreeCombo;
        private final Course base;

        CourseForm(Course base, List<Lecturer> lecturers, List<Degree> degrees) {
            this.base = base;

            LecturerItem none = LecturerItem.none();
            LecturerItem[] lecItems = new LecturerItem[lecturers.size() + 1];
            lecItems[0] = none;
            for (int i = 0; i < lecturers.size(); i++) lecItems[i + 1] = new LecturerItem(lecturers.get(i));
            lecturerCombo = new JComboBox<>(lecItems);
            degreeCombo = new JComboBox<>(degrees.stream().map(DegreeItem::new).toArray(DegreeItem[]::new));

            lecturerCombo.setFont(UITheme.FONT_REGULAR);
            degreeCombo.setFont(UITheme.FONT_REGULAR);

            code.setPreferredSize(new Dimension(320, 38));
            name.setPreferredSize(new Dimension(320, 38));
            credits.setPreferredSize(new Dimension(320, 38));

            if (base != null) {
                code.setText(String.valueOf(base.getCourseCode()));
                code.setEnabled(false);
                name.setText(base.getCourseName());
                credits.setText(String.valueOf(base.getCredits()));

                selectLecturer(base.getLecturerId());
                selectDegree(base.getDegreeId());
            }
        }

        private void selectLecturer(Integer lecturerId) {
            if (lecturerId == null) {
                lecturerCombo.setSelectedIndex(0);
                return;
            }
            for (int i = 0; i < lecturerCombo.getItemCount(); i++) {
                LecturerItem it = lecturerCombo.getItemAt(i);
                if (it.lecturer != null && it.lecturer.getLecturerId() == lecturerId) {
                    lecturerCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        private void selectDegree(int degreeId) {
            for (int i = 0; i < degreeCombo.getItemCount(); i++) {
                if (degreeCombo.getItemAt(i).degree.getDegreeId() == degreeId) {
                    degreeCombo.setSelectedIndex(i);
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
            addRow(panel, c, y++, "Course Code", code);
            addRow(panel, c, y++, "Course Name", name);
            addRow(panel, c, y++, "Credits", credits);
            addRow(panel, c, y++, "Lecturer", lecturerCombo);
            addRow(panel, c, y++, "Degree", degreeCombo);

            int ok = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return false;

            if (code.getText().trim().isEmpty() || name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Course code and name are required.");
                return false;
            }
            try {
                Integer.parseInt(code.getText().trim());
                Integer.parseInt(credits.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Course code and credits must be numbers.");
                return false;
            }
            return true;
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

        Course getCourse() {
            Course c = base == null ? new Course() : base;
            c.setCourseCode(Integer.parseInt(code.getText().trim()));
            c.setCourseName(name.getText().trim());
            c.setCredits(Integer.parseInt(credits.getText().trim()));

            LecturerItem lec = (LecturerItem) lecturerCombo.getSelectedItem();
            c.setLecturerId(lec == null ? null : lec.getLecturerIdOrNull());

            DegreeItem deg = (DegreeItem) degreeCombo.getSelectedItem();
            c.setDegreeId(deg == null ? 0 : deg.degree.getDegreeId());
            return c;
        }

        private static class LecturerItem {
            final Lecturer lecturer;
            final boolean none;

            private LecturerItem(Lecturer lecturer, boolean none) {
                this.lecturer = lecturer;
                this.none = none;
            }

            static LecturerItem none() { return new LecturerItem(null, true); }
            LecturerItem(Lecturer l) { this(l, false); }

            Integer getLecturerIdOrNull() { return (none || lecturer == null) ? null : lecturer.getLecturerId(); }

            @Override
            public String toString() {
                if (none) return "(None)";
                return lecturer.getLecturerId() + " - " + lecturer.getFullName();
            }
        }

        private static class DegreeItem {
            final Degree degree;
            DegreeItem(Degree d) { this.degree = d; }
            @Override public String toString() { return degree.getDegreeId() + " - " + degree.getDegreeName(); }
        }
    }
}
