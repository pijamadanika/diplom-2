package stellarburgers.support;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BurgerApi {
    private final String baseUri = System.getProperty("baseUri", "https://stellarburgers.education-services.ru");
    private String accessToken;

    private RequestSpecification request(boolean authorized) {
        RequestSpecification request = given().baseUri(baseUri).basePath("/api").contentType(ContentType.JSON);
        if (authorized) {
            request.header("Authorization", accessToken);
        }
        return request;
    }

    @Step("Зарегистрировать пользователя")
    public Response register(UserData user) {
        Response response = request(false).body(user.registration()).post("/auth/register");
        if (response.statusCode() == 200) {
            accessToken = response.path("accessToken");
        }
        return response;
    }

    @Step("Войти с указанными учётными данными")
    public Response login(UserData user) {
        return request(false).body(user.credentials()).post("/auth/login");
    }

    @Step("Изменить поле {field} профиля (с авторизацией: {authorized})")
    public Response update(String field, UserData changes, boolean authorized) {
        return request(authorized).body(Map.of(field, changes.get(field))).patch("/auth/user");
    }

    @Step("Получить из каталога булку и начинку для заказа")
    public List<String> ingredients() {
        Response response = request(false).get("/ingredients");
        String bun = "data.find { it.type == 'bun' }._id";
        String main = "data.find { it.type == 'main' }._id";
        response.then().statusCode(200).body("success", is(true),
                bun, not(emptyOrNullString()), main, not(emptyOrNullString()));
        return List.of(response.path(bun), response.path(main));
    }

    @Step("Оформить заказ (с авторизацией: {authorized})")
    public Response createOrder(List<String> ingredients, boolean authorized) {
        return request(authorized).body(Map.of("ingredients", ingredients)).post("/orders");
    }

    @Step("Запросить личную историю заказов (с авторизацией: {authorized})")
    public Response orders(boolean authorized) {
        return request(authorized).get("/orders");
    }

    public void deleteUserIfCreated() {
        if (accessToken != null) {
            deleteUser();
        }
    }

    @Step("Удалить созданного пользователя")
    private void deleteUser() {
        request(true).delete("/auth/user").then().statusCode(202).body("success", is(true));
    }
}
