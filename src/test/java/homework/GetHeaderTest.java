package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetHeaderTest {
    @Test
    public void testGetHeader()
    {
        Response responseCheckHomeWorkHeader = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        String expectedValue = "Some secret value";
        assertEquals(expectedValue, responseCheckHomeWorkHeader.getHeader("x-secret-homework-header"), "Header doesn't have expected value " + expectedValue);
    }
}
