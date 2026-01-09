package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;

public class DangerButton extends ModernButton {
    public DangerButton(String text) {
        super(text,
                new Color(220, 50, 50),
                new Color(240, 70, 70),
                new Color(180, 35, 35));
    }
}
