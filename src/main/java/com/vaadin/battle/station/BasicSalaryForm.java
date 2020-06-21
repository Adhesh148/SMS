package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.BasicSalaryTable;
import com.vaadin.battle.station.backend.DeductionsParam;
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
import java.time.LocalDate;

public class BasicSalaryForm extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");
    TextField eId = new TextField("Employee ID");
    TextField eName = new TextField("Employee Name");
    TextField basicPay = new TextField("Basic Pay");
    TextField grossPay = new TextField("Gross Pay");

    int originalEid = 0;

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    public BasicSalaryForm(){
        eId.setWidth("300px");
        eId.setEnabled(false);
        eName.setWidth("300px");
        eName.setEnabled(false);
        basicPay.setWidth("300px");
        grossPay.setWidth("300px");
        grossPay.setEnabled(false);
        this.setWidth("400px");

        add(header, guideline, eId, eName, basicPay, grossPay, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(evt -> this.setVisible(false));

        save.addClickListener(evt -> saveBasePay());

        return new HorizontalLayout(save, close);
    }

    private void saveBasePay() {
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
            onSave();
            dialog.close();
            update.getUI().ifPresent(ui -> ui.navigate(""));
            update.getUI().ifPresent(ui -> ui.navigate("basic_sal"));
        });

        cancel.addClickListener(evt -> dialog.close());
    }

    private void onSave() {
        float newbasicPay = Float.parseFloat(basicPay.getValue());
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            int rs,rs1;
            String sql = "delete from emp_salary where eid = "+originalEid+";";
            rs = stmt.executeUpdate(sql);
            String sql1 = "insert into emp_salary(eid,base_sal) values("+originalEid+","+newbasicPay+");";
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

    public void setInformation(BasicSalaryTable value) {
        eId.setValue(String.valueOf(value.getEid()));
        eName.setValue(value.getEname());
        basicPay.setValue(String.valueOf(value.getBasicSalary()));
        grossPay.setValue(String.valueOf(value.getGrossSalary()));

        originalEid = value.getEid();
    }
}
