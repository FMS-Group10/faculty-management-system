package com.faculty.main;

import com.faculty.util.UITheme;
import com.faculty.view.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UITheme.install();
            new LoginView().setVisible(true);
        });
    }
}
