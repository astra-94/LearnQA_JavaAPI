package src.test.java;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class ParsingJsonTest {
    @Test
    public void testParsingJson() {
//        Map<String, String> data = new HashMap<>();
//        data.put("login", "secret_login2");
//        data.put("password", "secret_pass2");

        Response responseForGet = RestAssured
                .given()
//                .body(data)
                .when()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();

//        String responseCookie = responseForGet.getCookie("auth_cookie");
//        Map<String, String> cookies = new HashMap<>();
//        if (responseCookie != null)
//        {cookies.put("auth_cookie", responseCookie);}
        responseForGet.print();

    }

}
