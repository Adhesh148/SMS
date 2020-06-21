package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.BasicSalaryTable;
import com.vaadin.battle.station.backend.EmployeeTable;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

public class EmployeeForm extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");
    TextField eId = new TextField("Employee ID");
    TextField eName = new TextField("Employee Name");
    DatePicker doj = new DatePicker("Date of Joining");
    DatePicker dor = new DatePicker("Date of Resignation");
    TextField qType = new TextField("Quarter Type");

    int originalEid = 0;

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    public EmployeeForm(){
        eId.setWidth("300px");
        eId.setEnabled(false);
        eName.setWidth("300px");
        doj.setWidth("300px");
        dor.setWidth("300px");
        qType.setWidth("300px");
        this.setWidth("400px");

        add(header, guideline, eId, eName, doj, dor,qType, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(evt -> this.setVisible(false));

        save.addClickListener(evt -> saveEmployee());

        return new HorizontalLayout(save, close);
    }

    private void saveEmployee() {
        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Edit");
        Label message = new Label("Are you sure you want to update the item?");
        Button cancel = new Button("Cancel");
        Button update = new Button("Update");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FlexLayout UpdateButtonWrapper = new FlexLayout(update);
        UpdateButtonWrapper.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout dialogButtons = new HorizontalLayout(cancel, UpdateButtonWrapper);
        dialogButtons.expand(UpdateButtonWrapper);

        dialog.add(new VerticalLayout(header, message), dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();

        update.addClickListener(evt -> {
            if(eId.isEnabled() == true)
                onADD();
            else
                onSave();
            dialog.close();
            update.getUI().ifPresent(ui -> ui.navigate(""));
            update.getUI().ifPresent(ui -> ui.navigate("empl_a"));
        });

        cancel.addClickListener(evt -> dialog.close());
    }

    private void onADD() {
        String newEname = eName.getValue();
        int newEid = Integer.parseInt(eId.getValue());
        String newDoj = String.valueOf(doj.getValue());
        String newDor = String.valueOf(dor.getValue());
        int newQtype;

        if(qType.getValue().equalsIgnoreCase(""))
            newQtype =0;
        else
            newQtype = Integer.parseInt(qType.getValue());

        Notification.show(String.valueOf(newQtype));

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int rs,rs1;
            String sql;
            if(dor.getValue() == null)
                sql = "Insert into employees(eid,ename,doj) values ("+newEid+",'"+newEname+"','"+newDoj+"');";
            else
                sql = "Insert into employees values ("+newEid+",'"+newEname+"','"+newDoj+"','"+newDor+"');";
            String sql1 = "insert into emp_quarters values("+newEid+","+newQtype+");";
            rs = stmt.executeUpdate(sql);
            rs1 = stmt.executeUpdate(sql1);
            if(rs>0 && rs1>0)
                Notification.show("Row Successfully Inserted.", 2000,Notification.Position.MIDDLE);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void onSave() {
        String newEname = eName.getValue();
        String newDoj = String.valueOf(doj.getValue());
        String newDor = String.valueOf(dor.getValue());
        int newQtype;
        if(qType.getValue().equalsIgnoreCase(""))
            newQtype =0;
        else
            newQtype = Integer.parseInt(qType.getValue());
//        Notification.show(newDoj);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            int rs,rs1;
            String sql,sql1;
            if(dor.getValue() == null)
                sql = "update employees set ename = '"+newEname+"', doj = '"+newDoj+"' where eid = "+originalEid+";";
            else
                sql = "update employees set ename = '"+newEname+"', doj = '"+newDoj+"',dor = '"+newDor+"' where eid = "+originalEid+";";
            sql1 = "update emp_quarters set qtype = "+newQtype+" where eid = "+originalEid+";";
            rs = stmt.executeUpdate(sql);
            rs1 = stmt.executeUpdate(sql1);
            if (rs > 0 && rs1>0)
                Notification.show("Row Successfully Updated.", 2000, Notification.Position.MIDDLE);
            else
                Notification.show("Row cannot be updated.", 2000, Notification.Position.MIDDLE);
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage(), 2000, Notification.Position.MIDDLE);
        }
    }

    public void setInformation(EmployeeTable value) {
        eId.setValue(String.valueOf(value.getEid()));
        eName.setValue(value.getEname());
        doj.setValue(LocalDate.parse(value.getDoj()));
        qType.setValue(String.valueOf(value.getQtype()));
        if (value.getDor() != null)
            dor.setValue(LocalDate.parse(value.getDor()));

        originalEid = value.getEid();
    }
}
