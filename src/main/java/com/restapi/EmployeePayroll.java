package com.restapi;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class EmployeePayroll {

    private String employeePayrollData;
    private int ConnectionCounter=0;

    private synchronized Connection getConnection() throws SQLException {
        String jdbcurl = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String usernam = "root";
        String password = "root123";
        Connection con=null;
        try {
            System.out.println("Connecting to database: " + jdbcurl);
            System.out.println("Processing Thread "+Thread.currentThread().getName()+"Connecting to database with id "+ConnectionCounter);
            con = DriverManager.getConnection(jdbcurl, usernam, password);
            System.out.println("Processing Thread "+Thread.currentThread().getName()+" id "+ConnectionCounter+ " Connection was sucessfull!!! " +con);
            System.out.println("Connection successfull: " + con);
            return con;
        }catch (Exception e){
            e.printStackTrace();
        }
        return con;
    }

    public List<EmployeePayrollData> readData() {
        List<EmployeePayrollData> employeePayrollDataArrayList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from employee_details; ");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                EmployeePayrollData employeePayrollData = new EmployeePayrollData(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getDate(3), resultSet.getDouble(4), resultSet.getString(5));
                employeePayrollDataArrayList.add(employeePayrollData);
            }
            System.out.println(employeePayrollDataArrayList.toString());
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollDataArrayList;
    }

    public long updateData(double salary, int id) throws SQLException {
        Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("Update employee_details set salary=? where id=? ; ");
            preparedStatement.setDouble(1, salary);
            preparedStatement.setInt(2, id);
            long resultSet = preparedStatement.executeUpdate();
            connection.commit();
            System.out.println(resultSet);
            return resultSet;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection.rollback();
        }
        return 0;
    }

    public List<EmployeePayrollData> employeeDetailsfromDate(String start) {
        List<EmployeePayrollData> employeePayrollDataArrayList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from employee_details where start>=? ");
            preparedStatement.setDate(1, Date.valueOf(start));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                EmployeePayrollData employeePayrollData = new EmployeePayrollData(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getDate(3), resultSet.getDouble(4), resultSet.getString(5));
                employeePayrollDataArrayList.add(employeePayrollData);
            }
            System.out.println(employeePayrollDataArrayList.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollDataArrayList;
    }

    public List<String> dataManipulation() {
        List<String> list = new ArrayList();
        try {
            Connection connection = this.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select gender,sum(salary), avg(salary),min(salary),max(salary),count(salary) from employee_payroll group by gender; ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int index = 1;
                System.out.println("Gender: " + resultSet.getString(1));
                System.out.println("Salary: " + resultSet.getString(2));
                for (int i = 0; i < 13; i++) {
                    if (index < 7) {
                        list.add(i, resultSet.getString(index));
                        index++;
                    }
                }
                System.out.println(list);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public void insertValuesintoTables(String name, String start, double salary, String gender, int payroll_id) throws SQLException {
        Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into employee_details(name,start,salary,gender) values(?,?,?,?); ");
            preparedStatement.setNString(1, name);
            preparedStatement.setDate(2, Date.valueOf(start));
            preparedStatement.setDouble(3, salary);
            preparedStatement.setNString(4, gender);
            int resultSet = preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 = connection.prepareStatement("insert into payroll_detail_table(payroll_id,basicpay,deduction,taxablepay,tax,netpay) values(?,?,?,?,?,?); ");
            preparedStatement1.setInt(1, payroll_id);
            preparedStatement1.setDouble(2, salary / 20);
            preparedStatement1.setDouble(3, salary / 10);
            preparedStatement1.setDouble(4, salary / 8);
            preparedStatement1.setDouble(5, salary / 60);
            preparedStatement1.setDouble(6, salary / 30);
            int resultSet1 = preparedStatement1.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection.rollback();
        } finally {
            connection.close();
        }
    }

    public int insertIntoPayrollDetails(int payroll_id, double basicpay, double deduction, double taxablepay, double tax, double netpay) throws SQLException {
        Connection connection = this.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into payroll_detial_table(payroll_id,basicpay,deduction,taxablepay,tax,netpay) values(?,?,?,?,?,?); ");

            preparedStatement.setInt(1, payroll_id);
            preparedStatement.setDouble(2, basicpay);
            preparedStatement.setDouble(3, deduction);
            preparedStatement.setDouble(4, taxablepay);
            preparedStatement.setDouble(5, tax);
            preparedStatement.setDouble(6, netpay);
            int resultSet = preparedStatement.executeUpdate();
            return resultSet;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public void deleteRecordFromEmployeePayroll(String name) throws SQLException {
        Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("delete from employee_details where name=?; ");
            preparedStatement.setString(1, name);
            int resultSet = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection.rollback();
        }
    }

    public void insertRecords(String name, String start, double salary, String gender) throws SQLException {
        Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into employee_details(name,start,salary,gender) values(?,?,?,?); ");
            preparedStatement.setNString(1, name);
            preparedStatement.setDate(2, Date.valueOf(start));
            preparedStatement.setDouble(3, salary);
            preparedStatement.setNString(4, gender);
            int resultSet = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection.rollback();
        } finally {
            connection.close();
        }
    }

    public void  insetUsingArrays(List<EmployeePayrollData> employeePayrollData) throws SQLException {
        Connection connection=this.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement=connection.prepareStatement("insert into employee_details(id,name,start,salary,gender) values(?,?,?,?,?) ;");
            for(Iterator<EmployeePayrollData> iterator=employeePayrollData.iterator();iterator.hasNext();){
                EmployeePayrollData employeePayrollData1=(EmployeePayrollData) iterator.next();
                preparedStatement.setInt(1,employeePayrollData1.getId());
                preparedStatement.setString(2,employeePayrollData1.getName());
                preparedStatement.setDate(3,employeePayrollData1.getDate());
                preparedStatement.setDouble(4,employeePayrollData1.getSalary());
                preparedStatement.setString(5,employeePayrollData1.getGender());
                preparedStatement.addBatch();
            }
            int[] updatecounts=preparedStatement.executeBatch();
            connection.commit();
        }catch (SQLException throwables){
            throwables.printStackTrace();
            connection.rollback();
        }
    }


    public void insertUsingThreads(List<EmployeePayrollData> employeePayrollData) throws SQLException {
        Map<Integer, Boolean> employee = new HashMap<>();
        employeePayrollData.forEach(employeePayrollData1 -> {
            Runnable task = () -> {
                employee.put(employeePayrollData.hashCode(),  false);
                System.out.println("Employee being added : " + Thread.currentThread().getName());
                try {
                    this.insertRecords(employeePayrollData1.name, String.valueOf(employeePayrollData1.start), employeePayrollData1.salary,employeePayrollData1.gender);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                employee.put(employeePayrollData.hashCode(), true);
                System.out.println("Employee added : " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollData1.name);
            thread.start();
        });
        while (employee.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("" + this.employeePayrollData);
    }

    public void setEmployeePayrollData(String employeePayrollData) {
        this.employeePayrollData = employeePayrollData;
    }
}

