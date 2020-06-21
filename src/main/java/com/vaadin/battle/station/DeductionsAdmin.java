package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.DeductionsParam;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "deductions_a", layout = MainView.class)
@PageTitle("Deductions | SMS")
public class DeductionsAdmin extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    Grid<DeductionsParam> grid = new Grid<>(DeductionsParam.class);
    DeductionsForm form = new DeductionsForm();

    TextField filterText = new TextField();
    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);

    Label heading = new Label("Deductions Record");
    Label message = new Label("Add, Edit and Delete records of Deductions");

    public DeductionsAdmin() {
        this.getStyle().set("display", "flex");
        setSizeFull();

        configureFilter(filterText);
        configureGrid(grid);
        fillDeductionsGrid();

        HorizontalLayout toolBar = new HorizontalLayout(filterText, addNew);
        toolBar.setAlignSelf(Alignment.CENTER, addNew);

        Div content = new Div(grid, form);
        content.getStyle().set("display", "flex");
        content.setSizeFull();
        form.setVisible(false);

        addNew.addClickListener(evt -> addFaculty());

        heading.addClassName("main-heading");
        message.addClassName("main-message");

        add(heading, message, new Hr());
        add(toolBar, content);
    }

    private void addFaculty() {
        form.setVisible(true);

        form.eid.setValue("");
        form.fromDate.setValue(LocalDate.now());
        form.toDate.setValue(LocalDate.now());
        form.remarks.setValue("");
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by EName.");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
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
            sql = "select * from sal_deductions where eid in (select eid from employees where ename like '%" + filter + "%')";
            rs = stmt.executeQuery(sql);

            Collection<DeductionsParam> data = new ArrayList<>();
            while (rs.next()) {
                DeductionsParam entry = new DeductionsParam();
                entry.setEid(rs.getString("eid"));
                entry.setFromDate(LocalDate.parse(rs.getString("from_date")));
                entry.setToDate(LocalDate.parse(rs.getString("to_date")));
                entry.setRemarks(rs.getString("remarks"));
                data.add(entry);
            }

            grid.setItems(data);
            rs.close();
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configureGrid(Grid<DeductionsParam> grid) {
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
    }

    public void fillDeductionsGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            sql = "select * from sal_deductions";
            rs = stmt.executeQuery(sql);

            Collection<DeductionsParam> data = new ArrayList<>();

            while (rs.next()) {
                DeductionsParam entry = new DeductionsParam();
                entry.setEid(rs.getString("eid"));
                entry.setFromDate(LocalDate.parse(rs.getString("from_date")));
                entry.setToDate(LocalDate.parse(rs.getString("to_date")));
                entry.setRemarks(rs.getString("remarks"));
                data.add(entry);
            }
            grid.setItems(data);

        } catch (Exception exception) {
            Notification.show(exception.getLocalizedMessage());
        }
    }

    private void editForm(DeductionsParam value) {
        if (value == null) {
            form.setVisible(false);
        } else {
            form.setVisible(true);
            form.setInformation(value);
        }
    }
}