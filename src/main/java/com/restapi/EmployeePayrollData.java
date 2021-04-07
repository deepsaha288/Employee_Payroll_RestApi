package com.restapi;

import java.sql.Date;

public class EmployeePayrollData {
    public int id;
    public String name;
    public Date start;
    public double salary;
    public String gender;

    public EmployeePayrollData(int id, String name, Date start, double salary, String gender)
    {
        this.id=id;
        this.name=name;
        this.start=start;
        this.salary=salary;
        this.gender=gender;
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

    public Date getDate() {
        return start;
    }

    public void setDate(Date start) {
        this.start = start;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", salary=" + salary +
                ", gender='" + gender + '\'' +
                '}';
    }
}

