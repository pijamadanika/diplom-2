package stellarburgers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import stellarburgers.support.ApiTest;
import stellarburgers.support.Checks;
import stellarburgers.support.UserData;

@DisplayName("PATCH /api/auth/user — изменение профиля")
class UpdateUserTest extends ApiTest {
    @ParameterizedTest(name = "С авторизацией изменение {0} сохраняется")
    @ValueSource(strings = {"email", "password", "name"})
    void savesEachFieldWithAuthorization(String field) {
        UserData user = registeredUser();
        UserData updated = user.replace(field, UserData.unique());
        Checks.profile(api.update(field, updated, true), updated);
        Checks.session(api.login(updated), updated);
        if ("password".equals(field)) {
            Checks.error(api.login(user), 401, "email or password are incorrect");
        }
    }

    @ParameterizedTest(name = "Без авторизации изменение {0} отклоняется")
    @ValueSource(strings = {"email", "password", "name"})
    void rejectsEachFieldWithoutAuthorization(String field) {
        UserData user = registeredUser();
        Checks.unauthorized(api.update(field, UserData.unique(), false));
        Checks.session(api.login(user), user);
    }
}
