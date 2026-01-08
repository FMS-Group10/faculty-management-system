package com.faculty.view;

import com.faculty.model.Session;
import com.faculty.util.UITheme;
import com.faculty.view.components.DangerButton;
import com.faculty.view.components.SidebarButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DashboardFrame extends JFrame {

    protected final Session session;
    protected final CardLayout cardLayout = new CardLayout();
    protected final JPanel content = new JPanel(cardLayout);

    private final JLabel pageTitle = new JLabel("Dashboard");
    private final Map<String, SidebarButton> navButtons = new LinkedHashMap<>();

    protected DashboardFrame(Session session, String windowTitle) {
        this.session = session;

        setTitle(windowTitle);
        setSize(1180, 740);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildTopBar(), BorderLayout.NORTH);

        content.setBackground(UITheme.BG);
        add(content, BorderLayout.CENTER);

        // Let subclasses add pages
        buildPages();

        // Default page
        if (!navButtons.isEmpty()) {
            String firstKey = navButtons.keySet().iterator().next();
            showPage(firstKey, navButtons.get(firstKey).getText());
        }
    }

    protected abstract void buildPages();

    protected void addNavItem(String key, String label, JPanel page) {
        content.add(page, key);
        SidebarButton btn = new SidebarButton(label);
        navButtons.put(key, btn);
    }

    protected void showPage(String key, String title) {
        cardLayout.show(content, key);
        pageTitle.setText(title);
        navButtons.forEach((k, b) -> b.setSelectedState(k.equals(key)));
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 740));
        sidebar.setLayout(new BorderLayout());

        JPanel gradient = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UITheme.PURPLE_900, 0, getHeight(), UITheme.PURPLE_700);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public boolean isOpaque() { return false; }
        };
        gradient.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(22, 18, 18, 18));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("\uD83C\uDF93");
        icon.setForeground(Color.WHITE);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel app = new JLabel("Faculty System");
        app.setForeground(Color.WHITE);
        app.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel user = new JLabel(session.getUsername() + "  •  " + session.getRole());
        user.setForeground(new Color(235, 235, 255));
        user.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        header.add(icon);
        header.add(Box.createRigidArea(new Dimension(0, 6)));
        header.add(app);
        header.add(Box.createRigidArea(new Dimension(0, 6)));
        header.add(user);

        // Nav list
        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(10, 6, 10, 6));
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));

        // We'll attach buttons after pages are built. But we need them now.
        // So, buildPages() is called in constructor AFTER sidebar is built.
        // We'll refresh sidebar buttons via invokeLater.

        // Footer
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 18, 20, 18));
        footer.setLayout(new BorderLayout(0, 10));

        DangerButton logout = new DangerButton("Logout");
        logout.setPreferredSize(new Dimension(0, 42));
        logout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        footer.add(logout, BorderLayout.SOUTH);

        gradient.add(header, BorderLayout.NORTH);
        gradient.add(new JScrollPane(nav) {
            {
                setBorder(BorderFactory.createEmptyBorder());
                setOpaque(false);
                getViewport().setOpaque(false);
                getVerticalScrollBar().setUnitIncrement(16);
            }
        }, BorderLayout.CENTER);
        gradient.add(footer, BorderLayout.SOUTH);

        sidebar.add(gradient, BorderLayout.CENTER);

        // After UI is visible, we populate nav panel
        SwingUtilities.invokeLater(() -> {
            nav.removeAll();
            for (Map.Entry<String, SidebarButton> entry : navButtons.entrySet()) {
                String key = entry.getKey();
                SidebarButton btn = entry.getValue();
                btn.addActionListener(e -> showPage(key, btn.getText()));
                nav.add(btn);
                nav.add(Box.createRigidArea(new Dimension(0, 8)));
            }
            nav.revalidate();
            nav.repaint();
        });

        return sidebar;
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UITheme.SURFACE);
        top.setBorder(new EmptyBorder(12, 18, 12, 18));

        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pageTitle.setForeground(UITheme.TEXT_DARK);
        top.add(pageTitle, BorderLayout.WEST);

        JLabel badge = new JLabel("Connected to DB (JDBC)");
        badge.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        badge.setForeground(UITheme.TEXT_MUTED);
        top.add(badge, BorderLayout.EAST);

        return top;
    }
}
