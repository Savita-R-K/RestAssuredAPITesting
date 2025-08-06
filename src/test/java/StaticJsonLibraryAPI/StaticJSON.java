package StaticJsonLibraryAPI;

import SampleAPI.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class StaticJSON {

    @Test
    public void addBook() {
        RestAssured.baseURI = "http://216.10.245.166";
        File jsonFile = new File(System.getProperty("user.dir") + "src/test/java/StaticJsonLibraryAPI/addBook.json");
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonFile)
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200).extract().response();
        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.getString("ID");
        System.out.println(id);
    }

    @Test(dependsOnMethods = "addBook")
    public void getBookById() {
        RestAssured.baseURI = "http://216.10.245.166";
        given().log().all().queryParam("ID", "bcde6303")
                .when().get("/Library/GetBook.php")
                .then().assertThat().statusCode(200).log().all();
    }

    @Test(dependsOnMethods = "addBook")
    public void getBookByName() {
        RestAssured.baseURI = "http://216.10.245.166";
        given().queryParam("AuthorName", "SRK")
                .when().get("/Library/GetBook.php")
                .then().log().all().assertThat().statusCode(200).extract().response();
    }

    @Test
    public void deleteBook() {
        RestAssured.baseURI = "http://216.10.245.166";
        given().body("{\n" +
                        "\"ID\":\"bcde6303\"\n" +
                        "}")
                .when().post("/Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200).extract().response();
    }


}
