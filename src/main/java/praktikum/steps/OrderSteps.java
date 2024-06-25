package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.Order;

import static io.restassured.RestAssured.*;

public class OrderSteps {
    private static final String INGREDIENTS_GET = "/api/ingredients";
    private static final String ORDER_CREATE = "/api/orders";
    private static final String ORDERS_GET = "/api/orders";

    @Step("Шаг получения списка ингредиентов")
    public ValidatableResponse getIngredients(Order order) {
        return given()
                .when()
                .get(INGREDIENTS_GET)
                .then();
    }

    @Step("Шаг создания заказа авторизованным пользователем")
    public ValidatableResponse createOrderAuthorizedUser(Order order) {
        return given()
                .header("Authorization", order.getAccessToken())
                .body(order)
                .post(ORDER_CREATE)
                .then();
    }

    @Step("Шаг создания заказа неавторизованным пользователем")
    public ValidatableResponse createOrderNotAuthorizedUser(Order order) {
        return given()
                .body(order)
                .post(ORDER_CREATE)
                .then();
    }

    @Step("Получение заказов авторизованным пользователем")
    public ValidatableResponse getOrdersAuthorizedUser(Order order) {
        return given()
                .header("Authorization", order.getAccessToken())
                .get(ORDERS_GET)
                .then();
    }

    @Step("Получение заказов неавторизованным пользователем")
    public ValidatableResponse getOrdersNotAuthorizedUser(Order order) {
        return given()
                .get(ORDERS_GET)
                .then();
    }
}
