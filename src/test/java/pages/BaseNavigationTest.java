package pages;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import core.BaseTest;
import org.testng.annotations.Test;
import reusableactions.LoginReusableActions;

public class BaseNavigationTest extends BaseTest {

    private final HomePage homePage = new HomePage();
    private final LoginReusableActions loginReusableActions = new LoginReusableActions();

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
