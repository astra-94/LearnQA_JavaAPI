package homework;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import java.util.HashMap;
import java.util.Map;

public class TokensTest {

    @Test
    public void testTokens() throws InterruptedException {
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = response.get("token");
        int time = response.get("seconds");

        Map<String, String > params = new HashMap<>();
        params.put("token", token);

        JsonPath responseNoTime = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String status = responseNoTime.get("status");
        Assert.assertEquals(status, "Job is NOT ready");

        Thread.sleep(time * 1000);

        JsonPath responseInTime = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        status = responseInTime.get("status");
        Assert.assertEquals(status, "Job is ready");

        String result = responseInTime.get("result");
        Assert.assertEquals(result, "42");

    }
}
