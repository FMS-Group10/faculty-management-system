package com.faculty.model;

public class Department {
    private int deptId;
    private String name;

    // joined
    private Integer staffCount;

    public Department() {}

    public Department(int deptId, String name) {
        this.deptId = deptId;
        this.name = name;
    }

    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getStaffCount() { return staffCount; }
    public void setStaffCount(Integer staffCount) { this.staffCount = staffCount; }
}
