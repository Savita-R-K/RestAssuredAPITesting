package SampleAPI;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReusableMethods {
    public static JsonPath rawToJson(String response) {
        return new JsonPath(response);
    }

    public static JsonPath rawToJson(Response response) {
        return new JsonPath(response.asString());
    }
}
