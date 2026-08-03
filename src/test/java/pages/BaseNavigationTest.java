package pages;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import core.BaseTest;
import org.testng.annotations.Test;

public class BaseNavigationTest extends BaseTest {

    private final HomePage homePage = new HomePage();

    @Test
    public void baseNavigationTest() {
        Selenide.open(FrameworkConfig.APP_URL);

        homePage.verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen();
    }

}
