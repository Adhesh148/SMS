package com.vaadin.battle.station;

import com.vaadin.battle.station.backend.RateEntry;
import com.vaadin.battle.station.security.MyUserDetails;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "rate_u",layout = MainView.class)
@PageTitle("Employee Rate View | SMS")
public class EmployeeRateView extends VerticalLayout{
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";


    Label heading = new Label("Know Your Rates");
    Label message = new Label("View all Rates and Allowances applicable.");
    public EmployeeRateView() {
       this.getStyle().set("display", "flex");

        FormLayout formLayout = new FormLayout();
        TextField rate_da = new TextField("DA Rate");
        rate_da.setReadOnly(true);
        TextField rate_hra = new TextField("HRA Rate");
        rate_hra.setReadOnly(true);
        TextField base_incr = new TextField("Base Pay Increment Rate");
        base_incr.setReadOnly(true);
        TextField tdsSlab = new TextField("Tax Exemption Slab");
        tdsSlab.setReadOnly(true);
        TextField tdsRate = new TextField("TDS Rate");
        tdsRate.setReadOnly(true);
        TextField qtype = new TextField("Quarter Type");
        qtype.setReadOnly(true);
        TextField lf = new TextField("License Fee");
        lf.setReadOnly(true);
        TextField ta = new TextField("Travel Allowance");
        ta.setReadOnly(true);

        formLayout.add(rate_da,rate_hra,base_incr,tdsSlab,tdsRate,qtype,lf,ta);
        rate_da.setMaxWidth("300px");
        rate_da.addClassName("rate-form-text");
        rate_hra.setMaxWidth("300px");
        rate_hra.addClassName("rate-form-text");
        base_incr.setMaxWidth("300px");
        base_incr.addClassName("rate-form-text");
        tdsSlab.setMaxWidth("300px");
        tdsSlab.addClassName("rate-form-text");
        tdsRate.setMaxWidth("300px");
        tdsRate.addClassName("rate-form-text");
        qtype.setMaxWidth("300px");
        qtype.addClassName("rate-form-text");
        lf.setMaxWidth("300px");
        lf.addClassName("rate-form-text");
        ta.setMaxWidth("300px");
        ta.addClassName("rate-form-text");
        formLayout.setMaxWidth("800px");
        formLayout.addClassName("password-form");

        heading.addClassName("main-heading");
        message.addClassName("main-message");

        fillCommonRates(rate_da,tdsRate,tdsSlab,base_incr);
        fillSpecificRates(rate_hra,lf,qtype,ta);

        add(heading,message, new Hr(),formLayout);

    }

    private void fillSpecificRates(TextField rate_hra, TextField lf, TextField qtype, TextField ta) {
        int eId=0;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            eId = ((MyUserDetails) principal).getEid();
        }
        int qt =0;
        float licenseFee = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con  = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();

//            First Get the qtype from emp_quarters table
            String sql = "select qtype from emp_quarters where eid = "+eId+";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                qt = rs.getInt("qtype");
            }
            rs.close();
            qtype.setValue(String.valueOf(qt));
//            Let us get the hra
            if(qt != 0) {
                rate_hra.setValue("Not Applicable");
                String sql1 = "select license_fee from quarters where qtype = "+qt+";";
                rs = stmt.executeQuery(sql1);
                while (rs.next())
                    licenseFee = rs.getFloat("license_fee");
                lf.setValue(String.valueOf(licenseFee));
                rs.close();
            }else {
                String sql1 = "select rate from rates where rate_name = 'hra';";
                rs =stmt.executeQuery(sql1);
                while(rs.next())
                    rate_hra.setValue(String.valueOf(rs.getFloat("rate")));
                rs.close();
                lf.setValue("Not Applicable");
            }

 //         Get the travel allowance based on base pay
            String sql2 = "select ta from travel_allowance where (select base_sal from emp_salary where eid = "+eId+")>lower_lim or (select base_sal from emp_salary where eid = "+eId+")<=upper_lim;";
            rs = stmt.executeQuery(sql2);
            while (rs.next())
                ta.setValue(String.valueOf(rs.getFloat("ta")));
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void fillCommonRates(TextField rate_da, TextField tdsRate, TextField tdsSlab, TextField base_incr) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con  = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();

            String sql = "select rate from rates where rate_name = 'da' or rate_name = 'tds' or rate_name = 'base';";
            ResultSet rs = stmt.executeQuery(sql);
            float[] rates = new float [3];
            int i=0;
            while (rs.next()){
                rates[i++] = rs.getFloat("rate");
            }
            rs.close();
            con.close();
            rate_da.setValue(String.valueOf(rates[0]));
            tdsRate.setValue(String.valueOf(rates[1]));
            base_incr.setValue(String.valueOf(rates[2]));
            tdsSlab.setValue("1000000.0");
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }


}
