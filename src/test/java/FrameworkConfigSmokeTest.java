import config.FrameworkConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class FrameworkConfigSmokeTest {

    private static final String PRODUCTS_ENDPOINT = "/products";

    @Test
    public void statusCodeTest() {
        Assert.assertEquals(200, getStatusCode(FrameworkConfig.APP_URL));
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
