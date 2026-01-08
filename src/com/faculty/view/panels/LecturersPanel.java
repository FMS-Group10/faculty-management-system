package com.faculty.view.panels;

import com.faculty.controller.DepartmentController;
import com.faculty.controller.LecturerController;
import com.faculty.controller.UserController;
import com.faculty.model.Department;
import com.faculty.model.Lecturer;
import com.faculty.model.Role;
import com.faculty.model.User;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LecturersPanel extends BaseCrudPanel {
    private final LecturerController controller = new LecturerController();
    private final UserController userController = new UserController();
    private final DepartmentController deptController = new DepartmentController();

    public LecturersPanel() {
        super("Lecturer Management", new String[]{"Lecturer ID", "Username", "Full Name", "Department", "Email", "Mobile"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<Lecturer> list = controller.listAll();
        for (Lecturer l : list) {
            String hay = (l.getLecturerId() + " " + l.getUsername() + " " + l.getFullName() + " " + l.getDeptName() + " " + l.getEmail() + " " + l.getMobile()).toLowerCase();
            if (!filter.isEmpty() && !hay.contains(filter.toLowerCase())) continue;
            model.addRow(new Object[]{l.getLecturerId(), l.getUsername(), l.getFullName(), l.getDeptName(), l.getEmail(), l.getMobile()});
        }
    }

    @Override
    protected void onAdd() {
        try {
            List<User> lecturerUsers = userController.listByRole(Role.Lecturer);
            List<Department> departments = deptController.listAll();
            LecturerForm form = new LecturerForm(null, lecturerUsers, departments);
            if (!form.showDialog(this, "Add Lecturer")) return;
            controller.create(form.getLecturer());
            reload();
            DialogUtil.info(this, "Lecturer created successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Create failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        int lecturerId = (int) model.getValueAt(row, 0);
        try {
            Lecturer existing = controller.findById(lecturerId).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "Lecturer not found.");
                return;
            }
            List<User> lecturerUsers = userController.listByRole(Role.Lecturer);
            List<Department> departments = deptController.listAll();
            LecturerForm form = new LecturerForm(existing, lecturerUsers, departments);
            if (!form.showDialog(this, "Edit Lecturer")) return;
            controller.update(form.getLecturer());
            reload();
            DialogUtil.info(this, "Lecturer updated successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int lecturerId = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete lecturer ID " + lecturerId + "?")) return;
        try {
            controller.delete(lecturerId);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    private static class LecturerForm {
        private final JComboBox<UserItem> userCombo;
        private final StyledTextField first = new StyledTextField();
        private final StyledTextField last = new StyledTextField();
        private final JComboBox<DeptItem> deptCombo;
        private final StyledTextField email = new StyledTextField();
        private final StyledTextField mobile = new StyledTextField();
        private final Lecturer base;

        LecturerForm(Lecturer base, List<User> users, List<Department> departments) {
            this.base = base;
            userCombo = new JComboBox<>(users.stream().map(UserItem::new).toArray(UserItem[]::new));
            deptCombo = new JComboBox<>(departments.stream().map(DeptItem::new).toArray(DeptItem[]::new));
            userCombo.setFont(UITheme.FONT_REGULAR);
            deptCombo.setFont(UITheme.FONT_REGULAR);

            if (base != null) {
                first.setText(base.getFirstName());
                last.setText(base.getLastName());
                email.setText(base.getEmail());
                mobile.setText(base.getMobile());

                selectUser(base.getUserId());
                userCombo.setEnabled(false);
                selectDept(base.getDeptId());
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

        private void selectDept(int deptId) {
            for (int i = 0; i < deptCombo.getItemCount(); i++) {
                if (deptCombo.getItemAt(i).dept.getDeptId() == deptId) {
                    deptCombo.setSelectedIndex(i);
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
            addRow(panel, c, y++, "User (Lecturer)", userCombo);
            addRow(panel, c, y++, "First Name", sized(first));
            addRow(panel, c, y++, "Last Name", sized(last));
            addRow(panel, c, y++, "Department", deptCombo);
            addRow(panel, c, y++, "Email", sized(email));
            addRow(panel, c, y++, "Mobile", sized(mobile));

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

        Lecturer getLecturer() {
            Lecturer l = base == null ? new Lecturer() : base;
            UserItem u = (UserItem) userCombo.getSelectedItem();
            DeptItem d = (DeptItem) deptCombo.getSelectedItem();
            l.setUserId(u == null ? 0 : u.user.getUserId());
            l.setFirstName(first.getText().trim());
            l.setLastName(last.getText().trim());
            l.setDeptId(d == null ? 0 : d.dept.getDeptId());
            l.setEmail(email.getText().trim());
            l.setMobile(mobile.getText().trim());
            return l;
        }

        private static class UserItem {
            final User user;
            UserItem(User u) { this.user = u; }
            @Override public String toString() { return user.getUserId() + " - " + user.getUsername(); }
        }

        private static class DeptItem {
            final Department dept;
            DeptItem(Department d) { this.dept = d; }
            @Override public String toString() { return dept.getDeptId() + " - " + dept.getName(); }
        }
    }
}
