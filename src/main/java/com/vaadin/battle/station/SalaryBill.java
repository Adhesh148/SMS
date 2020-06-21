package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.Employee;
import com.vaadin.battle.station.backend.Salary;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.GeneratedVaadinTextArea;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

@Route("SalaryBill")
public class SalaryBill extends VerticalLayout
{
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    TreeGrid<Salary> salaryGrid = new TreeGrid<>(Salary.class);

    H1 Title = new H1("Salary Bill");

    DatePicker start = new DatePicker();
    DatePicker end = new DatePicker();

    ComboBox filter = new ComboBox("Search by name");
    ComboBox idFilter = new ComboBox("Search by id");

    Button button = new Button("Show All");
    Button half1 = new Button("JAN - JUN");
    Button half2 = new Button("JUL - DEC");
    Button full = new Button("THIS YEAR");

    Button year = new Button("YEAR");

    Button show_total = new Button("Headwise Total");
    Text SALARY = new Text("TOTAL SALARY" + "     ");
    Text TDS = new Text("TOTAL TDS");

    Date pd = new Date();

    int total_salary = 0;
    int total_tds = 0;

    public SalaryBill()
    {
        addClassName("salary-bill");

        configureGrid();

        updateComboBox(filter);
        updateIDComboBox(idFilter);

        start.setValue(LocalDate.now());
        start.setLabel("From");

        end.setLabel("To");
        end.setValue(LocalDate.now());

        end.setEnabled(false);

        start.addValueChangeListener(e -> end.setEnabled(true));
        start.addValueChangeListener(e -> end.setMin(start.getValue()));
        end.addValueChangeListener(e -> fillSalaryGrid());
        end.addValueChangeListener(e -> fillSalaryGrid());
        end.addValueChangeListener(e -> textUpdate());

        LocalDate thisYear = LocalDate.now();
        LocalDate h11 = LocalDate.of(thisYear.getYear(), 01, 01);
        LocalDate h12 = LocalDate.of(thisYear.getYear(),06, 30);

        LocalDate h21 = LocalDate.of(thisYear.getYear(), 07, 01);
        LocalDate h22 = LocalDate.of(thisYear.getYear(), 12, 31);

        half1.addClickListener(e -> start.setValue(h11));
        half1.addClickListener(e -> start.setValue(h11));
        half1.addClickListener(e -> end.setValue(h12));
        half1.addClickListener(e -> end.setValue(h12));
        half1.addThemeName(Lumo.LIGHT);

        half2.addClickListener(e -> start.setValue(h21));
        half2.addClickListener(e -> start.setValue(h21));
        half2.addClickListener(e -> end.setValue(h22));
        half2.addClickListener(e -> end.setValue(h22));
        half2.addThemeName(Lumo.LIGHT);

        full.addClickListener(e -> start.setValue(h11));
        full.addClickListener(e -> start.setValue(h11));
        full.addClickListener(e -> end.setValue(h22));
        full.addClickListener(e -> end.setValue(h22));
        full.addThemeName(Lumo.LIGHT);


       // filter.addValueChangeListener(e -> setID());
       // filter.addValueChangeListener(e -> setID());
        filter.addValueChangeListener(e -> fillSalaryGridFilter(filter));
        filter.addValueChangeListener(e -> fillSalaryGridFilter(filter));
        filter.addValueChangeListener(e -> textUpdate());

        button.addClickListener(e -> fillSalaryGrid());
        button.addClickListener(e -> fillSalaryGrid());
        button.addThemeName(Lumo.DARK);

        HorizontalLayout filterLine = new HorizontalLayout();
        filterLine.add(filter, button);
        filterLine.setAlignItems(Alignment.BASELINE);

        HorizontalLayout dateLine = new HorizontalLayout();
        dateLine.add(start, end, half1, half2, full);
        dateLine.setAlignItems(Alignment.BASELINE);

        show_total.addClickListener(e -> fillSalaryGridTotalPerHead());
        show_total.addClickListener(e -> fillSalaryGridTotalPerHead());

        add(Title, SALARY, new VerticalLayout(dateLine, filterLine), show_total, salaryGrid);
    }

    private void setID()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            // Find the number of distinct batches.
            sql = "select distinct eid from employees where ename = '" + filter.getValue() + "'";
            rs = stmt.executeQuery(sql);

            Collection data = new ArrayList<>();



            while(rs.next())
            {
                String entry;
                entry = rs.getString("eid");

                data.add(entry);
            }
            idFilter.setItems(data);
        }catch(Exception e){
            e.getLocalizedMessage();
        }
    }

    private void setName()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            // Find the number of distinct batches.
            sql = "select distinct ename from employees where eid = '" + idFilter.getValue() + "'";
            rs = stmt.executeQuery(sql);

            Collection data = new ArrayList<>();



            while(rs.next())
            {
                String entry;
                entry = rs.getString("ename");

                data.add(entry);

                filter.setValue(entry);

            }
            filter.setItems(data);


        }catch(Exception e){
            e.getLocalizedMessage();
        }
    }

    private void fillSalaryGridFilter(ComboBox filter)
    {
        int total =0;
        total_tds = 0;
        total_salary =0;

        try
        {

            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();

            String sql = "select salary.eid, ename, base_sal, da, pay_date, hra, arrear, ta, tds, license_fee, deductions from salary inner join employees e on salary.eid = e.eid where ename = '" + filter.getValue() + "'";
            ResultSet rs = stmt.executeQuery(sql);

            Collection<Salary> data = new ArrayList<>();


            while(rs.next()) {
                Salary entry = new Salary();

                entry.setEname(rs.getString("ename"));
                entry.setEid(rs.getInt("eid"));
                entry.setBase_sal(rs.getInt("base_sal"));
                entry.setTds(rs.getInt("tds"));
                entry.setTa(rs.getInt("ta"));
                entry.setDa(rs.getInt("da"));
                entry.setArrear(rs.getInt("arrear"));
                entry.setHra(rs.getInt("hra"));
                entry.setPay_date(rs.getDate("pay_date"));
                entry.setDeductions(rs.getInt("deductions"));
                entry.setLicense_fee(rs.getInt("license_fee"));
                total = entry.getBase_sal() + entry.getTa() + entry.getArrear() + entry.getTds() + entry.getHra() + entry.getDa() - entry.getDeductions() - entry.getLicense_fee();
                entry.setTotal(total);

                if (entry.getPay_date() != null) {
                    pd = entry.getPay_date();


                    LocalDate payDate = convertToLocalDateViaMillisecond(pd);

                    if ((end.getValue().isAfter(payDate) || end.getValue().isEqual(payDate)) && (start.getValue().isBefore(payDate) || start.getValue().isEqual(payDate))) {

                        total_salary += total;
                        total_tds += entry.getTds();

                        data.add(entry);

                    }
                }
            }

            salaryGrid.setItems(data);

        }
        catch (Exception e)
        {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateComboBox(ComboBox filter)
    {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            // Find the number of distinct batches.
            sql = "select distinct (ename) from employees";
            rs = stmt.executeQuery(sql);

            Collection data = new ArrayList<>();



            while(rs.next())
            {
                String entry;
                entry = rs.getString("ename");

                data.add(entry);
            }
            filter.setItems(data);
        }catch(Exception e){
            e.getLocalizedMessage();
        }
    }

    private void updateIDComboBox(ComboBox filter)
    {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            // Find the number of distinct batches.
            sql = "select eid from employees";
            rs = stmt.executeQuery(sql);

            Collection data = new ArrayList<>();



            while(rs.next())
            {
                String entry;
                entry = rs.getString("eid");

                data.add(entry);
            }
            filter.setItems(data);
        }catch(Exception e){
            e.getLocalizedMessage();
        }
    }

    private void textUpdate()
    {
        SALARY.setText("TOTAL SALARY = " + total_salary + "     ");
        TDS.setText("TOTAL TDS = " + total_tds);

    }

    public LocalDate convertToLocalDateViaMillisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void configureGrid() {
        salaryGrid.setColumnOrder(
                salaryGrid.getColumnByKey("eid"),
                salaryGrid.getColumnByKey("ename"),
                salaryGrid.getColumnByKey("base_sal"),
                salaryGrid.getColumnByKey("da"),
                salaryGrid.getColumnByKey("deductions"),
                salaryGrid.getColumnByKey("hra"),
                salaryGrid.getColumnByKey("license_fee"),
                salaryGrid.getColumnByKey("arrear"),
                salaryGrid.getColumnByKey("ta"),
                salaryGrid.getColumnByKey("tds"),
                salaryGrid.getColumnByKey("total"),
                salaryGrid.getColumnByKey("pay_date")

        );
    }


    private void fillSalaryGrid()
    {
        int total;

        total_salary = 0;
        total_tds = 0;


        try
        {

            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();

            String sql = "select salary.eid, ename, base_sal, da, pay_date, hra, arrear, ta, tds, license_fee, deductions from salary inner join employees e on salary.eid = e.eid";
            ResultSet rs = stmt.executeQuery(sql);

            Collection<Salary> data = new ArrayList<>();


            while(rs.next()) {
                Salary entry = new Salary();

                entry.setEname(rs.getString("ename"));
                entry.setEid(rs.getInt("eid"));
                entry.setBase_sal(rs.getInt("base_sal"));
                entry.setTds(rs.getInt("tds"));
                entry.setTa(rs.getInt("ta"));
                entry.setDa(rs.getInt("da"));
                entry.setArrear(rs.getInt("arrear"));
                entry.setHra(rs.getInt("hra"));
                entry.setPay_date(rs.getDate("pay_date"));
                entry.setDeductions(rs.getInt("deductions"));
                entry.setLicense_fee(rs.getInt("license_fee"));
                total = entry.getBase_sal() + entry.getTa() + entry.getArrear() + entry.getTds() + entry.getHra() + entry.getDa() - entry.getDeductions() - entry.getLicense_fee();
                entry.setTotal(total);

                if (entry.getPay_date() != null) {
                    pd = entry.getPay_date();


                    LocalDate payDate = convertToLocalDateViaMillisecond(pd);

                    if ((end.getValue().isAfter(payDate) || end.getValue().isEqual(payDate)) && (start.getValue().isBefore(payDate) || start.getValue().isEqual(payDate))) {

                        total_salary += total;
                        total_tds += entry.getTds();

                        data.add(entry);

                    }
                }
            }

            salaryGrid.setItems(data);

        }
        catch (Exception e)
        {
            Notification.show(e.getLocalizedMessage());
        }



    }

    private void fillSalaryGridTotalPerHead()
    {
        int total;

        total_salary = 0;
        total_tds = 0;


        try
        {


            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();

            String sql = "select salary.eid, ename, SUM(base_sal) as base_sal, SUM(da) as da, SUM(ta) as ta, SUM(tds) as tds, SUM(license_fee) as license_fee, SUM(deductions) as deductions, SUM(arrear) as arrear, SUM(hra) as hra from salary inner join employees e on salary.eid = e.eid GROUP BY e.eid";
            ResultSet rs = stmt.executeQuery(sql);

            Collection<Salary> data = new ArrayList<>();


            while(rs.next())
            {


                Salary entry = new Salary();

                entry.setEname(rs.getString("ename"));
                entry.setEid(rs.getInt("eid"));
                entry.setBase_sal(rs.getInt("base_sal"));
                entry.setTds(rs.getInt("tds"));
                entry.setTa(rs.getInt("ta"));
                entry.setDa(rs.getInt("da"));
                entry.setArrear(rs.getInt("arrear"));
                entry.setHra(rs.getInt("hra"));
                entry.setPay_date(null);
                entry.setDeductions(rs.getInt("deductions"));
                entry.setLicense_fee(rs.getInt("license_fee"));
                total = entry.getBase_sal() + entry.getTa() + entry.getArrear() + entry.getTds() + entry.getHra() + entry.getDa() - entry.getDeductions() - entry.getLicense_fee();
                entry.setTotal(total);


                total_salary += total;
                total_tds += entry.getTds();

                data.add(entry);
            }


            salaryGrid.setItems(data);

        }
        catch (Exception e)
        {
            Notification.show(e.getLocalizedMessage());
        }



    }
}
