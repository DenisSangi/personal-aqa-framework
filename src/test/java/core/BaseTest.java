package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import static config.FrameworkConfig.DEFAULT_TIMEOUT;

@Listeners(ScreenshotOnFailureListener.class)
public class BaseTest {

    static {
        Configuration.timeout = DEFAULT_TIMEOUT;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
