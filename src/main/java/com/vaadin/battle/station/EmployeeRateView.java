package com.vaadin.battle.station;

import com.vaadin.flow.component.dependency.CssImport;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "rate_u",layout = MainView.class)
@PageTitle("EmployeeTable Rate View")

public class EmployeeRateView extends VerticalLayout{
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    Grid<RateEntry> RateGrid = new Grid<>(RateEntry.class);
    Label heading = new Label("Rates");
    Label message = new Label("Add or Update Rates");
    public EmployeeRateView() {
        this.getStyle().set("display", "flex");

        configureGrid(RateGrid);
        fillRateGrid();

        Div content = new Div(RateGrid);
        content.getStyle().set("display", "flex");
        content.addClassName("course-content");
        content.setSizeFull();

        heading.addClassName("heading");
        message.addClassName("message");
        add(heading,message, new Hr());
        add(content);
    }

    private void configureGrid(Grid<RateEntry> rateGrid) {
        rateGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        rateGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }
    private void fillRateGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select rate_id, rate_name, rate from rates";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<RateEntry> data = new ArrayList<>();
            while(rs.next()){
                RateEntry entry = new RateEntry();
                entry.setRID(rs.getInt("rate_id"));
                entry.setRate_name(rs.getString("rate_name"));
                entry.setRate(rs.getInt("rate"));
                data.add(entry);
            }
            rs.close();
            con.close();
            RateGrid.setItems(data);
        }catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }
}
