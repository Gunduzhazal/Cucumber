package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

//to change the order of execution we use fix method order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCode {

    String baseURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";

    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzQzNDA2NjQsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTY3NDM4Mzg2NCwidXNlcklkIjoiNDg2MiJ9.ITDNCweKFQnXy8cL0jkXvW7SSXSwTAm4hxPsCQPNeKY";

    static String employee_id;


    @Test
    public void bgetOneEmployee() {

       // to prepare the request, we use request specification
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .queryParam("employee_id", employee_id);

        //to hit the end point/to make the request which will return response
        Response response = request.when().get("/getOneEmployee.php");

        //System.out.println(response.asString());

        //prettyPrint could be simply used instead of sout
        response.prettyPrint();

        //verifying the status code
        response.then().assertThat().statusCode(200);

        // using jsonpath method, we are extracting the value from response body
        String firstName = response.jsonPath().getString("employee.emp_firstname");
        System.out.println(firstName);

        // first way of assertion
        Assert.assertEquals(firstName, "hazal");

        // second way of assertion to verify the value in response body using hamcrest matchers
        response.then().assertThat().body("employee.emp_firstname", equalTo("hazal"));
    }

    @Test
    public void acreateEmployee() {
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"emp_firstname\": \"hazal\",\n" +
                        "    \"emp_lastname\": \"gunduz\",\n" +
                        "    \"emp_middle_name\": \"ms\",\n" +
                        "    \"emp_gender\": \"F\",\n" +
                        "    \"emp_birthday\": \"2011-01-10\",\n" +
                        "    \"emp_status\": \"confirmed\",\n" +
                        "    \"emp_job_title\": \"QA Engineer\"\n" +
                        "    }");
        Response response = request.when().post("/createEmployee.php");

        response.prettyPrint();

        response.then().assertThat().statusCode(201);

        // getting the employee id from response and use it as static one
        employee_id = response.jsonPath().getString("Employee.employee_id");
        System.out.println(employee_id);

        response.then().assertThat().body("Employee.emp_lastname", equalTo("gunduz"));
        response.then().assertThat().body("Employee.emp_middle_name", equalTo("ms"));

        // verify console header
        response.then().assertThat().header("Server", "Apache/2.4.39 (Win64) PHP/7.2.18");
    }

    @Test
    public void cupdateEmployee() {
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"employee_id\": \""+ employee_id +"\",\n" +
                        "\"emp_firstname\": \"hazel\",\n" +
                        "\"emp_lastname\": \"mary\",\n" +
                        "\"emp_middle_name\": \"ms\",\n" +
                        "\"emp_gender\": \"F\",\n" +
                        "\"emp_birthday\": \"2016-01-10\",\n" +
                        "\"emp_status\": \"confirmed\",\n" +
                        "\"emp_job_title\": \"QA\"\n" +
                        "}");

        Response response = request.when().put("/updateEmployee.php");
        response.prettyPrint();

        // verification
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("Message", equalTo("Employee record Updated"));
    }

    @Test
    public void dgetUpdatedEmployee() {
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .queryParam("employee_id", employee_id);

        Response response = request.when().get("/getOneEmployee.php");

        // System.out.println(response.asString());
        response.prettyPrint();

        //verifying the status code
        response.then().assertThat().statusCode(200);

        response.then().assertThat().body("employee.emp_job_title", equalTo("QA"));

    }
}
