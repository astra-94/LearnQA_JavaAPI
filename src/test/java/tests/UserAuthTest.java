package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import src.test.java.lib.BaseTestCase;

import java.util.HashMap;
import java.util.Map;


@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {

        String cookie;
        String header;
        int userIdOnAuth;
        private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

        @BeforeEach
        public void loginUser(){
            Map<String, String> authData = new HashMap<>();
            authData.put("email", "vinkotov@example.com");
            authData.put("password", "1234");

            Response responseGetAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

            this.cookie = this.getCookie(responseGetAuth, "auth_sid");
            this.header = this.getHeader(responseGetAuth, "x-csrf-token");
            this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
        }

        @Tag("Regress")
        @Severity(SeverityLevel.BLOCKER)
        @Owner("Makeeva")
        @Test
        @Description("This test successfully authorize user by email and password")
        @DisplayName("Test positive auth user")
        public void testAuthUser()
        {

            Response responseCheckAuth = apiCoreRequests
                    .makeGetRequest(
                            "https://playground.learnqa.ru/api/user/auth",
                            this.header,
                            this.cookie
                    );

            Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
        }

        @Issue("jiraLink.com")
        @Severity(SeverityLevel.NORMAL)
        @Owner("Ivanov")
        @Description("This test checks authorization without sending header or cookie")
        @DisplayName("Test negative auth user")
        @ParameterizedTest
        @ValueSource(strings = {"cookie", "headers"})
        public void testNegativeAuthUser(String condition)
        {

            if (condition.equals("cookie"))
            {
                Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.cookie
                );
            } else if (condition.equals("headers"))
            {
                Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header
                );
            } else {
                throw new IllegalArgumentException("Condition value is unknown" + condition);
            }
        }
}
