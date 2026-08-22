package api.services;

import api.core.BaseApiService;
import api.models.ProductResponseModel;
import api.models.ResponseProductContainerModel;
import io.restassured.response.Response;

import java.util.List;

public class ProductApiService extends BaseApiService {

    private static final String GET_PRODUCTS_LIST_ENDPOINT = "/api/productsList";

    /**
     * Та же асимметрия, что в {@code AccountApiService#getAccountDetailsByEmail}: разбирается
     * КОНВЕРТ {@link api.models.ResponseProductContainerModel} (потому что {@code as(...)} всегда
     * работает от корня тела), а наружу отдаётся его полезная нагрузка — список товаров.
     * <p>
     * Каждый элемент списка — ЦЕЛЫЙ товар со всеми полями, включая вложенную категорию
     * ({@code get(0).getCategory().getUserType().getUserType()} — три уровня вглубь),
     * а не отдельное поле.
     */
    public List<ProductResponseModel> getAllProductsAsModel() {
        Response response = restClient.get(GET_PRODUCTS_LIST_ENDPOINT);

        ResponseProductContainerModel responseProductContainerModel = response.getBody().as(ResponseProductContainerModel.class);

        if (responseProductContainerModel.getResponseCode() != 200) {
            throw new RuntimeException("Unable to get Products list: " + responseProductContainerModel.getResponseCode() + " " + responseProductContainerModel.getMessage());
        }

        if (responseProductContainerModel.getProducts().isEmpty()) {
            throw new RuntimeException(("Products list is empty"));
        }

        return responseProductContainerModel.getProducts();
    }
}
