package com.vaadin.battle.station;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;


public class RateEditForm extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");

    DatePicker iDate = new DatePicker("Implemented Date");
    DatePicker eDate = new DatePicker("To Date");

    public TextField rid = new TextField("Rate ID");
    public TextField rName = new TextField("Name");
    public TextField rate = new TextField("Rate");

    Button save = new Button("Save Changes");
    Button close = new Button("Close");

    public RateEditForm(){
        rid.setWidth("400px");
        rName.setWidth("400px");
        rate.setWidth("400px");

        rid.setReadOnly(true);
        rName.setReadOnly(true);

        iDate.setClearButtonVisible(true);
        eDate.setClearButtonVisible(true);
        iDate.setValue(LocalDate.now());
        eDate.setValue(LocalDate.now());

        this.setWidth("500px");
        this.setHeightFull();
        addClassName("rate-grid");

        add(header, guideline, rid, rName, rate, iDate, eDate, createButtonLayout());
    }

    private HorizontalLayout createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(evt -> {
            this.setVisible(false);
        });

        save.addClickListener(evt -> {
                saveRecord();
            });

        return new HorizontalLayout(save,close);
    }

    private void saveRecord() {
        boolean b =  (rName.getValue().contentEquals("DA") == false);

        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Edit");
        Label message = new Label();
        if (b){
            message.setText("Are you sure you want to update to the rate?");
        }
        else{
            message.setText("Are you sure you want to hike by this rate?");
        }
        Button cancel = new Button("Cancel");
        Button update = new Button("Update");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FlexLayout UpdateButton = new FlexLayout(update);
        UpdateButton.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout dialogButtons = new HorizontalLayout(cancel, UpdateButton);
        dialogButtons.expand(UpdateButton);

        dialog.add(new VerticalLayout(header, message), dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();



        update.addClickListener(evt-> {
            if (!b)
                update_change_rate();
            else {
                updateRate();
            }
            dialog.close();
            update.getUI().ifPresent(ui -> {ui.navigate("");});
            update.getUI().ifPresent(ui -> {ui.navigate("rate_a");});

        });
        cancel.addClickListener(evt -> {
            dialog.close();
        });
    }

    private void update_change_rate() {
        String newRate = rate.getValue();
        String effDate = eDate.getValue().toString();
        String impDate = eDate.getValue().toString();
        String rate_id = rid.getValue();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "insert into change_rate(rate_id, new_rate, date_effective, date_implemented) values("+rate_id+","+newRate+",'"+effDate+"','"+impDate+"') " ;
            stmt.execute(sql);
            Notification.show("DA updated.", 2000,Notification.Position.MIDDLE);
            con.close();
        }
        catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateRate() {
        String id = rid.getValue();
        String name = rName.getValue();
        String newRate = rate.getValue();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "update rates set rate_name ='" + name + "', rate = +" + newRate + " where rate_id = +" + id + ";";
            stmt.executeUpdate(sql);
            Notification.show("Row updated.", 2000,Notification.Position.MIDDLE);
            con.close();
        }
        catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void setInformation(RateEntry value) {
        rName.setValue(value.getRateName());
        rid.setValue(value.getRID()+"");
        rate.setValue(value.getRate()+"");
    }

}


