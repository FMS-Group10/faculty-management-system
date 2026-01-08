package com.faculty.model;

public class Degree {
    private int degreeId;
    private String degreeName;
    private int deptId;
    private int durationYears;

    // joined
    private String deptName;

    public Degree() {}

    public Degree(int degreeId, String degreeName, int deptId, int durationYears) {
        this.degreeId = degreeId;
        this.degreeName = degreeName;
        this.deptId = deptId;
        this.durationYears = durationYears;
    }

    public int getDegreeId() { return degreeId; }
    public void setDegreeId(int degreeId) { this.degreeId = degreeId; }
    public String getDegreeName() { return degreeName; }
    public void setDegreeName(String degreeName) { this.degreeName = degreeName; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public int getDurationYears() { return durationYears; }
    public void setDurationYears(int durationYears) { this.durationYears = durationYears; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
}
