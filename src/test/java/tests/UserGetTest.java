
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.test.java.lib.BaseTestCase;
import java.util.HashMap;
import java.util.Map;
import src.test.java.lib.Assertions;
import lib.ApiCoreRequests;

@Epic("Getting user's info cases")
@Feature("Get data")
public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("This test checks the getting user's info without login")
    @DisplayName("Test of getting info without login")
    @Test
    public void testGetUserDataNotAuth()
    {
        Response responseUserData = apiCoreRequests
                .makeGetRequestWithoutCookieAndHeader("https://playground.learnqa.ru/api/user/2");

        String [] unexpectedFields = {"email", "password", "lastName", "firstName"};

        Assertions.assertJsonHasNotFields(responseUserData, unexpectedFields);
        Assertions.assertJsonHasField(responseUserData, "username");
    }

    @Description("This test checks the getting user's info with login as the same user")
    @DisplayName("Test of getting info with login(same user)")
    @Test
    public void testGetUserDetailsAuthAsSameUser()
    {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        String [] expectedFields = {"email", "username", "lastName", "firstName"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);

    }

    @Description("This test checks the getting user's info with login as the another user")
    @DisplayName("Test of getting info with login(another user)")
    @Test
    public void testGetUserDetailsAuthAsAnotherUser()
    {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/91562", header, cookie);

        String [] unexpectedFields = {"email", "password", "lastName", "firstName"};
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotFields(responseUserData, unexpectedFields);
    }
}
