package com.vaadin.battle.station.backend;

import java.time.LocalDate;

public class ChangeRate {
    private String rateName;
    private String dateImpl;
    private float hike;
    private String dateEff;



    public String getDateImpl() {
        return dateImpl;
    }

    public void setDateImpl(String dateImpl) {
        this.dateImpl = dateImpl;
    }

    public float getHike() {
        return hike;
    }

    public void setHike(float hike) {
        this.hike = hike;
    }

    public String getDateEff() {
        return dateEff;
    }

    public void setDateEff(String dateEff) {
        this.dateEff = dateEff;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }
}
