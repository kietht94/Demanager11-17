package com.example.demanager;

public class Employees {
    private	int	id;
    private	String name;
    private	String phno;
    private int departmentId ;
    public Employees(String name, String phno) {
        this.name = name;
        this.phno = phno;
    }

    public Employees(int id, String name, String phno, int departmentId) {
        this.id = id;
        this.name = name;
        this.phno = phno;
        this.departmentId = departmentId;
    }
    //json add
    public Employees() {

        this.name = name;
        this.phno = phno;
    }


    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

}
