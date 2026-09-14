import config.FrameworkConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestingSiteAvailabilitySmokeTest {

    private static final String PRODUCTS_ENDPOINT = "/products";

    @Test
    public void statusCodeTest() {
        Assert.assertEquals(getStatusCode(FrameworkConfig.APP_URL), 200);
    }

    @Test
    public void responseBodyTest() {
        String allProductsTitle = "All Products";
        Assert.assertTrue(getResponseBody(FrameworkConfig.APP_URL).contains(allProductsTitle));
    }

    public int getStatusCode(String url) {
        return given()
                .baseUri(url)
                .when()
                .get(PRODUCTS_ENDPOINT)
                .then()
                .extract().statusCode();
    }

    public String getResponseBody(String url) {
        return given()
                .baseUri(url)
                .when()
                .get(PRODUCTS_ENDPOINT)
                .then()
                .extract().body().asString();
    }
}
