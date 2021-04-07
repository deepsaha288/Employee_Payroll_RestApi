package com.restapi;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

class JSONServerEmployeeDataTest {


    @BeforeAll
    static void setup(){
        RestAssured.baseURI="http://localhost";
        RestAssured.port=4000;
    }

    public JSONServerEmployeeData[] getEmployeelist(){
        Response response= RestAssured.get("/employee");
        System.out.println("employee entries in json server: \n"+response.asString());
        JSONServerEmployeeData[] jsonServerEmployeeData=new Gson().fromJson(response.asString(),JSONServerEmployeeData[].class);
        return jsonServerEmployeeData;
    }

    public Response addEmployeeDataToJsonServer(JSONServerEmployeeData restAssureEmpData){
        String employee=new Gson().toJson(restAssureEmpData);
        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body(employee);
        return requestSpecification.post("/employee");
    }

    @Test
    public void givenEmployeeDateToGsonServer_shouldRetrieveServerData(){
        JSONServerEmployeeData[] restAssureEmployeeData=getEmployeelist();
        System.out.println(restAssureEmployeeData);
        Assertions.assertEquals(4,restAssureEmployeeData.length);
    }

    @Test
    public void addMultipleEmployee_shouldReturn_201ResponseCode(){
        JSONServerEmployeeData[] restAssureEmployeeData=getEmployeelist();
        JSONServerEmployeeData jsonServerEmployeeData1=new JSONServerEmployeeData(6,"Madhu",3000);
        Response response=addEmployeeDataToJsonServer(jsonServerEmployeeData1);
        int statusCode= response.statusCode();
        Assertions.assertEquals(201,statusCode);
        Assertions.assertEquals(6,restAssureEmployeeData.length);
    }

    @Test
    public void addNewsalary_ShouldRetun_200ResponseCode() throws SQLException {
        JSONServerEmployeeData[] restAssureEmployeeData=getEmployeelist();
        String empJson=new Gson().toJson(restAssureEmployeeData);
        Assertions.assertEquals(4,restAssureEmployeeData.length);
        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body("{\"name\":\"Suraj\",\"salary\":\"50000\"}");
        Response response=requestSpecification.put("/employee/update/2");

        int statusCode=response.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }
    @Test
    public void deleteEmployee_ShouldRetun_200ResponseCode() throws SQLException {
        JSONServerEmployeeData[] restAssureEmployeeData=getEmployeelist();
        String empJson=new Gson().toJson(restAssureEmployeeData);

        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body(empJson);
        Response response=requestSpecification.delete("/employee/delete/5");

        int statusCode=response.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

}

