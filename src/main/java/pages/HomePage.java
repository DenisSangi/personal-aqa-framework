package pages;

import elements.ButtonElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage {

    private final ButtonElement signupLoginLink = new ButtonElement("a[href='/login']");
    private final ButtonElement productsLink = new ButtonElement("a[href='/products']");
    private final ButtonElement loggedUsername = new ButtonElement(By.xpath("//a[contains(text(), 'Logged in as')]"));


    public HomePage verifyPageIsOpen() {
        signupLoginLink.shouldBeClickable();
        return this;
    }

    public HomePage verifyLoggedUsername(String username) {
        loggedUsername.shouldBeVisible();
        loggedUsername.shouldHaveText(username);
        return this;
    }

    @Step("Click Signup/Login link")
    public LoginPage clickSignupLoginLink() {
        signupLoginLink.click();
        return new LoginPage();
    }

    @Step("Go to Products page")
    public ProductsPage clickProductsLink() {
        productsLink.click();
        return new ProductsPage();
    }
}
