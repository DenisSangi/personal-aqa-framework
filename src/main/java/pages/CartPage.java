package pages;

import elements.ButtonElement;
import elements.TableElement;
import elements.TextElement;
import io.qameta.allure.Step;


public class CartPage {

    private static final String CHECKOUT_MODAL = "div[class='modal-content']";

    private final ButtonElement proceedToCheckoutButton = new ButtonElement("a[class='btn btn-default check_out']");
    private final TableElement cartTable = new TableElement("table[id='cart_info_table']");
    private final TextElement modalCheckoutText = new TextElement(CHECKOUT_MODAL + " h4");
    private final ButtonElement modalRegisterLoginButton = new ButtonElement(CHECKOUT_MODAL + " a[href='/login']");
    private final ButtonElement modalContinueOnCartButton = new ButtonElement(CHECKOUT_MODAL + " button");

    public CartPage verifyPageIsOpen() {
        proceedToCheckoutButton.shouldBeVisible();
        return this;
    }

    public String getCellValueInCartTable(int index, String columnName) {
        return cartTable.getCellValueByRowIndexAndColumnName(index, columnName);
    }

    public CartPage verifyProceedToCheckoutButtonIsDisplayed() {
        proceedToCheckoutButton.shouldBeVisible();
        return this;
    }

    @Step("Click Proceed to checkout button")
    public CartPage clickProceedToCheckoutButton() {
        proceedToCheckoutButton.click();
        return this;
    }

    public CartPage verifyModalCheckoutTextIsDisplayed() {
        modalCheckoutText.shouldBeVisible();
        return this;
    }

    public CartPage verifyModalRegisterLoginButtonIsDisplayed() {
        modalRegisterLoginButton.shouldBeVisible();
        return this;
    }

    public CartPage verifyModalContinueOnCartButtonIsDisplayed() {
        modalContinueOnCartButton.shouldBeVisible();
        return this;
    }

    public CartPage verifyCartTableSize(int expectedSize) {
        cartTable.shouldHaveSize(expectedSize);
        return this;
    }
}


