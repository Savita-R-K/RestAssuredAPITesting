package SpecBuilder;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import static SampleAPI.Payload.addPlace;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class ReqAndResSpecBuilder {

    @Test
    public void specBuilder(){

        RestAssured.useRelaxedHTTPSValidation();

//        RequestSpecification
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .setContentType(ContentType.JSON)
                .build();

        RequestSpecification request = given().spec(reqSpec).body(addPlace());

//        Response Specification
        ResponseSpecification resSpec=new ResponseSpecBuilder()
                .expectStatusCode(200).build();

        Response response=request.when().post("/maps/api/place/add/json")
                .then().spec(resSpec).extract().response();

        System.out.println(response.asString());
    }
}
