package com.faculty.view.panels;

import com.faculty.controller.DepartmentController;
import com.faculty.model.Department;
import com.faculty.util.DialogUtil;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DepartmentsPanel extends BaseCrudPanel {
    private final DepartmentController controller = new DepartmentController();

    public DepartmentsPanel() {
        super("Department Management", new String[]{"Dept ID", "Department Name", "Staff Count"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<Department> list = controller.listAll();
        for (Department d : list) {
            String hay = (d.getDeptId() + " " + d.getName()).toLowerCase();
            if (!filter.isEmpty() && !hay.contains(filter.toLowerCase())) continue;
            model.addRow(new Object[]{d.getDeptId(), d.getName(), d.getStaffCount() == null ? 0 : d.getStaffCount()});
        }
    }

    @Override
    protected void onAdd() {
        DepartmentForm form = new DepartmentForm(null);
        if (!form.showDialog(this, "Add Department")) return;
        try {
            controller.create(form.getDepartment());
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Create failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        int deptId = (int) model.getValueAt(row, 0);
        try {
            Department existing = controller.findById(deptId).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "Department not found.");
                return;
            }
            DepartmentForm form = new DepartmentForm(existing);
            if (!form.showDialog(this, "Edit Department")) return;
            controller.update(form.getDepartment());
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int deptId = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete department ID " + deptId + "?")) return;
        try {
            controller.delete(deptId);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    private static class DepartmentForm {
        private final StyledTextField name = new StyledTextField();
        private final Department base;

        DepartmentForm(Department base) {
            this.base = base;
            if (base != null) name.setText(base.getName());
            name.setPreferredSize(new Dimension(320, 38));
        }

        boolean showDialog(Component parent, String title) {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.insets = new Insets(6, 6, 6, 6);
            c.anchor = GridBagConstraints.WEST;

            panel.add(new JLabel("Department Name"), c);
            c.gridy = 1;
            panel.add(name, c);

            int ok = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return false;

            if (name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Name is required.");
                return false;
            }
            return true;
        }

        Department getDepartment() {
            Department d = base == null ? new Department() : base;
            d.setName(name.getText().trim());
            return d;
        }
    }
}
