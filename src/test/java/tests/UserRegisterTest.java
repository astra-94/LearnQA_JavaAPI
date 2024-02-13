package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import src.test.java.lib.BaseTestCase;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Issue("jiralink.com")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Ivanov")
    @Description("This test checks the registration with already used email")
    @DisplayName("Test negative registration: existing email")
    @Test
    public void testCreateUserWithExistingEmail()
    {
        String email = "vinkotov@example.com";

        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Owner("Makeeva")
    @Tag("Regress")
    @Description("This test checks the positive case of the registration")
    @DisplayName("Test positive registration")
    @Test
    public void testCreateUserSuccessfully()
    {
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);
        System.out.println(responseCreateAuth.asString());
        System.out.println(userData);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
    }

    @Severity(SeverityLevel.MINOR)
    @Owner("Ivanov")
    @Description("This test checks the registration with an incorrect email")
    @DisplayName("Test negative registration: incorrect email")
    @Test
    public void testCreateUserWithIncorrectEmail()
    {
        Map<String,String> userData = DataGenerator.getRegistrationDataWithIncorrectEmail();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Severity(SeverityLevel.MINOR)
    @Owner("Ivanov")
    @Link("ConfluenceLink.com")
    @Description("This test checks the registration with a short name")
    @DisplayName("Test negative registration: short name")
    @Test
    public void testCreateUserWithShortName()
    {
        Map<String,String> userData =  DataGenerator.getRegistrationDataWithRandomName(1);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Severity(SeverityLevel.MINOR)
    @Owner("Ivanov")
    @Link("ConfluenceLink.com")
    @Description("This test checks the registration with a long name")
    @DisplayName("Test positive registration: long name")
    @Test
    public void testCreateUserWithLongName()
    {
        Map<String,String> userData =  DataGenerator.getRegistrationDataWithRandomName(250);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Severity(SeverityLevel.MINOR)
    @Owner("Ivanov")
    @Link("ConfluenceLink.com")
    @Tag("Regress")
    @Description("This test checks the registration with 1 empty parameter(5 cases)")
    @DisplayName("Test negative registration: empty parameter")
    @ParameterizedTest
    @ValueSource(strings = {"firstName", "lastName", "password", "email", "username"})
    public void testCreateUserWithEmptyField(String emptyField)
    {
        Map<String,String> userData = new HashMap<>();
        userData.put(emptyField, "");
        userData =  DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '" + emptyField+ "' field is too short");
    }
}
