package com.vaadin.battle.station;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard_a",layout = MainView.class)
@PageTitle("Dashboard | Boiler Plate")
public class DashboardAdmin extends VerticalLayout {

    public DashboardAdmin(){
        Label heading  = new Label("DashBoard");
        heading.addClassName("main-heading");
        Label message = new Label("This is the dashboard for the Admin");
        message.addClassName("main-message");

        add(new VerticalLayout(heading,message));

    }

}
