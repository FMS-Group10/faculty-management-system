package com.faculty.view.panels;

import com.faculty.controller.DegreeController;
import com.faculty.controller.DepartmentController;
import com.faculty.model.Degree;
import com.faculty.model.Department;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DegreesPanel extends BaseCrudPanel {
    private final DegreeController controller = new DegreeController();
    private final DepartmentController deptController = new DepartmentController();

    public DegreesPanel() {
        super("Degree Management", new String[]{"Degree ID", "Degree Name", "Department", "Duration (Years)"});
    }

    @Override
    protected void loadData(String filter) throws SQLException {
        model.setRowCount(0);
        List<Degree> list = controller.listAll();
        for (Degree d : list) {
            String hay = (d.getDegreeId() + " " + d.getDegreeName() + " " + d.getDeptName() + " " + d.getDurationYears()).toLowerCase();
            if (!filter.isEmpty() && !hay.contains(filter.toLowerCase())) continue;
            model.addRow(new Object[]{d.getDegreeId(), d.getDegreeName(), d.getDeptName(), d.getDurationYears()});
        }
    }

    @Override
    protected void onAdd() {
        try {
            List<Department> depts = deptController.listAll();
            DegreeForm form = new DegreeForm(null, depts);
            if (!form.showDialog(this, "Add Degree")) return;
            controller.create(form.getDegree());
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
            Degree existing = controller.findById(id).orElse(null);
            if (existing == null) {
                DialogUtil.error(this, "Degree not found.");
                return;
            }
            List<Department> depts = deptController.listAll();
            DegreeForm form = new DegreeForm(existing, depts);
            if (!form.showDialog(this, "Edit Degree")) return;
            controller.update(form.getDegree());
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Update failed.\n\n" + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        int id = (int) model.getValueAt(row, 0);
        if (!DialogUtil.confirm(this, "Delete degree ID " + id + "?")) return;
        try {
            controller.delete(id);
            reload();
        } catch (SQLException ex) {
            DialogUtil.error(this, "Delete failed.\n\n" + ex.getMessage());
        }
    }

    private static class DegreeForm {
        private final StyledTextField name = new StyledTextField();
        private final StyledTextField duration = new StyledTextField();
        private final JComboBox<DeptItem> deptCombo;
        private final Degree base;

        DegreeForm(Degree base, List<Department> departments) {
            this.base = base;
            deptCombo = new JComboBox<>(departments.stream().map(DeptItem::new).toArray(DeptItem[]::new));
            deptCombo.setFont(UITheme.FONT_REGULAR);

            name.setPreferredSize(new Dimension(320, 38));
            duration.setPreferredSize(new Dimension(320, 38));

            if (base != null) {
                name.setText(base.getDegreeName());
                duration.setText(String.valueOf(base.getDurationYears()));
                selectDept(base.getDeptId());
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
            addRow(panel, c, y++, "Degree Name", name);
            addRow(panel, c, y++, "Department", deptCombo);
            addRow(panel, c, y++, "Duration (Years)", duration);

            int ok = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return false;

            if (name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Degree name is required.");
                return false;
            }
            try {
                Integer.parseInt(duration.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Duration must be a number.");
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

        Degree getDegree() {
            Degree d = base == null ? new Degree() : base;
            d.setDegreeName(name.getText().trim());
            d.setDurationYears(Integer.parseInt(duration.getText().trim()));
            DeptItem deptItem = (DeptItem) deptCombo.getSelectedItem();
            d.setDeptId(deptItem == null ? 0 : deptItem.dept.getDeptId());
            return d;
        }

        private static class DeptItem {
            final Department dept;
            DeptItem(Department d) { this.dept = d; }
            @Override public String toString() { return dept.getDeptId() + " - " + dept.getName(); }
        }
    }
}
