package stellarburgers.support;

import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;

public abstract class ApiTest {
    protected final BurgerApi api = new BurgerApi();

    @Step("Создать пользователя для сценария")
    protected UserData registeredUser() {
        UserData user = UserData.unique();
        Checks.session(api.register(user), user);
        return user;
    }

    @AfterEach
    void cleanUp() {
        api.deleteUserIfCreated();
    }
}
