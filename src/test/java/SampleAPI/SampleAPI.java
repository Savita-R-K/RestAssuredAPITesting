package SampleAPI;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static SampleAPI.Payload.addPlace;

public class SampleAPI {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        RestAssured.useRelaxedHTTPSValidation();

        //POST
        //validating the response
        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(addPlace())
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .assertThat()
//                .log()
//                .all()
                .statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)")
                .extract()
                .response()
                .asString();

        System.out.println(response);

        //get particular attribute from string response
        JsonPath js = new JsonPath(response);
        String placeId = js.getString("place_id");
        System.out.println(placeId);


        //PUT request using place id
        //update the address of particular place id
        given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\"70 Summer walk, USA\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n")
                .when()
                .put("/maps/api/place/update/json")
                .then()
                .assertThat()
                .statusCode(200)
                .body("msg", equalTo("Address successfully updated"));


        //GET request using place id
        String getResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js1 = new JsonPath(getResponse);
        String updatedAddress = js1.getString("address");
        Assert.assertEquals(updatedAddress, "70 Summer walk, USA");
    }
}
