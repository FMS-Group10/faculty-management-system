package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;

/**
 * A simple rounded panel with optional shadow.
 */
public class RoundedPanel extends JPanel {
    private final int radius;
    private boolean shadow = true;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (shadow) {
            // Soft shadow (cheap)
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(4, 4, w - 8, h - 8, radius, radius);
        }

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, w - 8, h - 8, radius, radius);
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public Insets getInsets() {
        return new Insets(14, 16, 14, 16);
    }
}
