package com.vaadin.battle.station.Report;

public class SalaryReport {
    private int eid;
    private String payDate;
    private float baseSal;
    private float da;
    private float hra;
    private float arrear;
    private float ta;
    private float tds;
    private float licenseFee;
    private float deductions;

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public float getBaseSal() {
        return baseSal;
    }

    public void setBaseSal(float baseSal) {
        this.baseSal = baseSal;
    }

    public float getDa() {
        return da;
    }

    public void setDa(float da) {
        this.da = da;
    }

    public float getHra() {
        return hra;
    }

    public void setHra(float hra) {
        this.hra = hra;
    }

    public float getArrear() {
        return arrear;
    }

    public void setArrear(float arrear) {
        this.arrear = arrear;
    }

    public float getTa() {
        return ta;
    }

    public void setTa(float ta) {
        this.ta = ta;
    }

    public float getTds() {
        return tds;
    }

    public void setTds(float tds) {
        this.tds = tds;
    }

    public float getLicenseFee() {
        return licenseFee;
    }

    public void setLicenseFee(float licenseFee) {
        this.licenseFee = licenseFee;
    }

    public float getDeductions() {
        return deductions;
    }

    public void setDeductions(float deductions) {
        this.deductions = deductions;
    }
}
