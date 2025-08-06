package JiraBasicAuthCreateBugAndAddAttachments;

import SampleAPI.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JiraBugReporting {

    String bugId;
    String baseUrl = "https://savitaravindra57.atlassian.net";
    String jsonFilepath = System.getProperty("user.dir") + "/src/test/java/JiraBasicAuthCreateBugAndAddAttachments/createBug.json";
    String screenshotFilePath = System.getProperty("user.dir") + "/src/test/java/JiraBasicAuthCreateBugAndAddAttachments/Screenshot.png";
    String apiToken = "Basic c2F2aXRhcmF2aW5kcmE1N0BnbWFpbC5jb206QVRBVFQzeEZmR0YwYU9nUGNfNXRDNEQzTWNZQkROTzBGT1JMcll3aVV1WjhHLXRNMkcwM25LNHBUR1ZVQVM2NGF3TFgtbks4bjlMVjEzMzJncEJrdmFYcHp6SmFkOGFUcjlMNy0yeGtqY1JNZ245R3NRNkk4ajVLemYxNTRtNjZ2TFVSZmhPclJqa2dFZlJlaHdaT1d5aUtRR2RrUVd1akhrNjNOSEhkTWo4WDFVOUFZZzhPcTZRPTY4OTMxQ0Y0";

    @Test
    public void createBugInJira() {
        RestAssured.baseURI = baseUrl;
        File jsonFile = new File(jsonFilepath);
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", apiToken)
                .body(jsonFile)
                .when().post("rest/api/3/issue")
                .then().log().all().assertThat().statusCode(201).extract().response();
        JsonPath js = ReusableMethods.rawToJson(response);
        bugId = js.get("id");
    }

    @Test(dependsOnMethods = "createBugInJira")
    public void attachScreenshotToBug() {
        RestAssured.baseURI = baseUrl;
        File screenshot = new File(screenshotFilePath);
        given().log().all()
                .pathParam("key", bugId)
                .header("X-Atlassian-Token", "no-check")
                .header("Authorization", apiToken)
                .multiPart(screenshot)
                .when().post("rest/api/3/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200).extract().response();
    }

    @Test(dependsOnMethods = "createBugInJira")
    public void getBugInJira() {
        RestAssured.baseURI = baseUrl;
        given().log().all()
                .pathParam("key", bugId)
                .header("Authorization", apiToken)
                .when().get("rest/api/3/issue/{key}")
                .then().log().all().assertThat().statusCode(200).extract().response();

    }

}
