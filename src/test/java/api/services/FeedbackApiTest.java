package api.services;

import api.models.PlayerRequestModel;
import api.models.PlayerResponseModel;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class FeedbackApiTest {

    private String playerServiceBaseUri = "http://localhost:8080";
    private String wiremockBaseUri = "http://localhost:8089";

    @Test
    public void LogAllAndPathParamsTest() {
        given()
                .log().all()
                .baseUri(playerServiceBaseUri)
                .pathParam("id", 1)
                .get("/api/players/{id}")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void QueryParamsTest() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("country", "KZ");
        queryParams.put("size", "3");

        given()
                .log().all()
                .baseUri(playerServiceBaseUri)
                .queryParams(queryParams)
                .get("/api/players")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void ResponseModelTest() {
        PlayerResponseModel response = given()
                .log().all()
                .baseUri(playerServiceBaseUri)
                .get("/api/players/1")
                .then()
                .statusCode(200)
                .extract().as(PlayerResponseModel.class);
        Assert.assertEquals(response.getId(), 1);
        Assert.assertEquals(response.getCountry(), "KZ");
        Assert.assertEquals(response.getBalance().compareTo(new BigDecimal("2364.750")), 0);
    }

    @Test
    public void JsonPathTest() {
        String countryFromResponse = given()
                .log().all()
                .baseUri(playerServiceBaseUri)
                .get("/api/players/1")
                .then()
                .statusCode(200)
                .and()
                .extract().jsonPath().get("country");

        Assert.assertEquals(countryFromResponse, "KZ");
    }

    @Test
    public void RequestModelTest() {
        PlayerRequestModel playerRequestModel = new PlayerRequestModel();
        playerRequestModel
                .setEmail(System.currentTimeMillis() + "@mail.com");
        playerRequestModel
                .setUsername(String.valueOf(System.currentTimeMillis()));
        playerRequestModel.setCountry("KZ");

        PlayerResponseModel playerResponseModel = given()
                .log().all()
                .baseUri(playerServiceBaseUri)
                .contentType(ContentType.JSON).body(playerRequestModel)
                .post("/api/players")
                .then()
                .statusCode(201)
                .extract().body().as(PlayerResponseModel.class);

        Assert.assertEquals(playerResponseModel.getUsername(), playerRequestModel.getUsername());
    }

    @Test
    public void SchemaValidationTest() {
        given()
                .log().all()
                .baseUri(playerServiceBaseUri)
                .pathParam("id", 1)
                .get("/api/players/{id}")
                .then()
                .statusCode(200)
                .and()
                .body(matchesJsonSchemaInClasspath("schemas/player-schema.json"));
    }

    @Test
    public void AuthorizationBasicTest() {
        given()
                .log().all()
//                .auth().preemptive().basic("user", "pass")
//                .auth().preemptive().oauth2("abc123")
                .auth().preemptive().oauth2("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6InVzZXIifQ.Zt1u0pnJuGGB3SJpmqhfv8IdtWCm0PG6Iy8UjGjyvps")
                .baseUri(wiremockBaseUri)
                .get("/auth-check");
    }

    @Test
    public void AcceptedStatusTest() throws InterruptedException {
        int maxAttempts = 5;
        long timeout = 1000;
        String expectedStatus = "DONE";
        String actualStatus = null;

        String id = given()
                .log().all()
                .baseUri(wiremockBaseUri)
                .post("reports")
                .then()
                .statusCode(202)
                .extract().body().jsonPath().get("id");

        for (int i = 0; i < maxAttempts; i++) {
            actualStatus = given()
                    .pathParam("id", id)
                    .baseUri(wiremockBaseUri)
                    .get("/reports/{id}")
                    .then()
                    .extract().body().jsonPath().get("status");

            if (actualStatus.equals(expectedStatus)) {
                break;
            } else {
                Thread.sleep(timeout);
            }
        }

        Assert.assertEquals(actualStatus, expectedStatus);
    }



}
