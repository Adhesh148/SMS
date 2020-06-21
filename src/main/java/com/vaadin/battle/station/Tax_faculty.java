package com.vaadin.battle.station;



import com.vaadin.battle.station.backend.TaxFaculty;
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

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "tax-view",layout = MainView.class)
@PageTitle("Tax View | Salary Management System")
public class Tax_faculty extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";
    Grid<TaxFaculty> grid = new Grid<>(TaxFaculty.class);
    TextField filterText = new TextField();

    Label heading = new Label("Tax Invoice");
    Label message = new Label("View TDS details");

    public Tax_faculty(){
        setClassName("faculty-tax-u");
        setSizeFull();
        configureGrid(grid);
        fillTaxGrid();

        configureFilter(filterText);

        Div content = new Div(grid);
        content.addClassName("faculty-content");
        content.setSizeFull();
        add(content);

        HorizontalLayout searchbar = new HorizontalLayout(filterText);
        searchbar.setAlignSelf(Alignment.CENTER);
        heading.addClassName("faculty-admin-heading");
        message.addClassName("faculty-admin-message");
        add(heading, message, new Hr());
        add(searchbar, content);

    }
    public void fillTaxGrid(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userName = "";
            String role = "";
            int eid = 0;
            if (principal instanceof MyUserDetails) {
                userName = ((MyUserDetails) principal).getUsername();
                role = ((MyUserDetails) principal).getAuthorities().toString();
                role = role.substring(1, role.length() - 1);
                eid = ((MyUserDetails) principal).getEid();
            }
            sql = "select base_sal, gross_sal, tds, year from tax where eid = "+eid;
            rs = stmt.executeQuery(sql);

            Collection<TaxFaculty> data = new ArrayList<TaxFaculty>();

            while (rs.next()) {
                TaxFaculty emp = new TaxFaculty();
                emp.setBase_sal(rs.getFloat("base_sal"));
                emp.setGross_sal(rs.getFloat("gross_sal"));
                emp.setTds(rs.getFloat("tds"));
                emp.setYear(rs.getInt("year"));
                data.add(emp);
            }
            grid.setItems(data);
        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }


        public void configureGrid(Grid<TaxFaculty> grid){
        grid.getColumnByKey("base_sal").setHeader("Base Salary");
        grid.getColumnByKey("gross_sal").setHeader("Gross Salary");
        grid.getColumnByKey("tds").setHeader("TDS");
        grid.getColumnByKey("year").setHeader("Fiscal Year");

        grid.setSizeFull();

        grid.getColumns().forEach(col -> col.setWidth("300px"));
    }

    public void configureFilter(TextField filterText){
        filterText.setPlaceholder("Filter by year");
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
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userName = "";
            String role = "";
            int eid = 0;
            if (principal instanceof MyUserDetails) {
                userName = ((MyUserDetails) principal).getUsername();
                role = ((MyUserDetails) principal).getAuthorities().toString();
                role = role.substring(1, role.length() - 1);
                eid = ((MyUserDetails) principal).getEid();
            }
            String sql;
            ResultSet rs;
            sql = "select base_sal, gross_sal, tds, year from employees join tax on employees.eid = tax.eid where employees.eid = '" + eid +"' and year like '%"+filter+"%';";
            rs = stmt.executeQuery(sql);

            Collection<TaxFaculty> data = new ArrayList<>();
            while(rs.next()){
                TaxFaculty tax = new TaxFaculty();
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
}
