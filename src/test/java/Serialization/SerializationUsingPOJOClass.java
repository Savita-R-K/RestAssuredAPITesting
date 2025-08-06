package Serialization;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializationUsingPOJOClass {
    @Test
    public void serializationJsonData() {
        AddPlace addPlace = new AddPlace();

        Location location = new Location();
        location.setLatitude(-38.383494);
        location.setLongitude(33.427362);
        addPlace.setLocation(location);

        addPlace.setAccuracy(50);

        addPlace.setName("Frontline house");

        addPlace.setPhoneNumber("(+91) 983 893 3937");

        addPlace.setAddress("29, side layout, cohen 09");

        List<String> types = new ArrayList<>();
        types.add("shoe park");
        types.add("shop");
        addPlace.setTypes(types);

        addPlace.setWebsite("http://google.com");

        addPlace.setLanguage("French-IN");

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        RestAssured.useRelaxedHTTPSValidation();
        given().log().all()
                .queryParam("key", "qaclick123").body(addPlace)
                .when().post("/maps/api/place/add/json/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200);

    }

}
