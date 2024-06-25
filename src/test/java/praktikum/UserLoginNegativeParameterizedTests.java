package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.model.User;
import praktikum.steps.UserSteps;

@RunWith(Parameterized.class)
public class UserLoginNegativeParameterizedTests extends AbstractTest {
    private User user;
    private String email;
    private String password;

    public UserLoginNegativeParameterizedTests(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][]{
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), ""},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), null},
                {"", RandomStringUtils.randomAlphabetic(10)},
                {null, RandomStringUtils.randomAlphabetic(10)},
                {"", ""},
                {null, null}
        };
    }

    @Test
    @DisplayName("Проверяем стстус-код и тело ответа входа в систему с некорректными данными")
    public void shouldReturnStatusCode401AndMessageDataIncorrect() {
        UserSteps userSteps = new UserSteps();
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("email or password are incorrect"));
    }
}
