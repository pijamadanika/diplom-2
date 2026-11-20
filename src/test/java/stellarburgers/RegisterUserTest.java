package stellarburgers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import stellarburgers.support.ApiTest;
import stellarburgers.support.Checks;
import stellarburgers.support.UserData;

@DisplayName("POST /api/auth/register — регистрация")
class RegisterUserTest extends ApiTest {
    @Test
    @DisplayName("Уникальный пользователь получает профиль и токены")
    void createsUniqueUser() {
        UserData user = UserData.unique();
        Checks.session(api.register(user), user);
    }

    @Test
    @DisplayName("Повторная регистрация с тем же email отклоняется")
    void rejectsDuplicateUser() {
        UserData user = registeredUser();
        Checks.error(api.register(user), 403, "User already exists");
    }

    @ParameterizedTest(name = "Регистрация без поля {0}")
    @ValueSource(strings = {"email", "password", "name"})
    void requiresEachField(String field) {
        UserData incomplete = UserData.unique().without(field);
        Checks.error(api.register(incomplete), 403, "Email, password and name are required fields");
    }
}
