package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetCookieTest {

    @Test
    public void testGetCookie()
    {
        Response responseCheckHomeWorkCookie = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String expectedValue = "hw_value";
        assertEquals(expectedValue, responseCheckHomeWorkCookie.getCookie("HomeWork"), "Cookie doesn't have expected value " + expectedValue);
    }
}
