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
public class UserCreateNegativeParameterizedTests extends AbstractTest{
    private User user;
    private String email;
    private String password;
    private String name;

    public UserCreateNegativeParameterizedTests(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object [][] testData() {
        return new Object[][] {
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), "", RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), RandomStringUtils.randomAlphabetic(10), ""},
                {"", RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), "", ""},
                {"", RandomStringUtils.randomAlphabetic(10), ""},
                {"", "", RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), null, RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), RandomStringUtils.randomAlphabetic(10), null},
                {null, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), null, null},
                {null, RandomStringUtils.randomAlphabetic(10), null},
                {null, null, RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@" +
                        RandomStringUtils.randomAlphabetic(5).toLowerCase() + "." +
                        RandomStringUtils.randomAlphabetic(2).toLowerCase(), "", null},
                {null, RandomStringUtils.randomAlphabetic(10), ""},
                {"", null, RandomStringUtils.randomAlphabetic(10)},
                {"", "", ""},
                {null, null, null}
        };
    }

    @Test
    @DisplayName("Проверяем статус-код и тело ответа создания пользователя с недостающим обязательным полем")
    public void shouldReturnStatusCode403AndMessageRequiredFields() {
        UserSteps userSteps = new UserSteps();
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("Email, password and name are required fields"));
    }
}
