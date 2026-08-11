package elements;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import core.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductsPage;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TableElementTest extends BaseTest {
    private final HomePage homePage = new HomePage();

    @Test()
    public void testGetRows() {
        Selenide.open(FrameworkConfig.APP_URL);
        ProductsPage productsPage = homePage.verifyPageIsOpen().clickProductsLink().verifyPageIsOpen();
        TableElement cartTable = new TableElement($("table[id='cart_info_table'] > tbody"));
        String expectedText = "Blue Top";
        productsPage.addProductToCart(expectedText)
                .verifyModalIsDisplayed()
                .clickModalContinueShoppingButton()
                .addProductToCart("Summer White Top")
                .verifyModalIsDisplayed();
        Selenide.open(FrameworkConfig.APP_URL + "/view_cart");
        assertEquals(cartTable.getRows().size(), 2);
        assertTrue(cartTable.getRows().get(0).text().contains(expectedText));
    }

}