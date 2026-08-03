package pages;

import elements.ButtonElement;
import elements.CardListElement;
import elements.InputElement;
import elements.TextElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProductsPage {

    private static final String PRODUCT_CARD = "//div[@class='productinfo text-center'][.//p[text()='%s']]//a[@class='btn btn-default add-to-cart']";
    private static final String ADDED_TO_CART_MODAL = "div[class='modal-content']";

    private final InputElement searchInputField = new InputElement($("input[id='search_product']"));
    private final TextElement modalAddedText = new TextElement($(ADDED_TO_CART_MODAL + " h4"));
    private final ButtonElement submitSearchButton = new ButtonElement($("button[id='submit_search']"));
    private final ButtonElement modalViewCartButton = new ButtonElement($(ADDED_TO_CART_MODAL + " a[href='/view_cart']"));
    private final ButtonElement modalContinueShoppingButton = new ButtonElement($(ADDED_TO_CART_MODAL + " button"));
    private final CardListElement searchResults = new CardListElement($("div[class='features_items']"));


    public ProductsPage verifyPageIsOpen() {
        searchInputField.shouldBe(visible);
        return this;
    }

    public ProductsPage setValueInSearchInputField(String value) {
        searchInputField.setValue(value);
        return this;
    }

    public ProductsPage clickSubmitSearchButton() {
        submitSearchButton.click();
        return this;
    }

    public ProductsPage addProductToCart(String cardName) {
        new ButtonElement($x(PRODUCT_CARD.formatted(cardName))).shouldBe(clickable).click();
        return this;
    }

    public ProductsPage verifyModalIsDisplayed() {
        modalAddedText.shouldBe(visible);
        return this;
    }

    public CartPage clickModalViewCartButton() {
        modalViewCartButton.click();
        return new CartPage();
    }

    public int getSearchResultSize() {
        return searchResults.getRows().size();
    }

    public boolean isSearchResultsContainsProductName(String productName) {
        return searchResults.getRows().texts().stream().anyMatch(t -> t.contains(productName));
    }

    public ProductsPage clickModalContinueShoppingButton() {
        modalContinueShoppingButton.click();
        return this;
    }

}
