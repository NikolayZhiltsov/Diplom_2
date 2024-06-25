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

import java.util.List;

public class OrderCreateTests extends AbstractTest {
    private OrderSteps orderSteps = new OrderSteps();
    private UserSteps userSteps = new UserSteps();
    private User user = new User();
    private Order order;
    private List<String> ids;
    private String accessToken;
    private String ingredient1;
    private String ingredient2;
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
        ids = orderSteps
                .getIngredients(order)
                .extract().body().path("data._id");
    }

    @Test
    @DisplayName("Проверяем успешное создание заказа с двумя ингредиентами авторизованным пользователем")
    public void shouldReturnStatusCode200AndSuccessMessageWithIngredients() {
        order = new Order();
        ingredient1 = ids.get(Integer.parseInt(RandomStringUtils.randomNumeric(1)));
        ingredient2 = ids.get(Integer.parseInt(RandomStringUtils.randomNumeric(1)));
        String[] testIngredients = {ingredient1, ingredient2};
        order.setIngredients(testIngredients);
        order.setAccessToken(accessToken);
        orderSteps.createOrderAuthorizedUser(order)
                .statusCode(200)
                .body("success", Matchers.is(true),
                        "name", Matchers.notNullValue(),
                        "order.number", Matchers.notNullValue(),
                        "order.createdAt", Matchers.notNullValue(),
                        "order.updatedAt", Matchers.notNullValue(),
                        "order.ingredients._id[0]", Matchers.is(ingredient1),
                        "order.ingredients._id[1]", Matchers.is(ingredient2),
                        "order.owner.name", Matchers.is(name),
                        "order.owner.email", Matchers.is(email));
    }

    @Test
    @DisplayName("Проверяем невозможность создания заказа с двумя ингредиентами неавторизованным пользователем")
    /*
    Данный тест всегда падает, т.к. в поведении ручки есть баг — возможность создавать заказ неавторизованному пользователю.
    В документации сказано, что при попытке создания заказа неавторизованным пользователем должна возвращаться ошибка.
     */
    public void shouldReturnStatusCode401AndMessageNotAuthorized() {
        order = new Order();
        ingredient1 = ids.get(Integer.parseInt(RandomStringUtils.randomNumeric(1)));
        ingredient2 = ids.get(Integer.parseInt(RandomStringUtils.randomNumeric(1)));
        String[] testIngredients = {ingredient1, ingredient2};
        order.setIngredients(testIngredients);
        orderSteps.createOrderNotAuthorizedUser(order)
                .statusCode(401)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("You should be authorized"));
    }

    @Test
    @DisplayName("Проверяем невозможность создания заказа с невалидным хэшем одного из ингридиентов авторизованным пользователем")
    public void shouldReturnStatusCode500() {
        order = new Order();
        ingredient1 = ids.get(Integer.parseInt(RandomStringUtils.randomNumeric(1)));
        ingredient2 = RandomStringUtils.random(24, true, true).toLowerCase();
        String[] testIngredients = {ingredient1, ingredient2};
        order.setIngredients(testIngredients);
        order.setAccessToken(accessToken);
        orderSteps.createOrderAuthorizedUser(order)
                .statusCode(500);
    }

    @Test
    @DisplayName("Проверяем невозможность создания заказа без ингредиентов авторизованным пользователем")
    public void shouldReturnStatusCode400AndErrorMessage() {
        order = new Order();
        order.setIngredients(null);
        order.setAccessToken(accessToken);
        orderSteps.createOrderAuthorizedUser(order)
                .statusCode(400)
                .body("success", Matchers.is(false),
                        "message", Matchers.is("Ingredient ids must be provided"));
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
