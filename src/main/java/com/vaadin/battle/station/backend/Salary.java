package com.vaadin.battle.station.backend;

import java.util.Date;

public class Salary
{
    private int eid;
    private float total;
    private String ename;
    private float base_sal;
    private Date pay_date;
    private float da;
    private float hra;
    private float arrear;
    private float ta;
    private float tds;
    private float license_fee;
    private float deductions;

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEname() {
        return ename;
    }

    public Date getPay_date() {
        return pay_date;
    }

    public float getArrear() {
        return arrear;
    }


    public float getDa() {
        return da;
    }

    public float getDeductions() {
        return deductions;
    }

    public int setEid(int eid) {
        return this.eid = eid;
    }

    public float getHra() {
        return hra;
    }

    public float getLicense_fee() {
        return license_fee;
    }

    public float getTa() {
        return ta;
    }

    public float getTds() {
        return tds;
    }

    public void setArrear(float arrear) {
        this.arrear = arrear;
    }

    public void setBase_sal(float base_sal) {
        this.base_sal = base_sal;
    }

    public float getBase_sal() {
        return base_sal;
    }

    public void setDa(float da) {
        this.da = da;
    }

    public void setDeductions(float deductions) {
        this.deductions = deductions;
    }

    public float getEid() {
        return eid;
    }

    public void setHra(float hra) {
        this.hra = hra;
    }

   public void setLicense_fee(float license_fee) {
        this.license_fee = license_fee;
    }

    public void setPay_date(Date pay_date) {
        this.pay_date = pay_date;
    }

    public void setTa(float ta) {
        this.ta = ta;
    }

    public void setTds(float tds) {
        this.tds = tds;
    }

}
