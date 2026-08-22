package api.services;

import api.models.ProductResponseModel;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class ProductApiServiceTest {

    ProductApiService productApiService = new ProductApiService();

    @Test
    public void getAllProductsAsModelTest() {
        List<ProductResponseModel> productResponseModel = productApiService.getAllProductsAsModel();
        assertEquals(productResponseModel.get(0).getId(), 1);
        assertEquals(productResponseModel.get(0).getName(), "Blue Top");
        assertEquals(productResponseModel.get(0).getCategory().getCategory(), "Tops");
        assertEquals(productResponseModel.get(0).getCategory().getUserType().getUserType(), "Women");
        assertEquals(productResponseModel.get(0).getPrice(), "Rs. 500");
        assertEquals(productResponseModel.get(0).getBrand(), "Polo");
    }
}