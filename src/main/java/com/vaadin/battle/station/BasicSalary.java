package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.BasicSalaryTable;
import com.vaadin.battle.station.backend.DeductionsParam;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "basic_sal", layout = MainView.class)
@PageTitle("Basic Pay | Salary Management")
public class BasicSalary extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    Grid<BasicSalaryTable> grid = new Grid<>(BasicSalaryTable.class);
    TextField filterText = new TextField();
    BasicSalaryForm form = new BasicSalaryForm();

    Label heading = new Label("Employee Salary");
    Label message = new Label("Edit Basic Pay of Employees");

    public BasicSalary() {
        this.getStyle().set("display", "flex");
        setSizeFull();

        configureFilter(filterText);
        configureGrid(grid);
        fillBasicGrid();

        HorizontalLayout toolBar = new HorizontalLayout(filterText);
        toolBar.setAlignSelf(Alignment.CENTER);

        Div content = new Div(grid, form);
        content.getStyle().set("display", "flex");
        content.setSizeFull();
        form.setVisible(false);

        heading.addClassName("main-heading");
        message.addClassName("main-message");

        add(heading, message, new Hr());
        add(toolBar, content);
    }


    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by Ename.");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void configureGrid(Grid<BasicSalaryTable> grid) {
        grid.setColumns("eid","ename","basicSalary","grossSalary");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
    }

    public void fillBasicGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            sql = "select e.eid,e.ename,s.base_sal,s.gross_sal from employees e inner join emp_salary s on e.eid = s.eid;";
            rs = stmt.executeQuery(sql);

            Collection<BasicSalaryTable> data = new ArrayList<>();

            while (rs.next()) {
               BasicSalaryTable entry = new BasicSalaryTable();
                entry.setEid(rs.getInt("eid"));
                entry.setEname(rs.getString("ename"));
                entry.setBasicSalary(rs.getFloat("base_sal"));
                entry.setGrossSalary(rs.getFloat("gross_sal"));
                data.add(entry);
            }
            grid.setItems(data);

        } catch (Exception exception) {
            Notification.show(exception.getLocalizedMessage());
        }
    }

    private void updateList() {
        String filter = filterText.getValue();
        grid.setItems();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            // Get values from backend matching the filter pattern
            sql = "select e.eid,e.ename,s.base_sal,s.gross_sal from employees e inner join emp_salary s on e.eid = s.eid where e.ename like '%" + filter + "%'";
            rs = stmt.executeQuery(sql);

            Collection<BasicSalaryTable> data = new ArrayList<>();
            while (rs.next()) {
                BasicSalaryTable entry = new BasicSalaryTable();
                entry.setEid(rs.getInt("eid"));
                entry.setEname(rs.getString("ename"));
                entry.setBasicSalary(rs.getFloat("base_sal"));
                entry.setGrossSalary(rs.getFloat("gross_sal"));
                data.add(entry);
            }

            grid.setItems(data);
            rs.close();
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void editForm(BasicSalaryTable value) {
        if (value == null) {
            form.setVisible(false);
        } else {
            form.setVisible(true);
            form.setInformation(value);
        }
    }
}
