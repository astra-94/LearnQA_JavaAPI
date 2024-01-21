package src.test.java;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
   // video 4
//    @Test
//    public void testRestAssured()
//    {
//        Map<String, String> params = new HashMap<>();
//        params.put("name", "John");
//        Response response = RestAssured
//                .given()
//                .queryParams(params)
//                .get("https://playground.learnqa.ru/api/hello")
//                .andReturn();
//        response.prettyPrint();
//    }
    //video5
//    @Test
//    public void testRestAssured() {
//        Map<String, String> params = new HashMap<>();
//        params.put("name", "John");
//        JsonPath response = RestAssured
//                .given()
//                .queryParams(params)
//                .get("https://playground.learnqa.ru/api/hello")
//                .jsonPath();
//
//        String name = response.get("answer"); // еще можно проверить без двойки
//        if (name == null) {
//            System.out.println("The key 'answer2' is absent");
//        } else {
//            System.out.println(name);
//        }
//    }

 //   video6
//    @Test
//    public void testRestAssured() {
//        Map<String, Object> body = new HashMap<>();
//        body.put("param1", "value1");
//        body.put("param2", "value2");
//
//        Response response = RestAssured
//                .given()
//                .body(body)
//                .post("https://playground.learnqa.ru/api/check_type")
//                .andReturn();
//
//        response.print();
//    }
    //video 7
// @Test
// public void testRestAssured() {
//
//     Response response = RestAssured
//             .given()
//             .redirects()
//             .follow(false)
//             .when()
//             .get("https://playground.learnqa.ru/api/get_303")
//             .andReturn();
//
//     int statusCode = response.getStatusCode();
//     System.out.println(statusCode);
// }
    //video 8
//    @Test
//    public void testRestAssured()
//    {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("myHeader1", "value1");
//        headers.put("myHeader2", "value2");
//
//        Response response = RestAssured
//                .given()
//                .redirects()
//                .follow(false)
//                .when()
//                .get("https://playground.learnqa.ru/api/get_303")
//                .andReturn();
//
//        response.prettyPrint();
//
//        String locationHeader = response.getHeader("Location");
//        System.out.println(locationHeader);
//    }

    //video 9

//    @Test
//    public void testRestAssured()
//    {
//        Map<String, String> data = new HashMap<>();
//        data.put("login", "secret_login");
//        data.put("password", "secret_pass");
//
//        Response response = RestAssured
//                .given()
//                .body(data)
//                .when()
//                .post("https://playground.learnqa.ru/api/get_auth_cookie")
//                .andReturn();
//
//        System.out.println("Pretty text:");
//        response.prettyPrint();
//
//        System.out.println("Headers:");
//        Headers responseHeaders = response.getHeaders();
//        System.out.println(responseHeaders);
//
//        System.out.println("Cookies:");
//        Map<String, String > responseCookies = response.getCookies();
//        System.out.println(responseCookies);
//        String responseCookie = response.getCookie("auth_cookie");
//        System.out.println(responseCookie);
    //video 9 use cookie

    @Test
    public void testRestAssured()
    {
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login2");
        data.put("password", "secret_pass2");

        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");
        Map<String, String> cookies = new HashMap<>();
        if (responseCookie != null)
        {cookies.put("auth_cookie", responseCookie);}


        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();

    }
}
