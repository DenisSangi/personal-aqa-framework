package pages;

import elements.ButtonElement;
import elements.TableElement;
import elements.TextElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CartPage {

    private static final String CHECKOUT_MODAL = "div[class='modal-content']";

    private final ButtonElement proceedToCheckoutButton = new ButtonElement($("a[class='btn btn-default check_out']"));
    private final TableElement cartTable = new TableElement($("table[id='cart_info_table']"));
    private final TextElement modalCheckoutText = new TextElement($(CHECKOUT_MODAL + " h4"));
    private final ButtonElement modalRegisterLoginButton = new ButtonElement($(CHECKOUT_MODAL + " a[href='/login']"));
    private final ButtonElement modalContinueOnCartButton = new ButtonElement($(CHECKOUT_MODAL + " button"));

    public CartPage verifyPageIsOpen() {
        proceedToCheckoutButton.shouldBe(visible);
        return this;
    }

    public String getCellValueInCartTable(int index, String columnName) {
        return cartTable.getCellValueByRowIndexAndColumnName(index, columnName);
    }

    public CartPage verifyProceedToCheckoutButtonIsDisplayed() {
        proceedToCheckoutButton.shouldBe(visible);
        return this;
    }

    public CartPage clickProceedToCheckoutButton() {
        proceedToCheckoutButton.click();
        return this;
    }

    public CartPage verifyModalCheckoutTextIsDisplayed() {
        modalCheckoutText.shouldBe(visible);
        return this;
    }

    public CartPage verifyModalRegisterLoginButtonIsDisplayed() {
        modalRegisterLoginButton.shouldBe(visible);
        return this;
    }

    public CartPage verifyModalContinueOnCartButtonIsDisplayed() {
        modalContinueOnCartButton.shouldBe(visible);
        return this;
    }

    public int getCartTableSize() {
        return cartTable.getTableSize();
    }
}


