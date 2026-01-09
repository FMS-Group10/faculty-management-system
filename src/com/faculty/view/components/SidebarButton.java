package com.faculty.view.components;

import com.faculty.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class SidebarButton extends JButton {
    private boolean selected = false;

    public SidebarButton(String text) {
        super(text);
        setForeground(Color.WHITE);
        setBackground(new Color(0,0,0,0));
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(UITheme.FONT_BOLD.deriveFont(13.5f));
        setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!selected) repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (!selected) repaint();
            }
        });
    }

    public void setSelectedState(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean hover = getModel().isRollover();
        if (selected) {
            g2.setColor(new Color(255, 255, 255, 35));
            g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 14, 14);
        } else if (hover) {
            g2.setColor(new Color(255, 255, 255, 22));
            g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 14, 14);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
