package com.faculty.model;

public enum Role {
    Admin,
    Student,
    Lecturer;

    public static Role fromString(String s) {
        for (Role r : values()) {
            if (r.name().equalsIgnoreCase(s)) return r;
        }
        throw new IllegalArgumentException("Unknown role: " + s);
    }
}
