package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.DashboardPayGrid;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "dashboard_a", layout = MainView.class)
@PageTitle("Dashboard | Boiler Plate")
public class DashboardAdmin extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    Grid<DashboardPayGrid> payGrid = new Grid<>(DashboardPayGrid.class);


    public DashboardAdmin() {
        H2 heading = new H2("Dashboard");
        add(heading);
        float employee_knt = getEmployeeCount();
        float salary_total = getTotalSalary();
        float salary_monthly = getMonthlySalary();
        float greatest_gross = getGreatestGross();
//        String slope = getSlope();
        dashboardPanel panel1 = new dashboardPanel(employee_knt, "Number of Employees");
        dashboardPanel panel2 = new dashboardPanel(salary_total, "Net Gross Salary (Year)");
        dashboardPanel panel3 = new dashboardPanel(salary_monthly, "Net Monthly Basic Pay");
        dashboardPanel panel4 = new dashboardPanel(greatest_gross, "Highest Gross Pay");
//        dashboardPanel panel5 = new dashboardPanel()
        HorizontalLayout cards = new HorizontalLayout(panel1, panel2, panel3, panel4);
        cards.addClassName("dashboard-card");
        add(cards);

        H4 grid_1 = new H4("Highest Earners");
        configurePayGrid(payGrid);
        fillPayGrid();
        add(new VerticalLayout(grid_1, payGrid));
    }

    private float getEmployeeCount() {
        float knt = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select count(distinct eid) as knt from tax;";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                knt = rs.getFloat("knt");
            rs.close();
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
        return knt;
    }

    private float getTotalSalary() {
        float total = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select sum(gross_sal) as total from tax group by year order by year desc limit 1;";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                total = rs.getFloat("total");
            rs.close();
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
        return total;
    }

    private float getMonthlySalary() {
        float total = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select sum(base_sal) as total from tax group by year order by year desc limit 1;";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                total = rs.getFloat("total");
            rs.close();
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
        return total;
    }

    private float getGreatestGross() {
        float highest = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select max(gross_sal) as greatest from tax group by year order by year desc limit 1;";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                highest = rs.getFloat("greatest");
            rs.close();
            con.close();
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
        return highest;
    }

    private void configurePayGrid(Grid<DashboardPayGrid> payGrid) {
        payGrid.setColumnOrder(payGrid.getColumnByKey("eid"), payGrid.getColumnByKey("ename"), payGrid.getColumnByKey("base_sal"),
                payGrid.getColumnByKey("gross_sal"), payGrid.getColumnByKey("year"));

        payGrid.getColumnByKey("eid").setHeader("Employee ID");
        payGrid.getColumnByKey("ename").setHeader("Employee Name");
        payGrid.getColumnByKey("base_sal").setHeader("Basic Pay");
        payGrid.getColumnByKey("gross_sal").setHeader("Gross Salary");
        payGrid.getColumnByKey("year").setHeader("Year");

        payGrid.setSortableColumns();
//        scheduleGrid.setHeightByRows(true);
    }

    private void fillPayGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select e.eid,e.ename,t.base_sal,t.gross_sal,(t.gross_sal - t.tds) as net,year from tax t inner join employees e on e.eid = t.eid where t.year = "+ LocalDate.now().getYear()+" order by net desc;";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<DashboardPayGrid> data = new ArrayList<>();
            while (rs.next()) {
                DashboardPayGrid entry = new DashboardPayGrid();
                entry.setEid(rs.getInt("eid"));
                entry.setBase_sal(rs.getFloat("base_sal"));
                entry.setEname(rs.getString("ename"));
                entry.setGross_sal(rs.getFloat("gross_sal"));
                entry.setYear(rs.getInt("year"));
                data.add(entry);
            }
            rs.close();
            con.close();
            payGrid.setItems(data);
        } catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }

    }
}