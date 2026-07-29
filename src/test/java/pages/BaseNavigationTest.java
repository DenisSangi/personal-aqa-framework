package pages;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class BaseNavigationTest {

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    public void baseNavigationTest() {
        Selenide.open(FrameworkConfig.APP_URL);

        new HomePage().verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen();
    }

}
