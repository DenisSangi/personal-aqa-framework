package pages;

import elements.ButtonElement;
import elements.InputElement;
import elements.TextElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final InputElement loginEmailInput = new InputElement($("input[data-qa='login-email']"));
    private final InputElement passwordInput = new InputElement($("input[data-qa='login-password']"));
    private final InputElement signupNameInput = new InputElement($("input[data-qa='signup-name']"));
    private final InputElement signupEmailInput = new InputElement($("input[data-qa='signup-email']"));
    private final ButtonElement loginButton = new ButtonElement($("button[data-qa='login-button']"));
    private final ButtonElement signupButton = new ButtonElement($("button[data-qa='signup-button']"));
    private final TextElement failedLoginErrorMessageText = new TextElement($("form[action='/login'] > p"));
    private final TextElement failedSignupErrorMessageText = new TextElement($("form[action='/signup'] > p"));


    public LoginPage verifyPageIsOpen() {
        loginEmailInput.shouldBe(clickable);
        return this;
    }

    public LoginPage setLoginEmail(String email) {
        loginEmailInput.setValue(email);
        return this;
    }

    public LoginPage setSignupEmail(String email) {
        signupEmailInput.setValue(email);
        return this;
    }

    public LoginPage setSignupName(String name) {
        signupNameInput.setValue(name);
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

    public LoginPage clickLoginButtonWithFail() {
        loginButton.click();
        return this;
    }

    public LoginPage clickSignupButtonWithFail() {
        signupButton.click();
        return this;
    }

    public LoginPage verifyFailedLoginErrorMessage(String expectedText) {
        failedLoginErrorMessageText.shouldBe(visible).shouldHave(text(expectedText));
        return this;
    }

    public LoginPage verifyFailedSignupErrorMessage(String expectedText) {
        failedSignupErrorMessageText.shouldBe(visible).shouldHave(text(expectedText));
        return this;
    }
}
