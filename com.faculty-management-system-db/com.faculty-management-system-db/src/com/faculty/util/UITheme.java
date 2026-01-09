package com.faculty.util;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

public final class UITheme {
    private UITheme() {}

    public static final Color PURPLE_900 = new Color(76, 0, 153);
    public static final Color PURPLE_700 = new Color(102, 0, 204);
    public static final Color PURPLE_500 = new Color(153, 51, 255);
    public static final Color BG = new Color(245, 245, 255);
    public static final Color SURFACE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(45, 45, 45);
    public static final Color TEXT_MUTED = new Color(110, 110, 110);

    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public static void install() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ignored) {
        }

        // Global font
        setGlobalFont(FONT_REGULAR);

        // Basic Nimbus tuning
        UIManager.put("control", BG);
        UIManager.put("nimbusBase", PURPLE_700);
        UIManager.put("nimbusBlueGrey", new Color(220, 220, 235));
        UIManager.put("nimbusFocus", PURPLE_500);
        UIManager.put("text", TEXT_DARK);

        // Tables
        UIManager.put("Table.alternateRowColor", new Color(250, 250, 255));
        UIManager.put("Table.gridColor", new Color(230, 230, 245));
    }

    private static void setGlobalFont(Font f) {
        for (Object key : UIManager.getDefaults().keySet()) {
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new javax.swing.plaf.FontUIResource(f));
            }
        }
    }
}
