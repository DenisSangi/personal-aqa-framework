package core;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
public class ScreenshotOnFailureListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        log.info("Test started: {}", testResult.getName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        boolean isTest = method.isTestMethod();
        boolean isTestSuccess = testResult.isSuccess();
        boolean isBrowserRun = WebDriverRunner.hasWebDriverStarted();

        if (isTest && !isTestSuccess && isBrowserRun) {
            byte[] data = Selenide.screenshot(OutputType.BYTES);
            InputStream stream = new ByteArrayInputStream(data);
            Allure.addAttachment("Failure screenshot", "image/png", stream, "png");
        }
    }
}
