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

@Route(value = "rate_a",layout = MainView.class)
@PageTitle("Rate View")

public class RateView extends VerticalLayout{
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    Grid<RateEntry> RateGrid = new Grid<>(RateEntry.class);
    RateEditForm form = new RateEditForm();
//    TextField filterText = new TextField("filter");
    Label heading = new Label("Rates");
    Label message = new Label("Add or Update Rates");
    public RateView() {
        this.getStyle().set("display", "flex");

        configureGrid(RateGrid);
        fillRateGrid();

        Div content = new Div(RateGrid,form);
        content.getStyle().set("display", "flex");
        content.addClassName("course-content");
        content.setSizeFull();
        form.setVisible(false);

        heading.addClassName("main-heading");
        message.addClassName("main-message");
        add(heading,message, new Hr());
        add(content);
    }

    private void configureGrid(Grid<RateEntry> rateGrid) {
        rateGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        rateGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        rateGrid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
    }

    private void editForm(RateEntry value) {
        if (value == null)
            closeEditor();
        else
        {

            form.setVisible(true);
            form.setInformation(value);
        }
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

    private void closeEditor() {
        form.setVisible(false);
    }


}
