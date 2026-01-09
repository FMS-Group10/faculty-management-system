package com.faculty.view.components;

import com.faculty.util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class OutlineButton extends JButton {
    private final Color stroke;
    private final Color hoverFill;

    public OutlineButton(String text) {
        this(text, UITheme.PURPLE_700);
    }

    public OutlineButton(String text, Color stroke) {
        super(text);
        this.stroke = stroke;
        this.hoverFill = new Color(stroke.getRed(), stroke.getGreen(), stroke.getBlue(), 25);

        setForeground(stroke);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(UITheme.FONT_BOLD.deriveFont(13f));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover()) {
            g2.setColor(hoverFill);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
        }

        g2.setStroke(new BasicStroke(1.2f));
        g2.setColor(stroke);
        g2.draw(new RoundRectangle2D.Double(0.6, 0.6, getWidth()-1.2, getHeight()-1.2, 18, 18));

        g2.dispose();
        super.paintComponent(g);
    }
}
