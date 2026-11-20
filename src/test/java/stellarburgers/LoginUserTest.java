package stellarburgers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import stellarburgers.support.ApiTest;
import stellarburgers.support.Checks;
import stellarburgers.support.UserData;

@DisplayName("POST /api/auth/login — вход")
class LoginUserTest extends ApiTest {
    @Test
    @DisplayName("Существующий пользователь входит со своими учётными данными")
    void logsInWithRegisteredCredentials() {
        UserData user = registeredUser();
        Checks.session(api.login(user), user);
    }

    @ParameterizedTest(name = "Неверный email: {0}, неверный пароль: {1}")
    @CsvSource({"true,false", "false,true", "true,true"})
    void rejectsInvalidCredentials(boolean wrongEmail, boolean wrongPassword) {
        UserData user = registeredUser();
        UserData other = UserData.unique();
        if (wrongEmail) {
            user = user.replace("email", other);
        }
        if (wrongPassword) {
            user = user.replace("password", other);
        }
        Checks.error(api.login(user), 401, "email or password are incorrect");
    }
}
