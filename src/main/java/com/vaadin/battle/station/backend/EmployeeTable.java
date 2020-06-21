package com.vaadin.battle.station.backend;

import java.time.LocalDate;

public class EmployeeTable
{
    private int eid;
    private String ename;
    private String doj;
    private String dor;
    private int qtype;

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getEid() {
        return eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }
}