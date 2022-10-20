import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ProjectGitHub {
    // Declare request specification
    RequestSpecification requestSpec;

    String keyId="";
    int id;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                // Set content type
                .setContentType(ContentType.JSON)
                // Set base URL
                .setBaseUri("https://api.github.com")
                .addHeader("token", "value")
                // Build request specification
                .build();
    }

    @Test(priority=1)
    public void addSsh() {
        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAg....\"}";
        Response response = given().spec(requestSpec) // Use requestSpec
                .body(reqBody) // Send request body
                .when().post("/user/keys"); // Send POST request
        
        id= response.then().extract().path("[0].id");

        // Assertions
        response.then().body("code", equalTo(201));
    }

    @Test(priority=2)
    public void getSsh() {
        Response response = given().spec(requestSpec)
                .pathParam("keyId", id) 
                .when().get("/user/keys/{keyId}"); 

        // Print response
        Reporter.log(response.asPrettyString());
        
        // Assertions
        response.then().body("code", equalTo(200));
    }

    @Test(priority=3)
    public void deleteSsh() {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("keyId", id) // Add path parameter
                .when().delete("/user/keys/{keyId}"); 
        
     // Print response
        Reporter.log(response.asPrettyString());

        // Assertions
        response.then().body("code", equalTo(204));
    }

}
