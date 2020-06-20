package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.DeductionsParam;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

public class DeductionsForm extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    String originalEid = "";
    LocalDate originalFromDate = LocalDate.now();
    LocalDate originalToDate = LocalDate.now();
    String originalRemarks = "";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");
    TextField eid = new TextField("Employee ID");
    DatePicker fromDate = new DatePicker("From Date");
    DatePicker toDate = new DatePicker("To Date");
    TextField remarks = new TextField("Remarks");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public DeductionsForm() {
        eid.setWidth("300px");
        fromDate.setWidth("300px");
        toDate.setWidth("300px");
        remarks.setWidth("300px");
        this.setWidth("400px");

        fromDate.setClearButtonVisible(true);
        toDate.setClearButtonVisible(true);

        toDate.setMin(fromDate.getValue());

        add(header, guideline, eid, fromDate, toDate, remarks, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(evt -> this.setVisible(false));

        delete.addClickListener(evt -> deleteDeduction());

        save.addClickListener(evt -> saveDeduction());

        return new HorizontalLayout(save, delete, close);
    }

    private void saveDeduction() {
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
            if (originalEid.equals(""))
                onAdd();
            else
                onSave();
            dialog.close();
            update.getUI().ifPresent(ui -> ui.navigate(""));
            update.getUI().ifPresent(ui -> ui.navigate("deductions_a"));
        });

        cancel.addClickListener(evt -> dialog.close());
    }

    private void onAdd() {
        String newEid = eid.getValue();
        LocalDate newFromDate = fromDate.getValue();
        LocalDate newToDate = toDate.getValue();
        String newRemarks = remarks.getValue();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            boolean rs;
            String sql = "insert into sal_deductions values ('" + newEid + "','" + newFromDate + "','" + newToDate + "','" + newRemarks + "');";
            rs = stmt.execute(sql);
            Notification.show("Row Successfully Inserted.", 2000, Notification.Position.MIDDLE);
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void onSave() {
        String newEid = eid.getValue();
        LocalDate newFromDate = fromDate.getValue();
        LocalDate newToDate = toDate.getValue();
        String newRemarks = remarks.getValue();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            int rs;
            String sql = "update sal_deductions set eid ='" + newEid + "',from_date = '" + newFromDate + "',to_date ='" + newToDate + "',remarks = '" + newRemarks + "' where eid='" + originalEid + "';";
            rs = stmt.executeUpdate(sql);
            if (rs > 0)
                Notification.show("Row Successfully Updated.", 2000, Notification.Position.MIDDLE);
            else if (rs == 0)
                Notification.show("Row cannot be updated.", 2000, Notification.Position.MIDDLE);
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage(), 2000, Notification.Position.MIDDLE);
        }
    }

    private void deleteDeduction() {
        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Delete");
        Label message = new Label("Are you sure you want to delete the item?");
        Button cancel = new Button("Cancel");
        Button delete = new Button("Delete");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        FlexLayout deleteButtonWrapper = new FlexLayout(delete);
        deleteButtonWrapper.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout dialogButtons = new HorizontalLayout(cancel, deleteButtonWrapper);
        dialogButtons.expand(deleteButtonWrapper);

        dialog.add(new VerticalLayout(header, message), dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();

        delete.addClickListener(evt -> {
            onDelete();
            dialog.close();
            // Roundabout way to refresh the grid. Could look into another way..
            delete.getUI().ifPresent(ui -> ui.navigate(""));
            delete.getUI().ifPresent(ui -> ui.navigate("deductions_a"));
            //Could show a dialog box
        });
        cancel.addClickListener(evt -> dialog.close());

    }

    private void onDelete() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            int rs;
            String sql;
            sql = "delete from sal_deductions where eid = '" + eid.getValue() + "';";
            rs = stmt.executeUpdate(sql);
            if (rs > 0)
                Notification.show("Row Successfully Deleted.", 2000, Notification.Position.MIDDLE);
            else
                Notification.show("Unsuccessful.", 2000, Notification.Position.MIDDLE);
            this.setVisible(false);
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void setInformation(DeductionsParam value) {
        eid.setValue(value.getEid());
        fromDate.setValue(value.getFromDate());
        toDate.setValue(value.getToDate());
        remarks.setValue(value.getRemarks());

        originalEid = value.getEid();
        originalFromDate = value.getFromDate();
        originalToDate = value.getToDate();
        originalRemarks = value.getRemarks();
    }
}
