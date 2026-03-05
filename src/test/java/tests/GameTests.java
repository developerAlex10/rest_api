package tests;

import api.ApiClient;
import dto.common.Dlc;
import dto.request.GameRequest;
import dto.response.GameCreateResponse;
import dto.response.UserInfoResponse;
import extensions.UserExtension;
import extensions.UserSession;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import utils.TestDataFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(UserExtension.class)
public class GameTests {

    private UserSession getSession() {
        return UserExtension.getSession();
    }

    @Test
    @DisplayName("Регистрация пользователя происходит через Extension")
    @Description("Проверяем, что пользователь создан (токен не пустой)")
    void userIsCreated() {
        // 1. Получить сессию пользователя
        UserSession session = getSession();
        // 2. Проверить, что токен получен
        assertThat(session.getToken()).isNotEmpty();
    }

    @Test
    @DisplayName("Добавление игры с минимальными параметрами")
    @Description("Проверяем успешное создание игры и получение gameId")
    void addMinimalGame() {
        // 1. Получить данные пользователя
        UserSession session = getSession();
        ApiClient api = session.getApiClient();
        String token = session.getToken();

        // 2. Создать игру с минимальными параметрами
        GameRequest game = TestDataFactory.minimalGame();
        GameCreateResponse response = api.addGame(token, game);

        // 3. Проверить, что игра создана и получен gameId
        assertThat(response.getRegister_data().getGameId()).isPositive();
    }

    @Test
    @DisplayName("Добавление игры со случайными параметрами")
    @Description("Проверяем создание игры с полностью случайными данными")
    void addRandomGame() {
        // 1. Получить данные пользователя
        UserSession session = getSession();
        ApiClient api = session.getApiClient();
        String token = session.getToken();

        // 2. Создать игру со случайными параметрами
        GameRequest game = TestDataFactory.randomGame();
        GameCreateResponse response = api.addGame(token, game);

        // 3. Проверить успешное создание
        assertThat(response.getRegister_data().getGameId()).isPositive();
        assertThat(response.getInfo().getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Обновление DLC существующей игры")
    @Description("Создаём игру, затем обновляем её DLC и проверяем изменения")
    void updateGameDlcs() {
        // 1. Получить данные пользователя
        UserSession session = getSession();
        ApiClient api = session.getApiClient();
        String token = session.getToken();

        // 2. Создать игру без DLC
        GameRequest game = TestDataFactory.minimalGame();
        game.setDlcs(List.of());
        GameCreateResponse createResponse = api.addGame(token, game);
        int gameId = createResponse.getRegister_data().getGameId();

        // 3. Обновить DLC игры
        List<Dlc> newDlcs = TestDataFactory.updatedDlcs();
        var updateResponse = api.updateGameDlcs(token, gameId, newDlcs);

        // 4. Проверить, что обновление прошло успешно
        assertThat(updateResponse.statusCode()).isEqualTo(200);

        // 5. Получить игру и проверить, что DLC обновились
        GameRequest gameAfterUpdate = api.getGame(token, gameId);
        assertThat(gameAfterUpdate.getDlcs())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(newDlcs);
    }

    @Test
    @DisplayName("Получение информации о пользователе после регистрации")
    @Description("Проверяем, что после регистрации у пользователя нет игр, логин совпадает")
    void getUserInfoAfterRegistration() {
        // 1. Получить данные пользователя
        UserSession session = getSession();
        ApiClient api = session.getApiClient();
        String token = session.getToken();
        String expectedLogin = session.getLogin();

        // 2. Запросить информацию о пользователе
        UserInfoResponse userInfo = api.getUserInfo(token);

        // 3. Проверить данные пользователя
        assertThat(userInfo.getLogin()).isEqualTo(expectedLogin);
        assertThat(userInfo.getId()).isPositive();
        assertThat(userInfo.getGames()).isEmpty();
    }

    @Test
    @DisplayName("Получение информации о пользователе после добавления игры")
    @Description("Добавляем игру и проверяем, что она отображается в информации пользователя")
    void getUserInfoAfterAddingGame() {
        // 1. Получить данные пользователя
        UserSession session = getSession();
        ApiClient api = session.getApiClient();
        String token = session.getToken();

        // 2. Создать игру
        GameRequest game = TestDataFactory.minimalGame();
        GameCreateResponse createResponse = api.addGame(token, game);
        int gameId = createResponse.getRegister_data().getGameId();

        // 3. Запросить информацию о пользователе
        UserInfoResponse userInfo = api.getUserInfo(token);

        // 4. Найти созданную игру в списке игр пользователя
        var foundGame = userInfo.getGames().stream()
                .filter(g -> g.getGameId() == gameId)
                .findFirst();
        assertThat(foundGame).isPresent();
        assertThat(foundGame.get().getTitle()).isEqualTo(game.getTitle());
    }
}