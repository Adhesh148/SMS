package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.BasicSalaryTable;
import com.vaadin.battle.station.backend.EmployeeTable;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "empl_a", layout = MainView.class)
@PageTitle("Employee View | Salary Management")
public class EmployeeView extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    Grid<EmployeeTable> grid = new Grid<>(EmployeeTable.class);
    TextField filterText = new TextField();
    EmployeeForm form = new EmployeeForm();
    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);

    Label heading = new Label("Employees");
    Label message = new Label("Edit Basic Info of Employees");

    public EmployeeView(){
        this.getStyle().set("display", "flex");
        setSizeFull();

        configureFilter(filterText);
        configureGrid(grid);
        fillEmpGrid();

        HorizontalLayout toolBar = new HorizontalLayout(filterText,addNew);
        toolBar.setAlignSelf(Alignment.CENTER, addNew);

        Div content = new Div(grid, form);
        content.getStyle().set("display", "flex");
        content.setSizeFull();
        form.setVisible(false);

        addNew.addClickListener(evt -> {
            addEmployee();
        });

        heading.addClassName("main-heading");
        message.addClassName("main-message");

        add(heading, message, new Hr());
        add(toolBar, content);
    }

    private void addEmployee() {
        form.setVisible(true);
        form.eId.setValue("");
        form.eId.setEnabled(true);
        form.eName.setValue("");
        form.doj.setValue(null);
        form.dor.setValue(null);
        form.qType.setValue("");
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by Ename.");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void configureGrid(Grid<EmployeeTable> grid) {
        grid.setColumns("eid","ename","doj","dor","qtype");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
    }

    public void fillEmpGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            sql = "select e.eid,e.ename,e.doj,e.dor,q.qtype from employees e inner join emp_quarters q on q.eid = e.eid;";
            rs = stmt.executeQuery(sql);

            Collection<EmployeeTable> data = new ArrayList<>();
            while (rs.next()) {
                EmployeeTable entry = new EmployeeTable();
                entry.setEid(rs.getInt("eid"));
                entry.setEname(rs.getString("ename"));
                entry.setDoj(rs.getString("doj"));
                String dorValue = rs.getString("dor");
                entry.setDor(dorValue);
                int qType = rs.getInt("qtype");
                entry.setQtype(qType);
                data.add(entry);
            }
            grid.setItems(data);
            rs.close();
            con.close();
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
            sql = "select e.eid,e.ename,e.doj,e.dor,q.qtype from employees e inner join emp_quarters q on q.eid = e.eid where e.ename like '%" + filter + "%'";
            rs = stmt.executeQuery(sql);

            Collection<EmployeeTable> data = new ArrayList<>();
            while (rs.next()) {
                EmployeeTable entry = new EmployeeTable();
                entry.setEid(rs.getInt("eid"));
                entry.setEname(rs.getString("ename"));
                entry.setDoj(rs.getString("doj"));
                entry.setDor(rs.getString("dor"));
                int qType = rs.getInt("qtype");
                entry.setQtype(qType);
                data.add(entry);
            }
            grid.setItems(data);
            rs.close();
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void editForm(EmployeeTable value) {
        if (value == null) {
            form.setVisible(false);
        } else {
            form.eId.setEnabled(false);
            form.setVisible(true);
            form.setInformation(value);
        }
    }
}
