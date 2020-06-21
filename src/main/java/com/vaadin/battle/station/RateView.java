package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.ChangeRate;
import com.vaadin.battle.station.backend.RateEntry;
import com.vaadin.battle.station.backend.TravelAllowance;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Route(value = "rate_a",layout = MainView.class)
@PageTitle("Rate View | SMS")

public class RateView extends VerticalLayout{
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    Grid<RateEntry> RateGrid = new Grid<>(RateEntry.class);
    RateEditForm form = new RateEditForm();
    Grid<ChangeRate> changeRateGrid = new Grid<>(ChangeRate.class);
    Grid<TravelAllowance> taGrid = new Grid<>(TravelAllowance.class);
//    TextField filterText = new TextField("filter");
    Label heading = new Label("Rates & Allowances");
    Label message = new Label("Add or Update Rates");
    Button addArrear = new Button("Hike DA");

    TextField filterText = new TextField();
    public RateView() {
        this.getStyle().set("display", "flex");

        configureGrid(RateGrid);
        configureChangeGrid(changeRateGrid);
        configureTaGrid(taGrid);
        configureFilter(filterText);
        fillRateGrid();
        fillChangeGrid();
        fillTaGrid();

        HorizontalLayout toolBar = new HorizontalLayout(filterText,addArrear);

        Div content = new Div(RateGrid,form);
        content.getStyle().set("display", "flex");
        content.addClassName("course-content");
        content.setSizeFull();
        form.setVisible(false);

        addArrear.addClickListener(buttonClickEvent -> {
            addAr();
        });

        heading.addClassName("main-heading");
        message.addClassName("main-message");

        Label head_da = new Label("Hike in DA");
        Label head_rate = new Label("Rate Percentages");
        Label head_ta = new Label("Travel Allowances");
        head_da.addClassName("main-message");
        head_rate.addClassName("main-message");
        head_ta.addClassName("main-message");


        add(heading,message,toolBar, new Hr());
        add(head_rate,new Hr(),content);
        add(new Hr(),head_da,new Hr(),changeRateGrid);
        add(new Hr(),head_ta,new Hr(),taGrid);
    }

    private void fillTaGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select * from travel_allowance;";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<TravelAllowance> data = new ArrayList<>();
            while(rs.next()){
                TravelAllowance entry = new TravelAllowance();
                entry.setLower_lim(rs.getFloat("lower_lim"));
                entry.setUpper_lim(rs.getFloat("upper_lim"));
                entry.setTa(rs.getFloat("ta"));
                data.add(entry);
            }
            rs.close();
            con.close();
            taGrid.setItems(data);
        }catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configureTaGrid(Grid<TravelAllowance> taGrid) {
        taGrid.setColumns("lower_lim","upper_lim","ta");
        taGrid.getColumnByKey("lower_lim").setHeader("Lower Limit");
        taGrid.getColumnByKey("upper_lim").setHeader("Upper Limit");
        taGrid.getColumnByKey("ta").setHeader("Travel Allowance");
        taGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        taGrid.setSortableColumns();
        taGrid.setHeightByRows(true);
        taGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private void fillChangeGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select date_implemented,new_rate,date_effective from change_rate";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<ChangeRate> data = new ArrayList<>();
            while(rs.next()){
                ChangeRate entry = new ChangeRate();
                entry.setRateName("DA");
                entry.setDateImpl(rs.getString("date_implemented"));
                entry.setDateEff(rs.getString("date_effective"));
                entry.setHike(rs.getFloat("new_rate"));
                data.add(entry);
            }
            rs.close();
            con.close();
            changeRateGrid.setItems(data);
        }catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configureChangeGrid(Grid<ChangeRate> changeRateGrid) {
        changeRateGrid.setColumns("rateName","dateEff","dateImpl","hike");
        changeRateGrid.getColumnByKey("rateName").setHeader("Rate");
        changeRateGrid.getColumnByKey("dateEff").setHeader("Effective Date");
        changeRateGrid.getColumnByKey("dateImpl").setHeader("Implementation Date");
        changeRateGrid.getColumnByKey("hike").setHeader("Hike Percent");
        changeRateGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        changeRateGrid.setSortableColumns();
        changeRateGrid.setHeightByRows(true);
        changeRateGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private void addAr() {
        Dialog dialog = new Dialog();

        H4 header = new H4("Add a DA Hike");
        Label message = new Label("Please note that this is supposed to be a government approved hike in DA.");
        Label message2 = new Label("Changes added might result in disbursement of arrears. Also only one hike per 6 months is permitted.");
        TextField hike = new TextField("Enter hike percent");
        DatePicker implDate = new DatePicker("Implementation Date");
        DatePicker effectiveDate = new DatePicker("Effective From");
        FormLayout body = new FormLayout(hike, implDate, effectiveDate);
        Button cancel = new Button("Cancel");
        Button update = new Button("Insert");

        hike.setRequired(true);
        hike.setRequiredIndicatorVisible(true);
        implDate.setRequired(true);
        implDate.setRequiredIndicatorVisible(true);
        effectiveDate.setRequired(true);
        effectiveDate.setRequiredIndicatorVisible(true);

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout dialogButtons = new VerticalLayout(new HorizontalLayout(cancel,update));

        dialog.add(new VerticalLayout(header,message,message2),body,dialogButtons);
        dialog.open();

        cancel.addClickListener(buttonClickEvent -> {
            dialog.close();
        });

        update.addClickListener(buttonClickEvent -> {
            float hike_value = Float.parseFloat(hike.getValue());
            addChangeRate(hike_value,implDate.getValue(),effectiveDate.getValue());

        });
    }

    private void addChangeRate(float hike_value, LocalDate impl, LocalDate effect) {

        try {
            int month_impl = impl.getMonthValue();
            int month_eff = effect.getMonthValue();
            int yr_impl = impl.getYear();
            int yr_eff = impl.getYear();

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
//            Review this condition and optimize to fit overflowing months like Jan - March
            String sql = "select count(*) as cnt from change_rate where  month(date_implemented)>= "+month_eff+" and month(date_effective) <= "+month_impl+" and year(date_implemented) = year(curdate());";
            ResultSet rs = stmt.executeQuery(sql);
            int flag=0;
            while (rs.next()){
                 flag = rs.getInt("cnt");
            }
            rs.close();
            int rs1;
            if(flag == 0){
                String sql1 = "insert into change_rate(rate_id,date_implemented,new_rate,date_effective) values("+1+",'"+impl+"',"+hike_value+",'"+effect+"');";
                rs1 = stmt.executeUpdate(sql1);
                if(rs1>0) {
                    Notification.show("Hike Updated Successfully");
                }else
                    Notification.show("Insertion Unsuccessful");
            }
            else
                Notification.show("A hike already exists during this duration");
            con.close();

        }catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by Rate Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void updateList() {
        String filter = filterText.getValue();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select rate_id, rate_name, rate from rates where rate_name like '%"+filter+"%'";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<RateEntry> data = new ArrayList<>();
            while(rs.next()){
                RateEntry entry = new RateEntry();
                entry.setRid(rs.getInt("rate_id"));
                entry.setRname(rs.getString("rate_name"));
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

    private void configureGrid(Grid<RateEntry> rateGrid) {
        rateGrid.setColumns("rid","rname","rate");
        rateGrid.getColumnByKey("rid").setHeader("Rate Id");
        rateGrid.getColumnByKey("rname").setHeader("Rate Name");
        rateGrid.getColumnByKey("rate").setHeader("Rate (%)");
        rateGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        rateGrid.setSortableColumns();
        rateGrid.setHeightByRows(true);
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
                entry.setRid(rs.getInt("rate_id"));
                entry.setRname(rs.getString("rate_name"));
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
