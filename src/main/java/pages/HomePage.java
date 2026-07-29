package pages;

import elements.ButtonElement;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    private final ButtonElement signupLoginLink = new ButtonElement($("a[href='/login']"));


    public HomePage verifyPageIsOpen() {
        signupLoginLink.shouldBe(clickable);
        return this;
    }

    public LoginPage clickSignupLoginLink() {
        signupLoginLink.click();
        return new LoginPage();
    }
}
