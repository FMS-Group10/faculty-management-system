package com.faculty.view.panels;

import com.faculty.controller.StatsController;
import com.faculty.util.DialogUtil;
import com.faculty.util.UITheme;
import com.faculty.view.components.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class AdminOverviewPanel extends JPanel {
    private final StatsController statsController = new StatsController();

    private final JLabel usersCount = new JLabel("-");
    private final JLabel studentsCount = new JLabel("-");
    private final JLabel lecturersCount = new JLabel("-");
    private final JLabel coursesCount = new JLabel("-");

    public AdminOverviewPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel header = new JLabel("Admin Overview");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(UITheme.TEXT_DARK);
        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(18, 0, 0, 0));

        grid.add(statCard("Users", "Total accounts", usersCount, "\uD83D\uDC64"));
        grid.add(statCard("Students", "Registered students", studentsCount, "\uD83C\uDF93"));
        grid.add(statCard("Lecturers", "Academic staff", lecturersCount, "\uD83D\uDC69\u200D\uD83C\uDFEB"));
        grid.add(statCard("Courses", "Available courses", coursesCount, "\uD83D\uDCD6"));

        add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshCounts());
        bottom.add(refresh);
        add(bottom, BorderLayout.SOUTH);

        refreshCounts();
    }

    private RoundedPanel statCard(String title, String subtitle, JLabel value, String icon) {
        RoundedPanel card = new RoundedPanel(22);
        card.setBackground(UITheme.SURFACE);
        card.setLayout(new BorderLayout(10, 10));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(iconLbl, BorderLayout.NORTH);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(UITheme.TEXT_DARK);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UITheme.TEXT_MUTED);

        value.setFont(new Font("Segoe UI", Font.BOLD, 36));
        value.setForeground(UITheme.PURPLE_700);

        text.add(t);
        text.add(Box.createRigidArea(new Dimension(0, 4)));
        text.add(sub);
        text.add(Box.createRigidArea(new Dimension(0, 14)));
        text.add(value);

        card.add(left, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    private void refreshCounts() {
        try {
            usersCount.setText(String.valueOf(statsController.countTable("users")));
            studentsCount.setText(String.valueOf(statsController.countTable("students")));
            lecturersCount.setText(String.valueOf(statsController.countTable("lecturers")));
            coursesCount.setText(String.valueOf(statsController.countTable("courses")));
        } catch (SQLException ex) {
            DialogUtil.error(this, "Could not load stats.\n\n" + ex.getMessage());
        }
    }
}
