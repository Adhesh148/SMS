package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.QuarterEntry;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class QuarterEdit extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    int originalQtype = -1;

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");

    TextField qtype = new TextField("Quarter Type");
    TextField lowLim = new TextField("Lower Limit square meter");
    TextField upLim = new TextField("Upper Limit square meter");
    TextField lFee = new TextField("License Fee");

    Button save = new Button("Save Changes");
    Button close = new Button("Close");
    Button delete = new Button("Delete");

    public QuarterEdit(){
        qtype.setWidth("400px");
        lFee.setWidth("400px");
        lowLim.setWidth("400px");
        upLim.setWidth("400px");

        this.setWidth("500px");
        this.setHeightFull();
        addClassName("rate-grid");

        add(header, guideline, qtype, lowLim, upLim, lFee, lowLim, upLim, lFee, createButtonLayout());
    }
    public HorizontalLayout createButtonLayout(){
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);


        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);


        save.addClickListener(evt -> {
            saveRecord();
        });

        delete.addClickListener(evt -> {
            deleteRecord();
        });

        close.addClickListener(evt -> {
            this.setVisible(false);
        });

        return new HorizontalLayout(save, delete, close);
    }

    public void setInformation(QuarterEntry value) {
        qtype.setValue(String.valueOf(value.getQtype()));
        upLim.setValue(String.valueOf(value.getUp_lim()));
        lowLim.setValue(String.valueOf(value.getLow_lim()));
        lFee.setValue(String.valueOf(value.getFee()));

        originalQtype = value.getQtype();
        if(originalQtype == 0){
            if(value.getUp_lim() != 0 || value.getUp_lim()!=0)
                originalQtype = -1;
        }


    }

    private void saveRecord() {
        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Edit");
        Label message = new Label("Are you sure of the changes?");
        Button cancel = new Button("Cancel");
        Button update = new Button("Update");
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        cancel.addClickShortcut(Key.ESCAPE);
        
        HorizontalLayout dialogButtons = new HorizontalLayout(cancel,update);

        dialog.add(new VerticalLayout(header, message), dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();

        update.addClickListener(evt-> {
            if(originalQtype == -1)
                saveToTable();
            else
                updateToTable();
            dialog.close();
            update.getUI().ifPresent(ui -> {ui.navigate("");});
            update.getUI().ifPresent(ui -> {ui.navigate("quarter_a");});

        });

        cancel.addClickListener(evt -> {
            dialog.close();
        });
    }

    private void updateToTable() {
        int type= Integer.parseInt(qtype.getValue());
        int up = Integer.parseInt(upLim.getValue());
        int low = Integer.parseInt(lowLim.getValue());
        float lfee = Float.parseFloat(lFee.getValue());
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "update quarters set `ar_lowlimit(sqm)` = " + low +
                    ", `ar_uplimit(sqm)` = " + up +
                    ", license_fee = " + lfee +
                    " where qtype = " + type + ";";
            stmt.executeUpdate(sql);
            Notification.show("Row updated.", 2000,Notification.Position.MIDDLE);
            con.close();
        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void saveToTable() {
        int type= Integer.parseInt(qtype.getValue());
        int up = Integer.parseInt(upLim.getValue());
        int low = Integer.parseInt(lowLim.getValue());
        float lfee = Float.parseFloat(lFee.getValue());
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            sql = "insert into quarters values(" + type +
                        "," + low +
                        "," + up +
                        "," + lfee +
                        ");";
            stmt.execute(sql);
            Notification.show("Row Added.", 2000,Notification.Position.MIDDLE);
            con.close();

        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void deleteRecord() {
        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Edit");
        Label message = new Label("Are you sure want to delete the record?");
        Button cancel = new Button("Cancel");
        Button confirm = new Button("Confirm Delete");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirm.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        cancel.addClickShortcut(Key.ESCAPE);
        HorizontalLayout dialogButtons = new HorizontalLayout(cancel, confirm);

        dialog.add(new VerticalLayout(header, message), dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();
        cancel.addClickListener(evt -> {
            dialog.close();
        });
        confirm.addClickListener(evt -> {
            deletefromTable();
            dialog.close();
            confirm.getUI().ifPresent(ui -> {ui.navigate("");});
            confirm.getUI().ifPresent(ui -> {ui.navigate("quarter_a");});
        });

    }

    private void deletefromTable() {
        String type= qtype.getValue();
        String up = upLim.getValue();
        String low = lowLim.getValue();
        String lfee = lFee.getValue();
        if (type == "") {
            Notification.show("Deletion is Done by Type as key", 2000, Notification.Position.MIDDLE);
            return;
        }
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;

            sql = "delete from quarters where qtype=" + type + ";";
            stmt.execute(sql);
            Notification.show("Record deleted", 2000,Notification.Position.MIDDLE);
            con.close();

        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }
}
