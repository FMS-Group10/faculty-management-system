package com.faculty.view.panels;

import com.faculty.controller.CourseController;
import com.faculty.controller.EnrollmentController;
import com.faculty.controller.StudentController;
import com.faculty.model.Course;
import com.faculty.model.Enrollment;
import com.faculty.model.Student;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EnrollmentsPanel extends BaseCrudPanel {
    private final EnrollmentController controller = new EnrollmentController();
    private final StudentController studentController = new StudentController();
    private final CourseController courseController = new CourseController();

    public EnrollmentsPanel() {
        super("Enrollment Management", new String[]{"Enrollment ID", "Student", "Course", "Grade"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<Enrollment> list = controller.listAll();
        for (Enrollment e : list) {
            String hay = (e.getEnrollmentId() + " " + e.getStudentName() + " " + e.getCourseName() + " " + e.getGrade()).toLowerCase();
            if (!filter.isEmpty() && !hay.contains(filter.toLowerCase())) continue;
            model.addRow(new Object[]{e.getEnrollmentId(), e.getStudentName(), e.getCourseName(), e.getGrade() == null ? "" : e.getGrade()});
        }
    }

    @Override
    protected void onAdd() {
        try {
            List<Student> students = studentController.listAll();
            List<Course> courses = courseController.listAll();
            EnrollmentForm form = new EnrollmentForm(null, students, courses);
            if (!form.showDialog(this, "Add Enrollment")) return;
            controller.create(form.getEnrollment());
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
            Enrollment existing = controller.findById(id).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "Enrollment not found.");
                return;
            }
            List<Student> students = studentController.listAll();
            List<Course> courses = courseController.listAll();
            EnrollmentForm form = new EnrollmentForm(existing, students, courses);
            if (!form.showDialog(this, "Edit Enrollment")) return;
            controller.update(form.getEnrollment());
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int id = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete enrollment ID " + id + "?")) return;
        try {
            controller.delete(id);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    private static class EnrollmentForm {
        private final JComboBox<StudentItem> studentCombo;
        private final JComboBox<CourseItem> courseCombo;
        private final StyledTextField grade = new StyledTextField();
        private final Enrollment base;

        EnrollmentForm(Enrollment base, List<Student> students, List<Course> courses) {
            this.base = base;
            studentCombo = new JComboBox<>(students.stream().map(StudentItem::new).toArray(StudentItem[]::new));
            courseCombo = new JComboBox<>(courses.stream().map(CourseItem::new).toArray(CourseItem[]::new));
            studentCombo.setFont(UITheme.FONT_REGULAR);
            courseCombo.setFont(UITheme.FONT_REGULAR);
            grade.setPreferredSize(new Dimension(320, 38));

            if (base != null) {
                grade.setText(base.getGrade() == null ? "" : base.getGrade());
                selectStudent(base.getStudentId());
                selectCourse(base.getCourseCode());
            }
        }

        private void selectStudent(int studentId) {
            for (int i = 0; i < studentCombo.getItemCount(); i++) {
                if (studentCombo.getItemAt(i).student.getStudentId() == studentId) {
                    studentCombo.setSelectedIndex(i);
                    break;
                }
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
            addRow(panel, c, y++, "Student", studentCombo);
            addRow(panel, c, y++, "Course", courseCombo);
            addRow(panel, c, y++, "Grade", grade);

            int ok = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return ok == JOptionPane.OK_OPTION;
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

        Enrollment getEnrollment() {
            Enrollment e = base == null ? new Enrollment() : base;
            StudentItem s = (StudentItem) studentCombo.getSelectedItem();
            CourseItem c = (CourseItem) courseCombo.getSelectedItem();
            e.setStudentId(s == null ? 0 : s.student.getStudentId());
            e.setCourseCode(c == null ? 0 : c.course.getCourseCode());
            e.setGrade(grade.getText().trim().isEmpty() ? null : grade.getText().trim());
            return e;
        }

        private static class StudentItem {
            final Student student;
            StudentItem(Student s) { this.student = s; }
            @Override public String toString() { return student.getStudentId() + " - " + student.getFullName(); }
        }

        private static class CourseItem {
            final Course course;
            CourseItem(Course c) { this.course = c; }
            @Override public String toString() { return course.getCourseCode() + " - " + course.getCourseName(); }
        }
    }
}
