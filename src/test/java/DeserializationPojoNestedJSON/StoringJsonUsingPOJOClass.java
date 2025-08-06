package DeserializationPojoNestedJSON;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import SampleAPI.ReusableMethods;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class StoringJsonUsingPOJOClass {
    @Test
    public void storeResponseAsJavaObject() {
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given().log().all()
                .formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "trust")
                .when().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token")
                .then().log().all().assertThat().statusCode(200).extract().response();
        JsonPath js = ReusableMethods.rawToJson(response);
        String accessToken = js.get("access_token");
        GetCourseDetails getCourseDetails = given().log().all()
                .queryParam("access_token", accessToken)
                .when().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
                .then().log().all().assertThat().statusCode(401).extract().response().as(GetCourseDetails.class);

        System.out.println("**********************Extracting json data from response using POJO class*****************************");
        System.out.println("Instructor : " + getCourseDetails.getInstructor());
        System.out.println("url : " + getCourseDetails.getUrl());
        System.out.println("Services : " + getCourseDetails.getServices());
        System.out.println("Expertise : " + getCourseDetails.getExpertise());
        //Courses class contains 3 list(webAutomation, api, mobile) of type <Course>, where Course class has two fields course title and price
        //override toString() in course class
        System.out.println("WebAutomation " + getCourseDetails.getCourses().getWebAutomation().toString());
        System.out.println("API " + getCourseDetails.getCourses().getApi().toString());
        System.out.println("Mobile " + getCourseDetails.getCourses().getMobile().toString());
        System.out.println("LinkedIn : " + getCourseDetails.getLinkedIn());

        List<Course> webCourses = getCourseDetails.getCourses().getWebAutomation();
        String webCourse = "Selenium Webdriver Java";
        for (int i = 0; i < webCourses.size(); i++) {
            if (webCourses.get(i).getCourseTitle().equals(webCourse)) {
                System.out.println(webCourses.get(i).getCourseTitle() + " " + webCourses.get(i).getPrice());
            }
        }

    }
}
