package com.faculty.view.panels;

import com.faculty.controller.UserController;
import com.faculty.model.Role;
import com.faculty.model.User;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.ModernButton;
import com.faculty.view.components.StyledPasswordField;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UsersPanel extends BaseCrudPanel {
    private final UserController controller = new UserController();

    public UsersPanel() {
        super("User Management", new String[]{"User ID", "Username", "Role"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<User> users = controller.listAll();
        for (User u : users) {
            if (!filter.isEmpty()) {
                String hay = (u.getUserId() + " " + u.getUsername() + " " + u.getRole()).toLowerCase();
                if (!hay.contains(filter.toLowerCase())) continue;
            }
            model.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getRole()});
        }
    }

    @Override
    protected void onAdd() {
        UserForm form = new UserForm(null);
        if (!form.showDialog(this, "Add User")) return;
        try {
            controller.create(form.getUser());
            reload();
            DialogUtil.info(this, "User created successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Create failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        int userId = (int) model.getValueAt(row, 0);
        try {
            User existing = controller.findById(userId).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "User not found.");
                return;
            }
            UserForm form = new UserForm(existing);
            if (!form.showDialog(this, "Edit User")) return;
            controller.update(form.getUser());
            reload();
            DialogUtil.info(this, "User updated successfully.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int userId = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete user ID " + userId + "?")) return;
        try {
            controller.delete(userId);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    // ===== Form Dialog =====
    private static class UserForm {
        private final StyledTextField username = new StyledTextField();
        private final StyledPasswordField password = new StyledPasswordField();
        private final JComboBox<Role> role = new JComboBox<>(Role.values());
        private User base;

        UserForm(User base) {
            this.base = base;
            if (base != null) {
                username.setText(base.getUsername());
                password.setText(base.getPassword());
                role.setSelectedItem(base.getRole());
            } else {
                role.setSelectedItem(Role.Student);
            }
            role.setFont(UITheme.FONT_REGULAR);
        }

        boolean showDialog(Component parent, String title) {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 0;
            c.insets = new Insets(6, 6, 6, 6);
            c.anchor = GridBagConstraints.WEST;

            panel.add(new JLabel("Username"), c);
            c.gridy++;
            username.setPreferredSize(new Dimension(300, 38));
            panel.add(username, c);

            c.gridy++;
            panel.add(new JLabel("Password"), c);
            c.gridy++;
            password.setPreferredSize(new Dimension(300, 38));
            panel.add(password, c);

            c.gridy++;
            panel.add(new JLabel("Role"), c);
            c.gridy++;
            role.setPreferredSize(new Dimension(300, 38));
            panel.add(role, c);

            int ok = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return false;

            if (username.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Username is required.");
                return false;
            }
            if (new String(password.getPassword()).trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Password is required.");
                return false;
            }
            return true;
        }

        User getUser() {
            User u = base == null ? new User() : base;
            u.setUsername(username.getText().trim());
            u.setPassword(new String(password.getPassword()));
            u.setRole((Role) role.getSelectedItem());
            return u;
        }
    }
}
