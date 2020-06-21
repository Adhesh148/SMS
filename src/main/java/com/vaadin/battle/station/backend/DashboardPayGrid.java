package com.vaadin.battle.station.backend;

public class DashboardPayGrid {
    private int eid;
    private float base_sal;
    private float gross_sal;
    private String ename;
    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setBase_sal(float base_sal) {
        this.base_sal = base_sal;
    }

    public void setGross_sal(float gross_sal) {
        this.gross_sal = gross_sal;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getYear() {
        return year;
    }

    public String getEname() {
        return ename;
    }

    public float getGross_sal() {
        return gross_sal;
    }

    public float getBase_sal() {
        return base_sal;
    }

    public int getEid() {
        return eid;
    }
}
