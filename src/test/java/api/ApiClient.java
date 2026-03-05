package api;

import dto.common.Dlc;
import dto.request.AuthRequest;
import dto.request.GameRequest;
import dto.request.UserRequest;
import dto.response.AuthResponse;
import dto.response.GameCreateResponse;
import dto.response.UserInfoResponse;
import dto.response.UserResponse;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private static final String BASE_URL = "http://85.192.34.140:8080/api/";

    private RequestSpecification baseSpec() {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .log().ifValidationFails();
    }

    private RequestSpecification authSpec(String token) {
        return baseSpec().header("Authorization", "Bearer " + token);
    }

    @Step("Регистрация пользователя {login}")
    public UserResponse register(String login, String password) {
        var request = UserRequest.builder()
                .login(login)
                .pass(password)
                .games(new ArrayList<>())
                .build();
        return baseSpec()
                .body(request)
                .post("/signup")
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponse.class);
    }

    @Step("Авторизация и получение токена для {login}")
    public String token(String login, String password) {
        var request = AuthRequest.builder()
                .username(login)
                .password(password)
                .build();
        return baseSpec()
                .body(request)
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class)
                .getToken();
    }

    @Step("Добавление игры")
    public GameCreateResponse addGame(String token, GameRequest game) {
        return authSpec(token)
                .body(game)
                .post("/user/games")
                .then()
                .statusCode(201)
                .extract()
                .as(GameCreateResponse.class);
    }

    @Step("Обновление DLC для игры {gameId}")
    public Response updateGameDlcs(String token, int gameId, List<Dlc> dlcs) {
        return authSpec(token)
                .body(dlcs)
                .put("/user/games/" + gameId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("Получение игры по ID {gameId}")
    public GameRequest getGame(String token, int gameId) {
        return authSpec(token)
                .get("/user/games/" + gameId)
                .then()
                .statusCode(200)
                .extract()
                .as(GameRequest.class);
    }

    @Step("Удаление пользователя")
    public void deleteUser(String token) {
        authSpec(token)
                .delete("/user")
                .then()
                .statusCode(200);
    }

    @Step("Получение информации о пользователе")
    public UserInfoResponse getUserInfo(String token) {
        return authSpec(token)
                .get("/user")
                .then()
                .statusCode(200)
                .extract()
                .as(UserInfoResponse.class);
    }
}