package tests;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import core.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import reusableactions.LoginReusableActions;
import utils.RandomDataGenerator;

public class LoginTest extends BaseTest {

    private LoginPage loginPage = new LoginPage();
    private final HomePage homePage = new HomePage();
    private final LoginReusableActions loginReusableActions = new LoginReusableActions();
    private final String failedLoginErrorMessage = "Your email or password is incorrect!";
    private final String failedSignupErrorMessage = "Email Address already exist!";


    @BeforeMethod
    public void setup() {
        Selenide.open(FrameworkConfig.APP_URL);
    }

    @Test
    public void successfulLoginTest() {
        loginReusableActions.loginAsDefaultUser();
    }

    @Test
    public void incorrectEmailLoginTest() {
        loginPage = homePage.verifyPageIsOpen().clickSignupLoginLink();
        loginPage.verifyPageIsOpen()
                .setLoginEmail(RandomDataGenerator.generateEmail())
                .setPassword(FrameworkConfig.APP_USER_PASSWORD)
                .clickLoginButtonWithFail()
                .verifyFailedLoginErrorMessage(failedLoginErrorMessage);
    }

    @Test
    public void incorrectPasswordLoginTest() {
        loginPage = homePage.verifyPageIsOpen().clickSignupLoginLink();
        loginPage.verifyPageIsOpen()
                .setLoginEmail(FrameworkConfig.APP_USER_EMAIL)
                .setPassword(RandomDataGenerator.generateAlphanumeric(10))
                .clickLoginButtonWithFail()
                .verifyFailedLoginErrorMessage(failedLoginErrorMessage);
    }

    @Test
    public void incorrectSignupWithExistedEmail() {
        loginPage = homePage.verifyPageIsOpen().clickSignupLoginLink();
        loginPage.verifyPageIsOpen()
                .setSignupEmail(FrameworkConfig.APP_USER_EMAIL)
                .setSignupName(RandomDataGenerator.generateAlphanumeric(10))
                .clickSignupButtonWithFail()
                .verifyFailedSignupErrorMessage(failedSignupErrorMessage);
    }
}
