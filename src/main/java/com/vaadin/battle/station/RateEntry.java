package com.vaadin.battle.station;

public class RateEntry {
    int rid;
    String rate_name;
    int rate;

    public int getRID(){
        return rid;
    }
    public String getRateName(){
        return rate_name;
    }

    public int getRate(){
        return rate;
    }

    public void setRate(int new_rate){
        this.rate = new_rate;
    }

    public void setRate_name(String name){
        this.rate_name = name;
    }

    public void setRID(int id){
        this.rid = id;
    }
}

