package api.core;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestClient {

    private final String baseUrl;

    /*
     * Без этой строки НЕ РАБОТАЕТ ни один response.as(Model.class) в проекте.
     *
     * Причина не в Jackson, а в стенде: automationexercise.com отдаёт JSON-тело,
     * но объявляет его как "content-type: text/html; charset=utf-8" (проверено curl -D -
     * на productsList, verifyLogin, getUserDetailByEmail — везде одинаково).
     * REST Assured выбирает парсер ПО ЗАГОЛОВКУ, а не по содержимому, поэтому as(...)
     * падал ещё до того, как дело доходило до десериализации:
     *
     *   IllegalStateException: Cannot parse object because no supported Content-Type
     *   was specified in response. Content-Type was 'text/html; charset=utf-8'.
     *
     * defaultParser = Parser.JSON означает «тело неопознанного типа разбирай как JSON».
     *
     * Почему именно Parser.JSON, а не указание маппера в точке вызова
     * (response.as(X.class, ObjectMapperType.JACKSON_2)): Parser.JSON НЕ НАЗЫВАЕТ библиотеку,
     * поэтому переход Jackson → Gson это одна строка в pom.xml и ноль строк Java.
     *
     * Почему здесь, в src/main, а не в тестовой инфраструктуре: без этой настройки не работает
     * продакшн-код фреймворка. Для сравнения, Allure-фильтр живёт в src/test — без него
     * лишь пропадают вложения в отчёте, а код продолжает работать.
     *
     * Осознанный побочный эффект: RestAssured.defaultParser — СТАТИЧЕСКОЕ поле всей библиотеки,
     * то есть загрузка этого класса меняет глобальное состояние процесса.
     */
    static {
        RestAssured.defaultParser = Parser.JSON;
    }

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response get(String endpoint) {
        return given()
                .baseUri(baseUrl)
                .get(endpoint);
    }

    public Response get(String endpoint, Map<String, String> queryParams) {
        return given()
                .baseUri(baseUrl)
                .queryParams(queryParams)
                .get(endpoint);
    }

    public Response post(String endpoint, Map<String, String> formParams) {
        return given()
                .baseUri(baseUrl)
                .formParams(formParams)
                .post(endpoint);
    }

    public Response put(String endpoint, Map<String, String> formParams) {
        return given()
                .baseUri(baseUrl)
                .formParams(formParams)
                .put(endpoint);
    }

    public Response delete(String endpoint, Map<String, String> formParams) {
        return given()
                .baseUri(baseUrl)
                .formParams(formParams)
                .delete(endpoint);
    }
}
