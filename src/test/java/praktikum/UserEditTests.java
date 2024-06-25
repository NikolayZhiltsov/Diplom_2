package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.User;
import praktikum.steps.UserSteps;

public class UserEditTests extends AbstractTest {
    private UserSteps userSteps = new UserSteps();
    private User user;
    private String email;
    private String password;
    private String name;
    private String accessToken;

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
        accessToken = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        user.setAccessToken(accessToken);
    }

    @Test
    @DisplayName("Проверяем изменение пользовательских данных без авторизации")
    public void shouldReturnStatusCode401AndYouShouldBeAuthorized() {
        user.setEmail(RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                RandomStringUtils.randomAlphabetic(2).toLowerCase());
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps
                .editNotAuthorizedUser(user)
                .statusCode(401)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("You should be authorised"));
    }

    @Test
    @DisplayName("Проверяем изменение почты авторизованного пользователя")
    public void shouldReturnStatusCode200AndNewEmail() {
        email = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                RandomStringUtils.randomAlphabetic(2).toLowerCase();
        user.setEmail(email);
        userSteps
                .editAuthorizedUser(user)
                .statusCode(200)
                .body("success", Matchers.is(true),
                        "user.email", Matchers.is(email),
                        "user.name", Matchers.is(name));
    }

    @Test
    @DisplayName("Проверяем изменение имени авторизованного пользователя")
    public void shouldReturnStatusCode200AndNewName() {
        name = RandomStringUtils.randomAlphabetic(10);
        user.setName(name);
        userSteps
                .editAuthorizedUser(user)
                .statusCode(200)
                .body("success", Matchers.is(true),
                        "user.email", Matchers.is(email),
                        "user.name", Matchers.is(name));
    }

    @Test
    @DisplayName("Проверяем изменение пароля авторизованного пользователя")
    public void shouldReturnStatusCode200AndNotReturnNewPassword() {
        password = RandomStringUtils.randomAlphabetic(10);
        user.setPassword(password);
        userSteps
                .editAuthorizedUser(user)
                .statusCode(200)
                .body("success", Matchers.is(true),
                        "user.email", Matchers.is(email),
                        "user.name", Matchers.is(name));
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
