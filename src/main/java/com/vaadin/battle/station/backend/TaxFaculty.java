package com.vaadin.battle.station.backend;

public class TaxFaculty {
    private float base_sal;
    private float gross_sal;
    private float tds;
    private int year;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
