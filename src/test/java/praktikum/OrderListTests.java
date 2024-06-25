package praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.Order;
import praktikum.model.User;
import praktikum.steps.OrderSteps;
import praktikum.steps.UserSteps;

public class OrderListTests extends AbstractTest{
    private OrderSteps orderSteps = new OrderSteps();
    private UserSteps userSteps = new UserSteps();
    private User user = new User();
    private Order order;
    private String accessToken;
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
        accessToken = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        user.setAccessToken(accessToken);
    }

    @Test
    @DisplayName("Проверяем возможностль получить заказы авторизованным пользователем")
    public void shouldReturnStatus200AndOrderList() {
        order = new Order();
        order.setAccessToken(accessToken);
        orderSteps
                .getOrdersAuthorizedUser(order)
                .statusCode(200)
                .body("success", Matchers.is(true),
                        "total", Matchers.notNullValue(),
                        "totalToday", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Проверяем невозможность получить заказы неавторизованным пользователем")
    public void shouldReturnStatusCode401AndMessageNotAuthorized() {
        order = new Order();
        orderSteps
                .getOrdersNotAuthorizedUser(order)
                .statusCode(401)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("You should be authorised"));
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
