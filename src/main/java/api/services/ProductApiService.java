package api.services;

import api.core.BaseApiService;
import io.restassured.response.Response;

import java.util.List;

public class ProductApiService extends BaseApiService {

    private static final String GET_PRODUCTS_LIST_ENDPOINT = "/api/productsList";

    public List<String> getAllProductsNameAsList() {
        Response response = restClient.get(GET_PRODUCTS_LIST_ENDPOINT);

        if (response.jsonPath().getInt("responseCode") != 200) {
            throw new RuntimeException("Unable to get Products list due to " + response.jsonPath().getInt("responseCode") + ": " + response.getBody().asString());
        }

        if (response.jsonPath().getList("products.name", String.class).isEmpty()) {
            throw new RuntimeException("Product list is empty");
        }

        return response.jsonPath().getList("products.name", String.class);
    }
}
