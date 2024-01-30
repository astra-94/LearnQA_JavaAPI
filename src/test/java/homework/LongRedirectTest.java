package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {
    @Test
    public void redirectTest() {
        int responseCode = 0;
        int counter = 0;
        String URL = "https://playground.learnqa.ru/api/long_redirect";
        while (responseCode != 200) {

            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(URL)
                    .andReturn();

            URL = response.getHeader("Location");
            responseCode = response.getStatusCode();
            if (responseCode != 200) {
                counter++;
            }
        }
        System.out.println(counter);
    }
}
