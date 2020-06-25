package com.vaadin.battle.station.Report;

import com.vaadin.battle.station.MainView;
import com.vaadin.battle.station.security.MyUserDetails;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "print_bill",layout = MainView.class)
@PageTitle("Generate Pay Slip | SMS")
public class TryReport extends VerticalLayout {

    Button tryButton = new Button("Generate Invoice");
    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();
    int eid=0;
    Label heading = new Label("Generate Pay Slip");
    Label message = new Label("Get Account Summary of Last Month's Salary.");
    public TryReport(){

        FormLayout formLayout = new FormLayout();
        tryButton.setMaxWidth("300px");
        tryButton.addClassName("password-button");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            eid = ((MyUserDetails) principal).getEid();
        }

        tryButton.addClickListener(buttonClickEvent -> {
            invoiceGenerator.createPDF(eid);
            Notification.show("Invoice Successfully Generated");
        });

        Label msg = new Label("Click on the button to generate Invoice of Last Month's Payment");
        msg.addClassName("bolden");
        formLayout.add(new VerticalLayout(msg,tryButton));

        heading.addClassName("main-heading");
        message.addClassName("main-message");
        formLayout.setMaxWidth("900px");
        formLayout.addClassName("password-form");

        add(heading,message,new Hr(),formLayout);


    }
}
