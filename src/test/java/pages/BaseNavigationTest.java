package pages;

import core.BaseTest;
import org.testng.annotations.Test;

public class BaseNavigationTest extends BaseTest {

    private final HomePage homePage = new HomePage();

    @Test
    public void baseNavigationTest() {
        openApp();

        homePage.verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen();
    }

}
