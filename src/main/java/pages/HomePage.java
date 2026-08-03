package pages;

import elements.ButtonElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class HomePage {

    private final ButtonElement signupLoginLink = new ButtonElement($("a[href='/login']"));
    private final ButtonElement productsLink = new ButtonElement($("a[href='/products']"));
    private final ButtonElement loggedUsername = new ButtonElement($x("//a[contains(text(), 'Logged in as')]"));


    public HomePage verifyPageIsOpen() {
        signupLoginLink.shouldBe(clickable);
        return this;
    }

    public HomePage verifyLoggedUsername(String username) {
        loggedUsername.shouldBe(visible);
        loggedUsername.shouldHave(text(username));
        return this;
    }

    public LoginPage clickSignupLoginLink() {
        signupLoginLink.click();
        return new LoginPage();
    }

    public ProductsPage clickProductsLink() {
        productsLink.click();
        return new ProductsPage();
    }
}
