package api.services;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class ProductApiServiceTest {

    ProductApiService productApiService = new ProductApiService();

    @Test
    public void getAllProductsNameTest() {

        List<String> productNames = productApiService.getAllProductsNameAsList();
        assertFalse(productNames.isEmpty());
        assertTrue(productNames.contains("Blue Top"));
    }

}