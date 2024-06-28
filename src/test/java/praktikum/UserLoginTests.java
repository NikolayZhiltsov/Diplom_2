package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.User;
import praktikum.steps.UserSteps;

public class UserLoginTests extends AbstractTest {
    private UserSteps userSteps = new UserSteps();
    private User user;
    private String email;
    private String password;
    private String name;

    @Before
    public void setUp() {
        user = new User();
        email = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                RandomStringUtils.randomAlphabetic(2).toLowerCase();
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        userSteps.createUser(user);
        user.setName(null);
    }

    @Test
    @DisplayName("Проверяем статус-код и тело ответа успешного входа в систему")
    public void shouldReturnStatusCode200AndCorrectBody() {
        userSteps
                .loginUser(user)
                .statusCode(200)
                .body("success", Matchers.is(true),
                        "accessToken", Matchers.notNullValue(),
                        "refreshToken", Matchers.notNullValue(),
                        "user.email", Matchers.is(email),
                        "user.name", Matchers.is(name));
    }

    @Test
    @DisplayName("Проверяем статус-код и тело ответа входа в систему с некорректным email")
    public void shouldReturnStatusCode401AndMessageEmailIncorrect() {
        user.setEmail(RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                RandomStringUtils.randomAlphabetic(2).toLowerCase());
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверяем стстус-код и тело ответа входа в систему с некорректным паролем")
    public void shouldReturnStatusCode401AndMessagePasswordIncorrect() {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        user.setEmail(email);
        user.setPassword(password);
        String accessToken = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        if (accessToken != null) {
            user.setAccessToken(accessToken);
            userSteps.deleteUser(user);
        }
    }
}
