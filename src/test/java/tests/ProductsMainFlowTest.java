package tests;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import constants.TableHeaders;
import core.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductsPage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ProductsMainFlowTest extends BaseTest {

    private ProductsPage productsPage;
    private CartPage cartPage;
    private final String testingProductOne = "Winter Top",
            testingProductTwo = "Blue Top",
            testingProductThree = "Summer White Top";


    @BeforeMethod
    public void setup() {
        Selenide.open(FrameworkConfig.APP_URL);
    }

    @Test
    public void searchProductTest() {
        HomePage homePage = new HomePage().verifyPageIsOpen();
        productsPage = homePage.clickProductsLink();
        productsPage.verifyPageIsOpen()
                .setValueInSearchInputField(testingProductOne)
                .clickSubmitSearchButton();
        assertEquals(productsPage.getSearchResultSize(), 1);
        assertTrue(productsPage.isSearchResultsContainsProductName(testingProductOne));
    }

    @Test
    public void addProductToCartTest() {
        HomePage homePage = new HomePage().verifyPageIsOpen();
        productsPage = homePage.clickProductsLink().verifyPageIsOpen();
        cartPage = productsPage.addProductToCart(testingProductOne)
                .verifyModalIsDisplayed()
                .clickModalContinueShoppingButton()
                .addProductToCart(testingProductTwo)
                .verifyModalIsDisplayed()
                .clickModalContinueShoppingButton()
                .addProductToCart(testingProductThree)
                .verifyModalIsDisplayed()
                .clickModalContinueShoppingButton()
                .addProductToCart(testingProductThree)
                .verifyModalIsDisplayed()
                .clickModalViewCartButton();
        cartPage.verifyPageIsOpen();
        assertEquals(cartPage.getCartTableSize(), 3);
        assertEquals(cartPage.getCellValueInCartTable(0, TableHeaders.CT_DESCRIPTION.getHeaderName()), testingProductOne);
        assertEquals(cartPage.getCellValueInCartTable(0, TableHeaders.CT_PRICE.getHeaderName()), "Rs. 500");
        assertEquals(cartPage.getCellValueInCartTable(0, TableHeaders.CT_QUANTITY.getHeaderName()), "1");
        assertEquals(cartPage.getCellValueInCartTable(1, TableHeaders.CT_DESCRIPTION.getHeaderName()), testingProductTwo);
        assertEquals(cartPage.getCellValueInCartTable(1, TableHeaders.CT_PRICE.getHeaderName()), "Rs. 600");
        assertEquals(cartPage.getCellValueInCartTable(1, TableHeaders.CT_QUANTITY.getHeaderName()), "1");
        assertEquals(cartPage.getCellValueInCartTable(2, TableHeaders.CT_DESCRIPTION.getHeaderName()), testingProductThree);
        assertEquals(cartPage.getCellValueInCartTable(2, TableHeaders.CT_PRICE.getHeaderName()), "Rs. 400");
        assertEquals(cartPage.getCellValueInCartTable(2, TableHeaders.CT_QUANTITY.getHeaderName()), "2");
    }

    @Test
    public void proceedToCheckoutTest() {
        HomePage homePage = new HomePage().verifyPageIsOpen();
        productsPage = homePage.clickProductsLink().verifyPageIsOpen();
        cartPage = productsPage.addProductToCart(testingProductOne)
                .verifyModalIsDisplayed()
                .clickModalContinueShoppingButton()
                .addProductToCart(testingProductTwo)
                .verifyModalIsDisplayed()
                .clickModalViewCartButton()
                .verifyProceedToCheckoutButtonIsDisplayed()
                .clickProceedToCheckoutButton()
                .verifyModalCheckoutTextIsDisplayed()
                .verifyModalRegisterLoginButtonIsDisplayed()
                .verifyModalContinueOnCartButtonIsDisplayed();
    }
}
