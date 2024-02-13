package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import src.test.java.lib.BaseTestCase;

import java.util.HashMap;
import java.util.Map;

@Epic("Edit user's info")
@Feature("Editing")
public class UserEditTest extends BaseTestCase {

    private static Map<String, String> userData;
    private static String userId;
    private static final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeAll
    public static void setUp()
    {

            //GENERATE USER
            userData = DataGenerator.getRegistrationData();

            JsonPath responseCreateAuth = apiCoreRequests
                    .makePostRequestAndGetJson("https://playground.learnqa.ru/api/user", userData);

            userId = responseCreateAuth.getString("id");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Owner("Makeeva")
    @Tag("Regress")
    @Description("This test checks the positive case of the editing user's profile")
    @DisplayName("Test positive edition")
    @Test
    public void testEditJustCreated()
    {
        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        String cookie = responseGetAuth.getCookie("auth_sid");
        String header = responseGetAuth.getHeader("x-csrf-token");

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie,
                        editData
                );
        Assertions.assertResponseCodeEquals(responseEditUser, 200);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Owner("Ivanov")
    @Issue("jiraLink.com")
    @Description("This test checks the negative case of the editing user's profile without authorization")
    @DisplayName("Test negative edition: no auth")
    @Test
    public void testEditWithoutAuthorization()
    {
        //EDIT
        String newName = "Changed name by non authorized user";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithoutTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId,
                        editData
                );
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");

        //LOGIN - we want to check that we didn't change the name for sure
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


       // GET
        String cookie = responseGetAuth.getCookie("auth_sid");
        String header = responseGetAuth.getHeader("x-csrf-token");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie);

        Assertions.assertJsonByNameNotEquals(responseUserData, "firstName", newName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Owner("Makeeva")
    @Description("This test checks the negative case of the editing user's profile as another user")
    @DisplayName("Test negative edition: another user")
    @Test
    public void testEditAnotherUser()
    {
        //LOGIN
        Map<String,String> authDataAnotherUser = new HashMap<>();
        authDataAnotherUser.put("email", "learnqa20240210233030@example.com");
        authDataAnotherUser.put("password", "123");

        Response responseGetAuthAnotherUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDataAnotherUser);

        //EDIT
        String newName = "Changed name by another user";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        String cookie = responseGetAuthAnotherUser.getCookie("auth_sid");
        String header = responseGetAuthAnotherUser.getHeader("x-csrf-token");

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie,
                        editData
                );
        Assertions.assertResponseCodeEquals(responseEditUser, 200);


        //LOGIN - we want to check that we didn't change the name for sure
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        // GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        responseGetAuth.getHeader("x-csrf-token"),
                        responseGetAuth.getCookie("auth_sid"));

        Assertions.assertJsonByNameNotEquals(responseUserData, "firstName", newName);
    }

    @Severity(SeverityLevel.MINOR)
    @Owner("Ivanov")
    @Description("This test checks the negative case of the editing user's profile because of incorrect email")
    @DisplayName("Test negative edition: incorrect email")
    @Test
    public void testEditIncorrectEmail()
    {
        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newEmail = "test.mail.ru";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        String cookie = responseGetAuth.getCookie("auth_sid");
        String header = responseGetAuth.getHeader("x-csrf-token");

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie,
                        editData
                );

        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }

    @Severity(SeverityLevel.MINOR)
    @Owner("Ivanov")
    @Link("confluenceLink.com")
    @Description("This test checks the negative case of the editing user's profile because of short name")
    @DisplayName("Test negative edition: short name")
    @Test
    public void testEditShortName()
    {
        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = DataGenerator.getRandomName(1);
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        String cookie = responseGetAuth.getCookie("auth_sid");
        String header = responseGetAuth.getHeader("x-csrf-token");

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie,
                        editData
                );
        Assertions.assertJsonByName(responseEditUser, "error", "Too short value for field firstName");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }
}
