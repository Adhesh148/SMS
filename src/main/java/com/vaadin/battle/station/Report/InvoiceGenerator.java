package com.vaadin.battle.station.Report;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.lowagie.text.Font;

import java.sql.*;
import java.time.LocalDate;

public class InvoiceGenerator {
    String url = "jdbc:mysql://localhost:3306/dbmsendsem";
    String user = "dbmsendsem";
    String pwd = "Password_123";

    public static final String DEST = "/home/adheshreghu/Documents/invoice.pdf";
    public void createPDF(int eid){
        float baseSal = 0;
        float da =0;
        float hra =0;
        float arrear =0;
        float tds =0;
        float ta =0 ;
        float deductions =0;
        float lf =0;
        String pDate ="";
        int yr = 0;
        String mnt ="";
        String ename = "";

        // get the salary info from the backend
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql = "select *,year(pay_Date) as yr,monthname(pay_Date) as mnt from salary where eid = "+eid+" order by pay_date desc limit 0,1;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                baseSal = rs.getFloat("base_sal");
                pDate = rs.getString("pay_date");
                yr = rs.getInt("yr");
                mnt = rs.getString("mnt");
                da = rs.getFloat("da");
                hra = rs.getFloat("hra");
                arrear = rs.getFloat("arrear");
                ta = rs.getFloat("ta");
                tds = rs.getFloat("tds");
                lf = rs.getFloat("license_fee");
                deductions = rs.getFloat("deductions");
            }
            rs.close();
            String sql1 = "select ename from employees where eid = "+eid+";";
            rs = stmt.executeQuery(sql1);
            while (rs.next())
                ename = rs.getString("ename");
            rs.close();
            con.close();
        }catch (Exception e){
            e.getLocalizedMessage();
        }

        // Create the PDF
        try {
            PdfDocument pdf = new PdfDocument(new PdfWriter(DEST));

            Document document = new Document(pdf);

            Font regular = new Font(Font.HELVETICA, 12);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

            //heading
            Paragraph header = new Paragraph();
            String title = "Invoice Report";
            header.add("SMS\n");
            header.add(title);
            header.setFont(bold);
            header.setFontSize(20);
            header.setTextAlignment(TextAlignment.CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));

            // Employee Information
            Paragraph employeeInfo = new Paragraph();
            employeeInfo.add("Eid: "+eid);
            employeeInfo.add("\nEname: "+ename);
            employeeInfo.setTextAlignment(TextAlignment.LEFT);
            document.add(employeeInfo);


            //InvoiceInfo
            Paragraph invoiceInfo = new Paragraph();
            invoiceInfo.add("Date: "+ LocalDate.now());
            invoiceInfo.add("\nInvoice No: "+"001");
            invoiceInfo.setTextAlignment(TextAlignment.RIGHT);
            document.add(invoiceInfo);
            document.add(new Paragraph("\n"));


            //The line before the table
            String msgString = "The Account Summary for "+mnt+" "+yr+" is:";
            Paragraph message = new Paragraph();
            message.add(msgString);
            document.add(message);
            document.add(new Paragraph("\n"));

            float [] pointColumnWidths = {150F, 150F, 150F,150F,150F,150F,150F,150F};
            Table table = new Table(pointColumnWidths);
            // Adding cells to the table
            table.addCell(new Cell().add("BASE SAL"));
            table.addCell(new Cell().add("DA"));
            table.addCell(new Cell().add("HRA"));
            table.addCell(new Cell().add("TA"));
            table.addCell(new Cell().add("ARREARS"));
            table.addCell(new Cell().add("LICENSE FEE"));
            table.addCell(new Cell().add("TDS"));
            table.addCell(new Cell().add("DEDUCTIONS"));
            //entries
            table.addCell(new Cell().add(String.valueOf(baseSal)));
            table.addCell(new Cell().add(String.valueOf(da)));
            table.addCell(new Cell().add(String.valueOf(hra)));
            table.addCell(new Cell().add(String.valueOf(ta)));
            table.addCell(new Cell().add(String.valueOf(arrear)));
            table.addCell(new Cell().add(String.valueOf(lf)));
            table.addCell(new Cell().add(String.valueOf(tds)));
            table.addCell(new Cell().add(String.valueOf(deductions)));

            document.add(table);
            document.add(new Paragraph("\n"));

            //Total Salary
            float net = baseSal + da + hra + ta + arrear - lf - tds - deductions;
            Paragraph total = new Paragraph();
            total.add("Net Salary: "+net);
            total.setTextAlignment(TextAlignment.LEFT);
            document.add(total);

            // Add the payment Date
            Paragraph paymentDate = new Paragraph();
            paymentDate.add("Payment Date: "+pDate);
            paymentDate.setTextAlignment(TextAlignment.LEFT);
            document.add(paymentDate);

            document.add(new Paragraph("\n\n\n\n"));

            //Thank you note
            Paragraph thank = new Paragraph();
            thank.add("Thank You.");
            thank.setTextAlignment(TextAlignment.CENTER);
            document.add(thank);

            document.close();


//
//
        }catch (Exception e){
             e.getLocalizedMessage();
        }


    }
}
