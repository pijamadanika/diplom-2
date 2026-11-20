package stellarburgers;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stellarburgers.support.ApiTest;
import stellarburgers.support.Checks;

import java.util.List;

@DisplayName("GET /api/orders — личная история заказов")
class GetUserOrdersTest extends ApiTest {
    @Test
    @DisplayName("В истории нового пользователя отображается его заказ")
    void returnsOwnOrder() {
        registeredUser();
        List<String> ingredients = api.ingredients();
        int orderNumber = Checks.createdOrder(api.createOrder(ingredients, true));

        Response history = api.orders(true);
        Checks.orderHistory(history, orderNumber, ingredients);
    }

    @Test
    @DisplayName("История заказов без авторизации недоступна")
    void rejectsGuest() {
        Checks.unauthorized(api.orders(false));
    }
}
