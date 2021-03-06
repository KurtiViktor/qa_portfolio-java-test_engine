package test_engine;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import retrofit2.Call;
import retrofit2.Response;
import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockUriResolver;
import test_engine.api.rest.retrofit.APIRequests;
import test_engine.api.rest.retrofit.RetrofitAdapter;
import test_engine.api.rest.retrofit.model.UserData;
import test_engine.api.testdata.model.TestData;
import test_engine.ext.junit5.interf.EXT;
import test_engine.ext.junit5.interf.JsonFileSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для проверки функциональности из расширений.
 */
@EXT
@Slf4j
@DisplayName("Юнит-тесты для проверки функциональности из расширений")
class ExtTests {

    /**
     * Фикстура перед всеми api тестами.
     */
    @BeforeAll
    @DisplayName("Фикстура перед всеми api тестами")
    static void setUp() {
        log.info("Фикстура перед всеми ext тестами " + Thread.currentThread().getName());
    }

    /**
     * Тест запроса одиночного пользователя.
     *
     * @param td     тестовые данные
     * @param server сервер, получен из DI
     * @param uri    the uri
     * @throws IOException the io exception
     */
    @ParameterizedTest(name = "Запрос одиночного пользователя")
    @JsonFileSource(filePath = "/testdata/api1.json")
    void singleUserApiTest(
            TestData td,
            @WiremockResolver.Wiremock WireMockServer server,
            @WiremockUriResolver.WiremockUri String uri) throws IOException {
        APIRequests api = RetrofitAdapter.build(uri);
        Response<UserData> response = sendRequest(td.getUser_id(), api);
        UserData userData = response.body();
        assertNotNull(userData, "Сервер не ответил или произошла ошибка автотестов " + response.code());
        assertAll("response check",
                () -> assertEquals(response.code(), td.getResponse_status()),
                () -> assertEquals(userData.getData(), td.getResponse().getData())
        );
    }

    /**
     * Тест битого запроса одиночного пользователя.
     *
     * @param td     тестовые данные
     * @param server сервер, получен из DI
     * @param uri    the uri
     * @throws IOException the io exception
     */
    @ParameterizedTest(name = "Битый запрос одиночного пользователя")
    @JsonFileSource(filePath = "/testdata/api2.json")
    void singleUserNotFoundApiTest(
            TestData td,
            @WiremockResolver.Wiremock WireMockServer server,
            @WiremockUriResolver.WiremockUri String uri) throws IOException {
        APIRequests api = RetrofitAdapter.build(uri);
        Response<UserData> response = sendRequest(td.getUser_id(), api);
        UserData userData = response.body();
        assertAll(
                "response check",
                () -> assertEquals(
                        response.code(),
                        403
                ),
                () -> assertNull(
                        userData,
                        "Сервер не ответил или произошла ошибка автотестов " + response.code()
                )
        );
    }

    /**
     * Шаг установки текста {text} на поиск.
     *
     * @param id  the id
     * @param api объект класса с запросами к апи
     * @return объект ответа со статусом и телом ответа
     * @throws IOException the io exception
     */
    @Step("Задать текст {text} на поиск")
    Response<UserData> sendRequest(String id, APIRequests api) throws IOException {
        log.info("запрос одиночного пользователя " + Thread.currentThread().getName());
        Call<UserData> callSync = api.getUserData(id);
        return callSync.execute();
    }

}
