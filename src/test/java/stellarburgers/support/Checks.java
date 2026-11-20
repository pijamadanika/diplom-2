package stellarburgers.support;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static org.hamcrest.Matchers.*;

public final class Checks {
    private Checks() {
    }

    @Step("Проверить статус 200 и данные профиля")
    public static void profile(Response response, UserData expected) {
        response.then().statusCode(200).body("success", is(true),
                "user.email", equalTo(expected.get("email")), "user.name", equalTo(expected.get("name")));
    }

    @Step("Проверить профиль и выданные токены")
    public static void session(Response response, UserData expected) {
        profile(response, expected);
        response.then().body("accessToken", matchesPattern("Bearer \\S+"), "refreshToken", not(emptyOrNullString()));
    }

    @Step("Проверить статус {status} и сообщение об ошибке")
    public static void error(Response response, int status, String message) {
        response.then().statusCode(status).body("success", is(false), "message", equalTo(message));
    }

    @Step("Проверить отказ неавторизованному пользователю")
    public static void unauthorized(Response response) {
        error(response, 401, "You should be authorised");
    }

    @Step("Проверить успешное создание заказа и получить его номер")
    public static int createdOrder(Response response) {
        return response.then().statusCode(200).body("success", is(true), "name", not(emptyOrNullString()),
                "order.number", greaterThan(0)).extract().path("order.number");
    }

    @Step("Проверить номер и состав единственного заказа нового пользователя")
    public static void orderHistory(Response response, int number, List<String> ingredients) {
        response.then().statusCode(200).body("success", is(true), "orders.number", contains(number),
                "orders[0].ingredients", containsInAnyOrder(ingredients.toArray(new String[0])));
    }

    @Step("Проверить статус 500 и наличие тела ошибки")
    public static void invalidIngredient(Response response) {
        // В спецификации для этой ошибки указан только статус, без формата тела.
        response.then().statusCode(500).body(not(blankOrNullString()));
    }
}
