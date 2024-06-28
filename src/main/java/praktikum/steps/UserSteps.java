package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.User;

import static io.restassured.RestAssured.*;

public class UserSteps {

    private static final String USER_CREATE = "/api/auth/register";
    private static final String USER_LOGIN = "/api/auth/login";
    private static final String USER_EDIT = "/api/auth/user";
    private static final String USER_DELETE = "/api/auth/user";

    @Step("Шаг создания пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .body(user)
                .when()
                .post(USER_CREATE)
                .then();
    }

    @Step("Шаг логина пользователя в систему")
    public ValidatableResponse loginUser(User user) {
        return given()
                .body(user)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("Шаг редактирования данных авторизованного пользователя")
    public ValidatableResponse editAuthorizedUser(User user) {
        return given()
                .header("Authorization", user.getAccessToken())
                .body(user)
                .when()
                .patch(USER_EDIT)
                .then();
    }

    @Step("Шаг редактирования данных неавторизованного пользователя")
    public ValidatableResponse editNotAuthorizedUser(User user) {
        return given()
                .body(user)
                .when()
                .patch(USER_EDIT)
                .then();
    }

    @Step("Шаг удаления пользователя")
    public ValidatableResponse deleteUser(User user) {
        return given()
                .header("Authorization", user.getAccessToken())
                .when()
                .delete(USER_DELETE)
                .then();

    }
}
