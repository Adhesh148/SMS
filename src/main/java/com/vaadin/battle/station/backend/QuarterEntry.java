package com.vaadin.battle.station.backend;

public class QuarterEntry {
    private int qtype;
    private int low_lim;
    private int up_lim;
    private float fee;

    public void setQtype(int qtype){
        this.qtype= qtype;
    }
    public void setLow_lim(int low_lim){
        this.low_lim = low_lim;
    }
    public void setUp_lim(int up_lim){
        this.up_lim = up_lim;
    }
    public int getQtype(){
        return this.qtype;
    }
    public int getUp_lim(){
        return this.up_lim;
    }
    public int getLow_lim(){
        return this.low_lim;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
}
