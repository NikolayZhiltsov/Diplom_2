package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.User;
import praktikum.steps.UserSteps;

public class UserCreateTests extends AbstractTest {
    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                RandomStringUtils.randomAlphabetic(2).toLowerCase());
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
    }

    @Test
    @DisplayName("Проверяем статус-код и тело ответа успешного создания пользователя")
    public void shouldReturnStatusCode200AndCorrectBody() {
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("success", Matchers.is(true),
                        "user.email", Matchers.is(user.getEmail()),
                        "user.name", Matchers.is(user.getName()),
                        "accessToken", Matchers.notNullValue(),
                        "refreshToken", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Проверяем статус-код и тело ответа создания уже существующего пользователя")
    public void shouldReturnStatusCode403AndMessageUserAlreadyExists() {
        userSteps
                .createUser(user);
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("User already exists"));
    }

    @After
    public void tearDown() {
        String accessToken = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        if (accessToken != null) {
            user.setAccessToken(accessToken);
            userSteps.deleteUser(user);
        }
    }
}
