package pages;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import reusableactions.LoginReusableActions;

public class BaseNavigationTest {

    private final HomePage homePage = new HomePage();
    private final LoginReusableActions loginReusableActions = new LoginReusableActions();

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
        loginReusableActions.loginAsDefaultUser();
    }

}
