package com.faculty.view.panels;

import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.DangerButton;
import com.faculty.view.components.ModernButton;
import com.faculty.view.components.ModernTable;
import com.faculty.view.components.OutlineButton;
import com.faculty.view.components.RoundedPanel;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class BaseCrudPanel extends JPanel {
    protected final DefaultTableModel model;
    protected final ModernTable table;
    protected final StyledTextField searchField = new StyledTextField();

    protected final ModernButton addBtn = new ModernButton("+ Add");
    protected final OutlineButton editBtn = new OutlineButton("Edit");
    protected final DangerButton deleteBtn = new DangerButton("Delete");
    protected final OutlineButton refreshBtn = new OutlineButton("Refresh");

    protected BaseCrudPanel(String title, String[] columns) {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        // Header
        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(UITheme.TEXT_DARK);

        JLabel sub = new JLabel("Manage records (Create / Read / Update / Delete)");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UITheme.TEXT_MUTED);

        JPanel headerBox = new JPanel();
        headerBox.setOpaque(false);
        headerBox.setLayout(new BoxLayout(headerBox, BoxLayout.Y_AXIS));
        headerBox.add(header);
        headerBox.add(Box.createRigidArea(new Dimension(0, 2)));
        headerBox.add(sub);

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.setOpaque(false);
        toolbar.setBorder(new EmptyBorder(14, 0, 12, 0));

        JPanel left = new JPanel(new BorderLayout(8, 0));
        left.setOpaque(false);
        JLabel searchLbl = new JLabel("Search:");
        searchLbl.setForeground(UITheme.TEXT_MUTED);
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

        searchField.setPreferredSize(new Dimension(260, 36));
        left.add(searchLbl, BorderLayout.WEST);
        left.add(searchField, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        addBtn.setPreferredSize(new Dimension(110, 36));
        editBtn.setPreferredSize(new Dimension(90, 36));
        deleteBtn.setPreferredSize(new Dimension(100, 36));
        refreshBtn.setPreferredSize(new Dimension(100, 36));

        right.add(refreshBtn);
        right.add(addBtn);
        right.add(editBtn);
        right.add(deleteBtn);

        toolbar.add(left, BorderLayout.WEST);
        toolbar.add(right, BorderLayout.EAST);

        // Table
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new ModernTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        RoundedPanel surface = new RoundedPanel(22);
        surface.setBackground(UITheme.SURFACE);
        surface.setLayout(new BorderLayout());
        surface.add(scroll, BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(headerBox, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(surface, BorderLayout.CENTER);

        // Actions
        refreshBtn.addActionListener(e -> reload());
        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                DialogUtil.info(this, "Select a row to edit.");
                return;
            }
            onEdit();
        });
        deleteBtn.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                DialogUtil.info(this, "Select a row to delete.");
                return;
            }
            onDelete();
        });

        searchField.addActionListener(e -> reload());

        // IMPORTANT:
        // Do NOT call overridable methods (like loadData()) directly from a superclass constructor.
        // Subclass fields (e.g., controllers/DAOs) are not initialized yet at this point, which can
        // cause NullPointerExceptions like: "Cannot invoke ... because this.controller is null".
        //
        // Schedule the initial load to run after the Swing UI thread returns to the event loop,
        // which guarantees the subclass has finished initializing its fields.
        SwingUtilities.invokeLater(this::reload);
    }

    protected void reload() {
        String q = searchField.getText() == null ? "" : searchField.getText().trim();
        try {
            loadData(q);
        } catch (Exception ex) {
            DialogUtil.error(this, "Failed to load data.\n\n" + ex.getMessage());
        }
    }

    protected abstract void loadData(String filter) throws Exception;
    protected abstract void onAdd();
    protected abstract void onEdit();
    protected abstract void onDelete();
}
