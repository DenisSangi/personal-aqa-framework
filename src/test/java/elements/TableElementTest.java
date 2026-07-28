package elements;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TableElementTest {

    @AfterMethod
    public void quit() {
        Selenide.closeWebDriver();
    }

    @Test
    public void testGetRows() {
        Selenide.open(FrameworkConfig.APP_URL + "/login");
        InputElement emailInputField = new InputElement($("input[data-qa='login-email']"));
        InputElement passwordInputField = new InputElement($("input[data-qa='login-password']"));
        ButtonElement loginButton = new ButtonElement($("button[data-qa='login-button']"));
        TableElement cartTable = new TableElement($("table[id='cart_info_table'] > tbody"));
        String expectedText = "Blue Top";

        emailInputField.setValue(FrameworkConfig.APP_USER_EMAIL);
        passwordInputField.setValue(FrameworkConfig.APP_USER_PASSWORD);
        loginButton.click();
        Selenide.webdriver().shouldHave(url(FrameworkConfig.APP_URL + "/"));
        Selenide.open(FrameworkConfig.APP_URL + "/view_cart");
        assertEquals(cartTable.getRows().size(), 2);
        assertTrue(cartTable.getRows().get(0).text().contains(expectedText));
    }

}