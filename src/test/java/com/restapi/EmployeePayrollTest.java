package com.restapi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollTest {

    EmployeePayroll employeePayroll;
    @Before
    public void setup(){
        employeePayroll=new EmployeePayroll();
    }

    @Test
    public void given_select_statement_should_return_count(){
        List<EmployeePayrollData> employeePayrollDataList=employeePayroll.readData();
        Assert.assertEquals(3,employeePayrollDataList.size());
    }

    @Test
    public void update_table_should_return_true() throws SQLException {
        double salary=600000;
        int id=3;
        long result=employeePayroll.updateData(salary,id);
        Assert.assertEquals(1,result);
    }

    @Test
    public void should_reurn_employee_datails_for_a_given_date_range(){
        String date = "2019-01-01";
        List<EmployeePayrollData>employeePayrollDataList=employeePayroll.employeeDetailsfromDate(date);
        Assert.assertEquals(3,employeePayrollDataList.size());
    }

    @Test
    public void return_sum_avg_min_max_count(){
        List<String> list=employeePayroll.dataManipulation();
        Assert.assertEquals(4,list.size());
    }

    @Test
    public void insert_new_employee_in_employee_table() throws SQLException {
        String name="Peter";
        String date="2020-03-07";
        double salary=500000;
        String gender="M";
        int payroll_id=4;
        employeePayroll.insertValuesintoTables(name,date,salary,gender,payroll_id);
        List<EmployeePayrollData> employeePayrollDataList=employeePayroll.readData();
        Assert.assertEquals(4,employeePayrollDataList.size());
    }

    @Test
    public void insert_in_payroll_details() throws SQLException {
        int payroll_id=1;
        double basicpay=6000;
        double deduction=20000;
        double taxablepay=60000;
        double tax=80000;
        double netpay=3000;
        int result=employeePayroll.insertIntoPayrollDetails(payroll_id,basicpay,deduction,taxablepay,tax,netpay);
        Assert.assertEquals(1,result);
    }

    @Test
    public void adding_new_employee_details_in_employee_table() throws SQLException {
        String name="Manish";
        String date="2019-08-09";
        double salary=800000;
        String gender="M";
        int payroll_id=5;

        employeePayroll.insertValuesintoTables(name,date,salary,gender,payroll_id);
        List<EmployeePayrollData> employeePayrollDataList=employeePayroll.readData();
        Assert.assertEquals(6,employeePayrollDataList.size());
    }

    @Test
    public void deleting_employee_from_employee_table() throws  SQLException{
        String name="madhu";

        employeePayroll.deleteRecordFromEmployeePayroll(name);
        List<EmployeePayrollData> employeePayrollDataList=employeePayroll.readData();
        Assert.assertEquals(5,employeePayrollDataList.size());
    }

    @Test
    public void insert_multiple_values_into_a_table_at_a_single_time() throws SQLException {
        List<EmployeePayrollData> list=new ArrayList<>();

        list.add(new EmployeePayrollData(6,"Sonu", Date.valueOf("2019-05-19"),900000,"M"));
        list.add(new EmployeePayrollData(7,"yoona",Date.valueOf("2019-01-21"),800000,"F"));

        Instant start=Instant.now();
        employeePayroll.insetUsingArrays(list);
        Instant end=Instant.now();
        System.out.println("Duration of non thread process is: "+ Duration.between(start,end));

        List<EmployeePayrollData> employeePayrollDataList=employeePayroll.readData();
        Assert.assertEquals(7,employeePayrollDataList.size());
    }

    @Test
    public void insert_multiple_records_using_threads() throws SQLException {
        List<EmployeePayrollData> list=new ArrayList<>();

        list.add(new EmployeePayrollData(8,"Anup", Date.valueOf("2017-03-09"),900000,"M"));
        list.add(new EmployeePayrollData(9,"Deepika",Date.valueOf("2018-06-19"),919918,"F"));

        Instant start=Instant.now();
        employeePayroll.insertUsingThreads(list);
        Instant end=Instant.now();
        System.out.println("Duration of non thread process is: "+ Duration.between(start,end));

        List<EmployeePayrollData> employeePayrollDataList=employeePayroll.readData();
        Assert.assertEquals(9,employeePayrollDataList.size());
    }

    @Test
    public  void insert_in_synchronised_way_in_table() throws SQLException{
        List<EmployeePayrollData> list=new ArrayList<>();

        list.add(new EmployeePayrollData(8,"Vivek", Date.valueOf("2018-05-10"),899980,"M"));
        list.add(new EmployeePayrollData(9,"deepika",Date.valueOf("2020-07-09"),889918,"F"));

        Instant start=Instant.now();
        employeePayroll.insertUsingThreads(list);
        Instant end=Instant.now();
        System.out.println("Duration of non thread process is: "+ Duration.between(start,end));

        List<EmployeePayrollData> employeePayrollDataList=employeePayroll.readData();
        Assert.assertEquals(9,employeePayrollDataList.size());
    }

}
