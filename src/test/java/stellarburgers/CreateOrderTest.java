package stellarburgers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stellarburgers.support.ApiTest;
import stellarburgers.support.Checks;

import java.util.List;

@DisplayName("POST /api/orders — создание заказа")
class CreateOrderTest extends ApiTest {
    @Test
    @DisplayName("Авторизованный пользователь оформляет заказ с ингредиентами")
    void createsOrderWithIngredients() {
        registeredUser();
        Checks.createdOrder(api.createOrder(api.ingredients(), true));
    }

    @Test
    @DisplayName("Гость оформляет заказ с ингредиентами и получает номер")
    void createsGuestOrder() {
        Checks.createdOrder(api.createOrder(api.ingredients(), false));
    }

    @Test
    @DisplayName("Заказ без ингредиентов отклоняется")
    void rejectsEmptyIngredients() {
        registeredUser();
        Checks.error(api.createOrder(List.of(), true), 400, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Невалидный хеш ингредиента приводит к ошибке 500")
    void rejectsInvalidIngredientHash() {
        registeredUser();
        Checks.invalidIngredient(api.createOrder(List.of("invalid-ingredient-hash"), true));
    }
}
