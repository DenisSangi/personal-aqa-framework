package api.core;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestClient {

    private final String baseUrl;

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
