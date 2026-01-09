package com.faculty.view.components;

import com.faculty.util.UITheme;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

public class ModernTable extends JTable {
    public ModernTable(TableModel dm) {
        super(dm);
        setRowHeight(34);
        setFont(UITheme.FONT_REGULAR.deriveFont(13.5f));
        setShowVerticalLines(false);
        setGridColor(new Color(230, 230, 245));
        setSelectionBackground(new Color(220, 210, 255));
        setSelectionForeground(UITheme.TEXT_DARK);

        JTableHeader header = getTableHeader();
        header.setPreferredSize(new Dimension(0, 38));
        header.setBackground(UITheme.PURPLE_500);
        header.setForeground(Color.WHITE);
        header.setFont(UITheme.FONT_BOLD.deriveFont(13.5f));
    }
}
