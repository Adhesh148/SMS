package com.vaadin.battle.station;

import com.vaadin.battle.station.security.MyUserDetails;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

@Route("")
@CssImport("./styles/shared-styles.css")
public class MainView extends AppLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    public MainView() {
        Label heading = new Label("Boiler Plate");
        Label message = new Label("A boiler plate for the DBMS Lab Exam Project.");

        // Get the login details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = "";
        String role = "";
        int eid =0;
        if (principal instanceof MyUserDetails) {
            userName = ((MyUserDetails) principal).getUsername();
            role = ((MyUserDetails) principal).getAuthorities().toString();
            role = role.substring(1, role.length() - 1);
            eid = ((MyUserDetails) principal).getEid();
        }

        heading.addClassName("main-heading");
        message.addClassName("main-message");

        createHeader(userName);
        createDrawer(role);

        this.setContent(new VerticalLayout(heading, message));
    }

    private void createDrawer(String role) {
        RouterLink home = new RouterLink("Home", MainView.class);
        RouterLink dashboard_a = new RouterLink("Dashboard", DashboardAdmin.class);
        RouterLink dashboard_u = new RouterLink("Dashboard", DashboardUser.class);
        RouterLink salaryBill = new RouterLink("SalaryBill", SalaryBill.class);
        RouterLink salarySlip = new RouterLink("SalarySlip", SalarySlip.class);

        home.addClassName("router-link");
        dashboard_a.addClassName("router-link");
        dashboard_u.addClassName("router-link");

        if (role.equalsIgnoreCase("Admin")) {
            addToDrawer(new VerticalLayout(home, dashboard_a, salaryBill, salarySlip));
        } else if (role.equalsIgnoreCase("User")) {
            addToDrawer(new VerticalLayout(home, dashboard_u));
        }
    }

    private void createHeader(String userName) {
        H1 logo = new H1("Boiler Plate");
        logo.addClassName("logo");

        Label username = new Label("Hello " + userName);
        username.addClassName("username");

        // Dark Mode Toggle Button
        ToggleButton themeToggle = new ToggleButton();
        themeToggle.addClickListener(toggleButtonClickEvent -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });

        // User Menubar
        MenuBar menuBar = new MenuBar();
        MenuItem user = menuBar.addItem(new Icon(VaadinIcon.USER));
        Label name = new Label("Hi " + userName);
        name.addClassName("setVisible");
        user.getSubMenu().addItem(name).setEnabled(false);
        user.getSubMenu().add(new Hr());
        //user.getSubMenu().addItem("Edit Personal Info",e-> UI.getCurrent().navigate("personal_info"));
        user.getSubMenu().addItem(new Anchor("/logout", "Sign Out"));
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.addClassName("header");
        header.setWidth("90%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        themeToggle.getStyle().set("padding-left", "10px");
        themeToggle.getStyle().set("padding-right", "10px");
        addToNavbar(header, menuBar, themeToggle);
    }
}
