package Oauth;

import SampleAPI.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class OauthGetCourseDetails {
    String accessToken;

    @Test
    public void ApplicationServerGetToken() {
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given().log().all()
                .formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "trust")
                .when().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token")
                .then().log().all().assertThat().statusCode(200).extract().response();
        JsonPath js = ReusableMethods.rawToJson(response);
        accessToken = js.get("access_token");
        System.out.println(accessToken);
    }

    @Test(dependsOnMethods = "ApplicationServerGetToken")
    public void getCourseDetails() {
        given().log().all()
                .queryParam("access_token", accessToken)
                .when().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
                .then().log().all().assertThat().statusCode(401);
    }
}
