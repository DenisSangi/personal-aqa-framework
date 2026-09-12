package pages;

import elements.ButtonElement;
import elements.CardListElement;
import elements.InputElement;
import elements.TextElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;


public class ProductsPage {

    private static final String PRODUCT_CARD = "//div[@class='productinfo text-center'][.//p[text()='%s']]//a[@class='btn btn-default add-to-cart']";
    private static final String ADDED_TO_CART_MODAL = "div[class='modal-content']";

    private final InputElement searchInputField = new InputElement("input[id='search_product']");
    private final TextElement modalAddedText = new TextElement(ADDED_TO_CART_MODAL + " h4");
    private final ButtonElement submitSearchButton = new ButtonElement("button[id='submit_search']");
    private final ButtonElement modalViewCartButton = new ButtonElement(ADDED_TO_CART_MODAL + " a[href='/view_cart']");
    private final ButtonElement modalContinueShoppingButton = new ButtonElement(ADDED_TO_CART_MODAL + " button");
    private final CardListElement searchResults = new CardListElement("div[class='features_items']");


    public ProductsPage verifyPageIsOpen() {
        searchInputField.shouldBeVisible();
        return this;
    }

    @Step("Enter value in to the Search input {value}")
    public ProductsPage setValueInSearchInputField(String value) {
        searchInputField.setValue(value);
        return this;
    }

    @Step("Click Submit search button")
    public ProductsPage clickSubmitSearchButton() {
        submitSearchButton.click();
        return this;
    }

    @Step("Add product to cart: {cardName}")
    public ProductsPage addProductToCart(String cardName) {
        new ButtonElement(By.xpath(PRODUCT_CARD.formatted(cardName))).shouldBeClickable().click();
        return this;
    }

    public ProductsPage verifyModalIsDisplayed() {
        modalAddedText.shouldBeVisible();
        return this;
    }

    @Step("Click Modal view Cart button")
    public CartPage clickModalViewCartButton() {
        modalViewCartButton.click();
        return new CartPage();
    }

    public ProductsPage verifySearchResultsSize(int expectedSize) {
        searchResults.shouldHaveSize(expectedSize);
        return this;
    }

    public ProductsPage verifySearchResultsContainsProduct(String productName) {
        searchResults.shouldHaveRowWithText(productName);
        return this;
    }

    @Step("Click Continue shopping button")
    public ProductsPage clickModalContinueShoppingButton() {
        modalContinueShoppingButton.click();
        return this;
    }

}
