package pages;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class BaseNavigationTest {

    private final HomePage homePage = new HomePage();

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    public void baseNavigationTest() {
        Selenide.open(FrameworkConfig.APP_URL);

        homePage.verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen();
    }

    @Test
    public void loginFlowTest() {
        Selenide.open(FrameworkConfig.APP_URL);

        homePage.verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen()
                .setEmail(FrameworkConfig.APP_USER_EMAIL)
                .setPassword(FrameworkConfig.APP_USER_PASSWORD)
                .clickLoginButton()
                .verifyLoggedUsername(FrameworkConfig.APP_USERNAME);
    }

}
