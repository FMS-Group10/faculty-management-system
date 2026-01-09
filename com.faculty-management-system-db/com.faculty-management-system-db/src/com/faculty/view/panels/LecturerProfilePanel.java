package com.faculty.view.panels;

import com.faculty.controller.DepartmentController;
import com.faculty.controller.LecturerController;
import com.faculty.model.Department;
import com.faculty.model.Lecturer;
import com.faculty.model.Session;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.ModernButton;
import com.faculty.view.components.RoundedPanel;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LecturerProfilePanel extends JPanel {
    private final Session session;
    private final LecturerController lecturerController = new LecturerController();
    private final DepartmentController deptController = new DepartmentController();

    private Lecturer current;

    private final StyledTextField lecturerId = new StyledTextField();
    private final StyledTextField username = new StyledTextField();
    private final StyledTextField first = new StyledTextField();
    private final StyledTextField last = new StyledTextField();
    private final StyledTextField email = new StyledTextField();
    private final StyledTextField mobile = new StyledTextField();
    private final JComboBox<DeptItem> deptCombo = new JComboBox<>();

    public LecturerProfilePanel(Session session) {
        this.session = session;

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel header = new JLabel("My Profile");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(UITheme.TEXT_DARK);
        add(header, BorderLayout.NORTH);

        RoundedPanel card = new RoundedPanel(22);
        card.setBackground(UITheme.SURFACE);
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(8, 10, 8, 10);
        c.anchor = GridBagConstraints.WEST;

        // ✅ IMPORTANT: allow components to expand horizontally (fixes tiny boxes)
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        lecturerId.setEnabled(false);
        username.setEnabled(false);
        deptCombo.setFont(UITheme.FONT_REGULAR);

        int r = 0;
        addField(card, c, r++, "Lecturer ID", lecturerId);
        addField(card, c, r++, "Username", username);
        addField(card, c, r++, "First Name", first);
        addField(card, c, r++, "Last Name", last);
        addField(card, c, r++, "Email", email);
        addField(card, c, r++, "Mobile", mobile);
        addField(card, c, r++, "Department", deptCombo);

        ModernButton save = new ModernButton("Save Changes");
        save.setPreferredSize(new Dimension(260, 48)); // bigger button
        save.addActionListener(e -> save());

        c.gridy = r * 2;
        c.insets = new Insets(18, 10, 18, 10);
        c.anchor = GridBagConstraints.CENTER;
        card.add(save, c);

        add(card, BorderLayout.CENTER);

        load();
    }

    private void addField(JPanel p, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridy = row * 2;
        JLabel l = new JLabel(label);
        l.setForeground(UITheme.TEXT_MUTED);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(l, c);

        c.gridy = row * 2 + 1;

        // ✅ IMPORTANT: keep fill/weightx for each field row (fixes tiny boxes)
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        field.setPreferredSize(new Dimension(520, 38)); // wider inputs
        p.add(field, c);
    }

    private void load() {
        try {
            current = lecturerController.findByUserId(session.getUserId()).orElse(null);
            if (current == null) {
                DialogUtil.error(this, "No lecturer profile found for this account.");
                return;
            }

            List<Department> departments = deptController.listAll();
            deptCombo.removeAllItems();
            for (Department d : departments) deptCombo.addItem(new DeptItem(d));
            selectDept(current.getDeptId());

            lecturerId.setText(String.valueOf(current.getLecturerId()));
            username.setText(current.getUsername());
            first.setText(current.getFirstName());
            last.setText(current.getLastName());
            email.setText(current.getEmail());
            mobile.setText(current.getMobile());
        } catch (SQLException ex) {
            DialogUtil.error(this, "Failed to load profile.\n\n" + ex.getMessage());
        }
    }

    private void selectDept(int deptId) {
        for (int i = 0; i < deptCombo.getItemCount(); i++) {
            if (deptCombo.getItemAt(i).dept.getDeptId() == deptId) {
                deptCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void save() {
        if (current == null) return;

        if (first.getText().trim().isEmpty() || last.getText().trim().isEmpty()) {
            DialogUtil.error(this, "First and last name are required.");
            return;
        }

        DeptItem d = (DeptItem) deptCombo.getSelectedItem();
        current.setFirstName(first.getText().trim());
        current.setLastName(last.getText().trim());
        current.setEmail(email.getText().trim());
        current.setMobile(mobile.getText().trim());
        current.setDeptId(d == null ? current.getDeptId() : d.dept.getDeptId());

        try {
            lecturerController.update(current);
            DialogUtil.info(this, "Profile updated.");
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    private static class DeptItem {
        final Department dept;
        DeptItem(Department d) { this.dept = d; }
        @Override public String toString() { return dept.getName(); }
    }
}
