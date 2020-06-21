package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.QuarterEntry;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "quarter_a", layout = MainView.class)
@PageTitle("Quarters | SMS")
public class QuarterView extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";
    Icon newAdd = new Icon(VaadinIcon.PLUS_CIRCLE);
    QuarterEdit form = new QuarterEdit();
    Label heading = new Label("Quarters");
    Label message = new Label("Add, Delete or Update Quarter Types");


    Grid<QuarterEntry> grid = new Grid<>(QuarterEntry.class);
    public QuarterView(){
        configureGrid(grid);
        fillGrid();

        Div content = new Div(grid,form);
        content.getStyle().set("display", "flex");
        content.addClassName("course-content");
        content.setSizeFull();
        form.setVisible(false);

        HorizontalLayout toolBar = new HorizontalLayout(message,newAdd);
        toolBar.setAlignSelf(Alignment.CENTER, newAdd);

        heading.addClassName("main-heading");
        message.addClassName("main-message");
        add(heading,toolBar,new Hr());

        newAdd.addClickListener(evt -> addRecord());

        add(content);
    }

    private void addRecord() {
        form.setVisible(true);
        form.qtype.setValue("");
        form.lFee.setValue("");
        form.upLim.setValue("");
        form.lowLim.setValue("");
    }

    private void fillGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select qtype, `ar_lowlimit(sqm)` low_lim, `ar_uplimit(sqm)` up_lim, license_fee fee from quarters";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<QuarterEntry> data = new ArrayList<>();
            while(rs.next()){
                QuarterEntry entry = new QuarterEntry();
                entry.setFee(rs.getFloat("fee"));
                entry.setLow_lim(rs.getInt("low_lim"));
                entry.setUp_lim(rs.getInt("up_lim"));
                entry.setQtype(rs.getInt("qtype"));
                data.add(entry);
            }
            rs.close();
            con.close();
            grid.setItems(data);
        }catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configureGrid(Grid<QuarterEntry> grid) {
        grid.setColumns("qtype","low_lim","up_lim","fee");
        grid.getColumnByKey("qtype").setHeader("Quarter Type");
        grid.getColumnByKey("low_lim").setHeader("Lower Limit");
        grid.getColumnByKey("up_lim").setHeader("Upper Limit");
        grid.getColumnByKey("fee").setHeader("Fee");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
        grid.setHeightByRows(true);
    }

    private void editForm(QuarterEntry value) {
        if (value == null)
            closeEditor();
        else
        {

            form.setVisible(true);
            form.setInformation(value);
        }
    }
    private void closeEditor() {
        form.setVisible(false);
    }

}
