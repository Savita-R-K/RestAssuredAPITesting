package ecommerce;

import SampleAPI.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EcommerceApi {

    LoginResponse resLogin;
    RequestSpecification reqBaseAuth;
    AddProductResponse resAddProduct;


    @Test
    public void login(){

        RestAssured.useRelaxedHTTPSValidation();
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUserEmail("savitaravindra57@gmail.com");
        loginRequest.setUserPassword("Pass@123");

        RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        RequestSpecification reqLogin = given().spec(req).body(loginRequest);

        resLogin = reqLogin.when().post("api/ecom/auth/login").then().log().all().extract().response().as(LoginResponse.class);

    }

    @Test
    public void addProduct(){
        reqBaseAuth=new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",resLogin.getToken()).build();

        File productPic=new File(System.getProperty("user.dir")+"/src/test/java/ecommerce/laptop.jpg");
        resAddProduct=given().spec(reqBaseAuth)
                .param("productName","Laptop")
                .param("productAddedBy",resLogin.getUserId())
                .param("productCategory","electronics")
                .param("productSubCategory","laptops")
                .param("productPrice","80000")
                .param("productDescription","DELL 123")
                .param("productFor","men")
                .multiPart("productImage",productPic)
                .when()
                .post("api/ecom/product/add-product")
                .then().log().all()
                .extract()
                .response()
                .as(AddProductResponse.class);
    }

    @Test(dependsOnMethods = {"login","addProduct"})
    public void createOrder() {
//        OrderDetail detail1 = new OrderDetail();
//        detail1.setCountry("India");
//        detail1.setProductOrderedId("6878eda36eb3777530a43224");
//
//        // Create second order detail
//        OrderDetail detail2 = new OrderDetail();
//        detail2.setCountry("India");
//        detail2.setProductOrderedId("685bf2b6129e250258b680cb");
//
//        // Add to list
//        List<OrderDetail> orderDetailsList = new ArrayList<>();
//        orderDetailsList.add(detail1);
//        orderDetailsList.add(detail2);
//
//        // Set in Orders object
//        Orders orders = new Orders();
//        orders.setOrderDetails(orderDetailsList);

        String resCreateOrder = given().spec(reqBaseAuth).contentType(ContentType.JSON)
//                .body(orders)
                .body("{\n" +
                        "    \"orders\":[\n" +
                        "        {\n" +
                        "            \"country\":\"India\",\n" +
                        "            \"productOrderedId\":\""+resAddProduct.getProductId()+"\"\n" +
                        "        }\n" +
                        "]\n"+
                        "}")
                .when()
                .post("api/ecom/order/create-order")
                .then().log().all()
                .extract()
                .response().asString();

        JsonPath js= ReusableMethods.rawToJson(resCreateOrder);
        String productId=js.getString("productOrderId.get(0)");
        String deleteRes = given().spec(reqBaseAuth).pathParam("productId", productId)
                .when().delete("api/ecom/product/delete-product/{productId}")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js1=new JsonPath(deleteRes);
        Assert.assertEquals(js1.getString("message"),"Product Deleted Successfully");


    }


}
