package homework;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParsingJsonTest {
    @Test
    public void testParsingJson() {

        Response responseWithMessages = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();

        JsonPath jsonPath = responseWithMessages.jsonPath();
        List<String> messages = jsonPath.getList("messages.message");
        //List<String> timestamps = jsonPath.getList("messages.timestamp");

//I don't know if I need to compare timestamps to find what was in the middle,
// or I can rely on sorting in json

        System.out.println(messages.get(1));
    }
}
