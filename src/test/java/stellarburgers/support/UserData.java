package stellarburgers.support;

import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UserData {
    private final Map<String, String> fields;

    private UserData(Map<String, String> fields) {
        this.fields = Map.copyOf(fields);
    }

    @Step("Подготовить уникального пользователя")
    public static UserData unique() {
        String id = UUID.randomUUID().toString();
        return new UserData(Map.of("email", "burger-" + id + "@example.com",
                "password", "Test-" + id, "name", "Tester-" + id.substring(0, 8)));
    }

    public UserData without(String field) {
        Map<String, String> copy = new HashMap<>(fields);
        copy.remove(field);
        return new UserData(copy);
    }

    public UserData replace(String field, UserData source) {
        Map<String, String> copy = new HashMap<>(fields);
        copy.put(field, source.get(field));
        return new UserData(copy);
    }

    public String get(String field) {
        return fields.get(field);
    }

    Map<String, String> registration() {
        return fields;
    }

    Map<String, String> credentials() {
        return Map.of("email", get("email"), "password", get("password"));
    }

    @Override
    public String toString() {
        return fields.getOrDefault("email", "(без email)");
    }
}
