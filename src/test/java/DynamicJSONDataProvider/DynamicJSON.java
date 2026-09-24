package DynamicJSONDataProvider;

import SampleAPI.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJSON {

    public String addBookData(String isbn, String aisle) {
        return "{\n" +
                "  \"name\":\"Learn Appium Automation with Java\",\n" +
                "  \"isbn\":\"" + isbn + "\",\n" +
                "  \"aisle\":\"" + aisle + "\",\n" +
                "  \"author\":\"John foe\"\n" +
                "}";

    }

    @Test(dataProvider = "BooksData")
    public void addBookWithDynamicData(String isbn, String aisle) {
        RestAssured.baseURI = "http://216.10.245.166";
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .body(addBookData(isbn, aisle))
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200).extract().response();
        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.getString("ID");
        System.out.println(id);
        //deleting books
        given().body("{\n" +
                        "\"ID\":\""+ isbn+aisle +"\"\n" +
                        "}")
                .when().post("/Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200).extract().response();
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData() {
        return new Object[][]{{"adhk", "2345"}, {"yulk", "5729"}, {"adsf", "3728"}};
    }

}
