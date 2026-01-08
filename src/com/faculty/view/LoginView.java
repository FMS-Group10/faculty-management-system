package com.faculty.view;

import com.faculty.controller.LoginController;
import com.faculty.model.Role;
import com.faculty.util.UITheme;
import com.faculty.view.components.ModernButton;
import com.faculty.view.components.RoundedPanel;
import com.faculty.view.components.StyledPasswordField;
import com.faculty.view.components.StyledTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class LoginView extends JFrame {

    private final StyledTextField userField = new StyledTextField();
    private final StyledPasswordField passField = new StyledPasswordField();
    private final RoleToggle adminBtn = new RoleToggle("Admin", Role.Admin);
    private final RoleToggle studentBtn = new RoleToggle("Student", Role.Student);
    private final RoleToggle lecturerBtn = new RoleToggle("Lecturer", Role.Lecturer);
    private final ModernButton loginBtn = new ModernButton("Sign In");

    public LoginView() {
        setTitle("Faculty Management System");
        setSize(980, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        add(buildLeftBrandPanel());
        add(buildRightLoginPanel());

        // Wire controller
        new LoginController(this);
    }

    private JPanel buildLeftBrandPanel() {
        JPanel left = new GradientPanel();
        left.setLayout(new GridBagLayout());

        JLabel icon = new JLabel("\uD83C\uDF93");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icon.setForeground(Color.WHITE);

        JLabel title = new JLabel("Faculty Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel subtitle = new JLabel("<html><center>Login to manage students, lecturers,<br/>courses and academic records.</center></html>", SwingConstants.CENTER);
        subtitle.setForeground(new Color(235, 235, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        RoundedPanel badge = new RoundedPanel(16);
        badge.setShadow(false);
        badge.setBackground(new Color(255,255,255, 35));
        badge.setLayout(new BorderLayout(10, 0));
        JLabel badgeTxt = new JLabel("JDBC + MySQL Ready");
        badgeTxt.setForeground(Color.WHITE);
        badgeTxt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        badge.add(new JLabel("\u26A1"), BorderLayout.WEST);
        badge.add(badgeTxt, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridy = 0;
        left.add(icon, gbc);

        gbc.gridy = 1;
        left.add(title, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(18, 20, 8, 20);
        left.add(subtitle, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(26, 20, 8, 20);
        left.add(badge, gbc);

        return left;
    }

    private JPanel buildRightLoginPanel() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(UITheme.BG);

        RoundedPanel card = new RoundedPanel(22);
        card.setBackground(UITheme.SURFACE);
        card.setLayout(new GridBagLayout());

        JLabel header = new JLabel("Welcome Back");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(UITheme.TEXT_DARK);

        JLabel hint = new JLabel("Sign in using your database account");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(UITheme.TEXT_MUTED);

        JLabel uLbl = label("Username");
        JLabel pLbl = label("Password");
        JLabel rLbl = label("Select Role");

        userField.setPreferredSize(new Dimension(320, 40));
        passField.setPreferredSize(new Dimension(320, 40));
        loginBtn.setPreferredSize(new Dimension(320, 46));

        JPanel roleRow = new JPanel(new GridLayout(1, 3, 10, 0));
        roleRow.setOpaque(false);
        ButtonGroup g = new ButtonGroup();
        g.add(adminBtn);
        g.add(studentBtn);
        g.add(lecturerBtn);
        adminBtn.setSelected(true);
        roleRow.add(adminBtn);
        roleRow.add(studentBtn);
        roleRow.add(lecturerBtn);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 6, 6, 6);

        c.gridy = 0;
        c.insets = new Insets(0, 6, 0, 6);
        card.add(header, c);

        c.gridy = 1;
        c.insets = new Insets(2, 6, 14, 6);
        card.add(hint, c);

        c.gridy = 2;
        c.insets = new Insets(8, 6, 4, 6);
        card.add(uLbl, c);

        c.gridy = 3;
        c.insets = new Insets(0, 6, 10, 6);
        card.add(userField, c);

        c.gridy = 4;
        c.insets = new Insets(8, 6, 4, 6);
        card.add(pLbl, c);

        c.gridy = 5;
        c.insets = new Insets(0, 6, 10, 6);
        card.add(passField, c);

        c.gridy = 6;
        c.insets = new Insets(8, 6, 4, 6);
        card.add(rLbl, c);

        c.gridy = 7;
        c.insets = new Insets(0, 6, 14, 6);
        card.add(roleRow, c);

        c.gridy = 8;
        c.insets = new Insets(4, 6, 0, 6);
        card.add(loginBtn, c);

        GridBagConstraints outer = new GridBagConstraints();
        outer.gridx = 0;
        outer.insets = new Insets(20, 20, 20, 20);
        right.add(card, outer);

        return right;
    }

    private JLabel label(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    // Getters for controller
    public String getUsername() { return userField.getText(); }
    public String getPassword() { return new String(passField.getPassword()); }
    public void addLoginListener(ActionListener listener) { loginBtn.addActionListener(listener); }

    public Role getSelectedRole() {
        if (adminBtn.isSelected()) return Role.Admin;
        if (studentBtn.isSelected()) return Role.Student;
        return Role.Lecturer;
    }

    // ======= Components =======

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, UITheme.PURPLE_900, 0, getHeight(), UITheme.PURPLE_500);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Subtle dots
            g2.setColor(new Color(255,255,255, 18));
            for (int y = 30; y < getHeight(); y += 26) {
                for (int x = 20; x < getWidth(); x += 26) {
                    g2.fillOval(x, y, 3, 3);
                }
            }

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public boolean isOpaque() { return false; }
    }

    private static class RoleToggle extends JToggleButton {
        private final Role role;

        public RoleToggle(String text, Role role) {
            super(text);
            this.role = role;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, 36));
        }

        public Role getRole() { return role; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelected()) {
                g2.setColor(UITheme.PURPLE_700);
                setForeground(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
            } else {
                setForeground(UITheme.PURPLE_700);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(UITheme.PURPLE_700.getRed(), UITheme.PURPLE_700.getGreen(), UITheme.PURPLE_700.getBlue(), 140));
                g2.setStroke(new BasicStroke(1.1f));
                g2.draw(new RoundRectangle2D.Double(0.6, 0.6, getWidth() - 1.2, getHeight() - 1.2, 16, 16));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
