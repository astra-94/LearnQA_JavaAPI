package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import src.test.java.lib.BaseTestCase;

@Epic("Delete users")
@Feature("Deletion")
public class UserDeleteTest extends BaseTestCase {
    private static final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("This test checks the negative case of the deletion protected users(number 2)")
    @DisplayName("Test negative deletion: second user")
    @Test
    public void testDeleteTheSecondUser() {
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        int userId = 2;

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, responseGetAuth.getHeader("x-csrf-token"), responseGetAuth.getCookie("auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Description("This test checks the positive case of the deletion user")
    @DisplayName("Test positive deletion")
    @Test
    public void testDeleteCreatedUser() {
        //Create new user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        String userId = responseCreateUser.jsonPath().getString("id");

        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //Delete this user

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, responseGetAuth.getHeader("x-csrf-token"), responseGetAuth.getCookie("auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //Get user data
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, responseGetAuth.getHeader("x-csrf-token"), responseGetAuth.getCookie("auth_sid"));

        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }

    @Description("This test checks the negative case of the deletion another user")
    @DisplayName("Test negative deletion: another user")
    @Test
    public void testDeleteAnotherUser() {

        //Register a new user (we will delete them)
        Map<String,String> userDataDeleted = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user", userDataDeleted);

        int userIdDeleted = responseCreateAuth.jsonPath().getInt("id");
        String emailDeleted = userDataDeleted.get("email");
        String passwordDeleted = userDataDeleted.get("password");

        //Register a new user (who will try to delete them)

        Map<String,String> userDataMain = DataGenerator.getRegistrationData();

        Response responseCreateAuthMain = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userDataMain);

        String emailMain = userDataMain.get("email");
        String passwordMain = userDataMain.get("password");

        //LOGIN as user who will try to delete

        Map<String, String> authData = new HashMap<>();
        authData.put("email", emailMain);
        authData.put("password", passwordMain);

        Response responseGetAuthMain = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //Delete user

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userIdDeleted, responseGetAuthMain.getHeader("x-csrf-token"), responseGetAuthMain.getCookie("auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //Check that our "deleted" user exists. LOGIN

        Map<String,String> authDataDeleted = new HashMap<>();
        authDataDeleted.put("email", emailDeleted);
        authDataDeleted.put("password", passwordDeleted);

        Response responseGetAuthDeleted = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDataDeleted);

        //Get user data for "deleted" user

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userIdDeleted, responseGetAuthDeleted.getHeader("x-csrf-token"), responseGetAuthDeleted.getCookie("auth_sid"));

        String [] expectedFields = {"email", "id", "lastName", "firstName", "username"};
        Assertions.assertJsonHasFields(responseUserData,expectedFields);
        Assertions.assertResponseCodeEquals(responseUserData, 200);
    }
}
