package api;

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

    public Response post(String endpoint, Map<String, String> formParams) {
        return given()
                .baseUri(baseUrl)
                .formParams(formParams)
                .post(endpoint);
    }
}
