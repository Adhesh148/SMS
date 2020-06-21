package com.vaadin.battle.station;


import com.vaadin.battle.station.backend.Tax;
import com.vaadin.battle.station.security.MyUserDetails;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "tax-view-admin",layout = MainView.class)
@PageTitle("Tax View | SMS")
public class Tax_admin extends VerticalLayout{
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";
    Grid<Tax> grid = new Grid<>(Tax.class);
    TextField filterText = new TextField();

    Label heading = new Label("Tax Invoice");
    Label message = new Label("Search and view TDS details of employees");

    public Tax_admin(){
        setClassName("faculty-class-u");
        setSizeFull();
        configureGrid(grid);
        fillTaxGrid();

        configureFilter(filterText);

        Div content = new Div(grid);
        content.addClassName("faculty-content");
        content.setSizeFull();

        HorizontalLayout searchbar = new HorizontalLayout(filterText);
        searchbar.setAlignSelf(Alignment.CENTER);
        heading.addClassName("faculty-admin-heading");
        message.addClassName("faculty-admin-message");
        add(heading, message, new Hr());
        add(searchbar, content);

    }

    public void configureGrid(Grid<Tax> grid){
//        grid.setColumnOrder(grid.getColumnByKey("eid").setHeader("EmployeeTable ID"), grid.getColumnByKey("base_sal").setHeader("EmployeeTable ID"),
//                grid.getColumnByKey("gross_sal").setHeader("Gross Salary"),grid.getColumnByKey("TDS").setHeader("TDS"));
        grid.setColumns("eid","ename","year","base_sal","gross_sal","tds");
        grid.getColumnByKey("eid").setHeader("Employee ID");
        grid.getColumnByKey("ename").setHeader("Employee Name");
        grid.getColumnByKey("base_sal").setHeader("Base Salary");
        grid.getColumnByKey("gross_sal").setHeader("Gross Salary");
        grid.getColumnByKey("tds").setHeader("TDS");
        grid.getColumnByKey("year").setHeader("Fiscal Year");

        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setWidth("200px"));


    }

    public void configureFilter(TextField filterText){
        filterText.setPlaceholder("Filter by employee ID");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void updateList(){
        String filter = filterText.getValue();
        grid.setItems();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            sql = "select tax.eid, ename, base_sal, gross_sal, tds, year from employees join tax on employees.eid = tax.eid where tax.eid like '%"+filter+"%';";
            rs = stmt.executeQuery(sql);

            Collection<Tax> data = new ArrayList<>();
            while(rs.next()){
                Tax tax = new Tax();
                tax.setEid(rs.getInt("tax.eid"));
                tax.setEname(rs.getString("ename"));
                tax.setBase_sal(rs.getFloat("base_sal"));
                tax.setGross_sal(rs.getFloat("gross_sal"));
                tax.setTds(rs.getFloat("tds"));
                tax.setYear(rs.getInt("year"));
                data.add(tax);
            }
            grid.setItems(data);

        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }

    }

    public void fillTaxGrid(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            sql = "select tax.eid, ename, base_sal, gross_sal, tds, year from employees join tax on employees.eid = tax.eid;";
            rs = stmt.executeQuery(sql);

            Collection<Tax> data = new ArrayList<Tax>();

            while (rs.next()) {
                Tax tax = new Tax();
                tax.setEid(rs.getInt("tax.eid"));
                tax.setEname(rs.getString("ename"));
                tax.setBase_sal(rs.getFloat("base_sal"));
                tax.setGross_sal(rs.getFloat("gross_sal"));
                tax.setTds(rs.getFloat("tds"));
                tax.setYear(rs.getInt("year"));
                data.add(tax);
            }
            grid.setItems(data);
        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }
}


