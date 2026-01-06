package com.faculty.view.components;

import com.faculty.util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernButton extends JButton {
    private final Color base;
    private final Color hover;
    private final Color press;

    public ModernButton(String text) {
        this(text, UITheme.PURPLE_700, UITheme.PURPLE_500, UITheme.PURPLE_900);
    }

    public ModernButton(String text, Color base, Color hover, Color press) {
        super(text);
        this.base = base;
        this.hover = hover;
        this.press = press;

        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(UITheme.FONT_BOLD.deriveFont(14f));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color c = base;
        if (getModel().isPressed()) c = press;
        else if (getModel().isRollover()) c = hover;
        g2.setColor(c);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setForeground(b ? Color.WHITE : new Color(240,240,240));
    }
}
