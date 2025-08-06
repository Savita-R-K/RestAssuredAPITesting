package SampleAPI;

import io.restassured.path.json.JsonPath;

public class JsonParsing {
    public static void main(String[] args) {
        String jsonData = "{\n" +
                "\"dashboard\": {\n" +
                "\"purchaseAmount\": 910,\n" +
                "\"website\": \"rahulshettyacademy.com\"\n" +
                "},\n" +
                "\"courses\": [\n" +
                "{\n" +
                "\"title\": \"Selenium Python\",\n" +
                "\"price\": 50,\n" +
                "\"copies\": 6\n" +
                "},\n" +
                "{\n" +
                "\"title\": \"Cypress\",\n" +
                "\"price\": 40,\n" +
                "\"copies\": 4\n" +
                "},\n" +
                "{\n" +
                "\"title\": \"RPA\",\n" +
                "\"price\": 45,\n" +
                "\"copies\": 10\n" +
                "}\n" +
                "]\n" +
                "}\n";
        JsonPath js = new JsonPath(jsonData);
        System.out.println(js.getInt("courses.size()"));
        int count = js.getInt("courses.size()");
        for (int i = 0; i < count; i++) {
            System.out.println(js.getString("courses[" + i + "].title"));
            System.out.println(js.getString("courses[" + i + "].price"));
        }
    }
}
