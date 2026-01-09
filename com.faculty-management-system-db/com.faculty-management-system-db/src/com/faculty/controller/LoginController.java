package com.faculty.controller;

import com.faculty.dao.UserDAO;
import com.faculty.dao.UserDAOImpl;
import com.faculty.model.Role;
import com.faculty.model.Session;
import com.faculty.model.User;
import com.faculty.util.DialogUtil;
import com.faculty.view.AdminDashboardView;
import com.faculty.view.LecturerDashboardView;
import com.faculty.view.LoginView;
import com.faculty.view.StudentDashboardView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController implements ActionListener {
    private final LoginView view;
    private final UserDAO userDAO;

    public LoginController(LoginView view) {
        this.view = view;
        this.userDAO = new UserDAOImpl();
        this.view.addLoginListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = view.getUsername().trim();
        String password = view.getPassword();
        Role selectedRole = view.getSelectedRole();

        if (username.isEmpty() || password.isEmpty()) {
            DialogUtil.error(view, "Please enter username and password.");
            return;
        }

        try {
            Optional<User> uOpt = userDAO.authenticate(username, password);
            if (uOpt.isEmpty()) {
                DialogUtil.error(view, "Invalid username or password.");
                return;
            }

            User u = uOpt.get();
            if (u.getRole() != selectedRole) {
                DialogUtil.error(view, "Role mismatch. Your account role is: " + u.getRole());
                return;
            }

            Session session = new Session(u.getUserId(), u.getUsername(), u.getRole());
            view.dispose();

            switch (session.getRole()) {
                case Admin -> new AdminDashboardView(session).setVisible(true);
                case Student -> new StudentDashboardView(session).setVisible(true);
                case Lecturer -> new LecturerDashboardView(session).setVisible(true);
            }
        } catch (SQLException ex) {
            DialogUtil.error(view, "Database error.\n\n" + ex.getMessage() + "\n\nHint: Check DBConfig.java and JDBC driver.");
        }
    }
}
