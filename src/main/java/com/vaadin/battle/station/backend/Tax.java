package com.vaadin.battle.station.backend;

public class Tax {
    private int eid;
    private float base_sal;
    private float gross_sal;
    private float tds;
    private String ename;
    private int year;

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getEid() {
        return eid;
    }

    public void setBase_sal(float base_sal) {
        this.base_sal = base_sal;
    }

    public float getGross_sal() {
        return gross_sal;
    }

    public void setTds(float tds) {
        this.tds = tds;
    }

    public float getBase_sal() {
        return base_sal;
    }

    public void setGross_sal(float gross_sal) {
        this.gross_sal = gross_sal;
    }

    public float getTds() {
        return tds;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEname() {
        return ename;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
