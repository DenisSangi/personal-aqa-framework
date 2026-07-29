package pages;

import elements.ButtonElement;
import elements.InputElement;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final InputElement loginEmailInput = new InputElement($("input[data-qa='login-email']"));
    private final InputElement passwordInput = new InputElement($("input[data-qa='login-password']"));
    private final ButtonElement loginButton = new ButtonElement($("button[data-qa='login-button']"));


    public LoginPage verifyPageIsOpen() {
        loginEmailInput.shouldBe(clickable);
        return this;
    }

    public LoginPage setEmail(String email) {
        loginEmailInput.setValue(email);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public HomePage clickLoginButton() {
        loginButton.click();
        return new HomePage();
    }
}
