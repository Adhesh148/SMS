package com.vaadin.battle.station.backend;

import java.util.Date;
import java.util.Date;

public class Salary
{
    private int eid;
    private int total;
    private String ename;
    private int base_sal;
    private Date pay_date;
    private int da;
    private int hra;
    private int arrear;
    private int ta;
    private int tds;
    private int license_fee;
    private int deductions;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
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

    public int getArrear() {
        return arrear;
    }

    public int getBase_sal(int base_sal) {
        return this.base_sal;
    }

    public int getDa() {
        return da;
    }

    public int getDeductions() {
        return deductions;
    }

    public int setEid(int eid) {
        return this.eid = eid;
    }

    public int getHra() {
        return hra;
    }

    public int getLicense_fee() {
        return license_fee;
    }

    public int getTa() {
        return ta;
    }

    public int getTds() {
        return tds;
    }

    public void setArrear(int arrear) {
        this.arrear = arrear;
    }

    public void setBase_sal(int base_sal) {
        this.base_sal = base_sal;
    }

    public int getBase_sal() {
        return base_sal;
    }

    public void setDa(int da) {
        this.da = da;
    }

    public void setDeductions(int deductions) {
        this.deductions = deductions;
    }

    public int getEid() {
        return eid;
    }

    public void setHra(int hra) {
        this.hra = hra;
    }

    public void setLicense_fee(int license_fee) {
        this.license_fee = license_fee;
    }

    public void setPay_date(Date pay_date) {
        this.pay_date = pay_date;
    }

    public void setTa(int ta) {
        this.ta = ta;
    }

    public void setTds(int tds) {
        this.tds = tds;
    }

}