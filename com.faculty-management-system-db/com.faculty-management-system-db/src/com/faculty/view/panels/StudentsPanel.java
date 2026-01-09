package com.faculty.view.panels;

import com.faculty.controller.DegreeController;
import com.faculty.controller.StudentController;
import com.faculty.controller.UserController;
import com.faculty.model.Degree;
import com.faculty.model.Role;
import com.faculty.model.Student;
import com.faculty.model.User;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentsPanel extends BaseCrudPanel {
    private final StudentController controller = new StudentController();
    private final UserController userController = new UserController();
    private final DegreeController degreeController = new DegreeController();

    public StudentsPanel() {
        super("Student Management", new String[]{"Student ID", "Username", "Full Name", "Email", "Mobile", "Degree"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<Student> list = controller.listAll();
        for (Student s : list) {
            String hay = (s.getStudentId() + " " + s.getUsername() + " " + s.getFullName() + " " + s.getEmail() + " " + s.getMobile() + " " + s.getDegreeName()).toLowerCase();
            if (!filter.isEmpty() && !hay.contains(filter.toLowerCase())) continue;
            model.addRow(new Object[]{s.getStudentId(), s.getUsername(), s.getFullName(), s.getEmail(), s.getMobile(), s.getDegreeName()});
        }
    }

    @Override
    protected void onAdd() {
        try {
            List<User> studentUsers = userController.listByRole(Role.Student);
            List<Degree> degrees = degreeController.listAll();
            StudentForm form = new StudentForm(null, studentUsers, degrees);
            if (!form.showDialog(this, "Add Student")) return;
            controller.create(form.getStudent());
            reload();
            DialogUtil.info(this, "Student created successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Create failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        int studentId = (int) model.getValueAt(row, 0);
        try {
            Student existing = controller.findById(studentId).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "Student not found.");
                return;
            }
            List<User> studentUsers = userController.listByRole(Role.Student);
            List<Degree> degrees = degreeController.listAll();
            StudentForm form = new StudentForm(existing, studentUsers, degrees);
            if (!form.showDialog(this, "Edit Student")) return;
            controller.update(form.getStudent());
            reload();
            DialogUtil.info(this, "Student updated successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int studentId = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete student ID " + studentId + "?")) return;
        try {
            controller.delete(studentId);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    private static class StudentForm {
        private final JComboBox<UserItem> userCombo;
        private final StyledTextField first = new StyledTextField();
        private final StyledTextField last = new StyledTextField();
        private final StyledTextField email = new StyledTextField();
        private final StyledTextField mobile = new StyledTextField();
        private final JComboBox<DegreeItem> degreeCombo;
        private final Student base;

        StudentForm(Student base, List<User> users, List<Degree> degrees) {
            this.base = base;
            userCombo = new JComboBox<>(users.stream().map(UserItem::new).toArray(UserItem[]::new));
            degreeCombo = new JComboBox<>(degrees.stream().map(DegreeItem::new).toArray(DegreeItem[]::new));
            userCombo.setFont(UITheme.FONT_REGULAR);
            degreeCombo.setFont(UITheme.FONT_REGULAR);

            if (base != null) {
                first.setText(base.getFirstName());
                last.setText(base.getLastName());
                email.setText(base.getEmail());
                mobile.setText(base.getMobile());

                // lock user_id on edit by default
                selectUser(base.getUserId());
                userCombo.setEnabled(false);

                selectDegree(base.getDegreeId());
            }
        }

        private void selectUser(int userId) {
            for (int i = 0; i < userCombo.getItemCount(); i++) {
                if (userCombo.getItemAt(i).user.getUserId() == userId) {
                    userCombo.setSelectedIndex(i);
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
            addRow(panel, c, y++, "User (Student)", userCombo);
            addRow(panel, c, y++, "First Name", sized(first));
            addRow(panel, c, y++, "Last Name", sized(last));
            addRow(panel, c, y++, "Email", sized(email));
            addRow(panel, c, y++, "Mobile", sized(mobile));
            addRow(panel, c, y++, "Degree", degreeCombo);

            int ok = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return false;

            if (first.getText().trim().isEmpty() || last.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "First name and last name are required.");
                return false;
            }
            return true;
        }

        private JComponent sized(JComponent comp) {
            comp.setPreferredSize(new Dimension(320, 38));
            return comp;
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

        Student getStudent() {
            Student s = base == null ? new Student() : base;
            UserItem u = (UserItem) userCombo.getSelectedItem();
            DegreeItem d = (DegreeItem) degreeCombo.getSelectedItem();
            s.setUserId(u == null ? 0 : u.user.getUserId());
            s.setFirstName(first.getText().trim());
            s.setLastName(last.getText().trim());
            s.setEmail(email.getText().trim());
            s.setMobile(mobile.getText().trim());
            s.setDegreeId(d == null ? 0 : d.degree.getDegreeId());
            return s;
        }

        private static class UserItem {
            final User user;
            UserItem(User u) { this.user = u; }
            @Override public String toString() { return user.getUserId() + " - " + user.getUsername(); }
        }

        private static class DegreeItem {
            final Degree degree;
            DegreeItem(Degree d) { this.degree = d; }
            @Override public String toString() { return degree.getDegreeId() + " - " + degree.getDegreeName(); }
        }
    }
}
