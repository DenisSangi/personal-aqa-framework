package pages;

import elements.InputElement;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final InputElement loginEmailInput = new InputElement($("input[data-qa='login-email']"));


    public LoginPage verifyPageIsOpen() {
        loginEmailInput.shouldBe(clickable);
        return this;
    }
}
