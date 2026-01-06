package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;

public class StyledTextField extends JTextField {
    public StyledTextField() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 225), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        setBackground(Color.WHITE);
    }
}
