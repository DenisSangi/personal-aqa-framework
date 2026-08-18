package core;

import com.codeborne.selenide.Selenide;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class ScreenshotOnFailureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Selenide.screenshot(result.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test started: {}", result.getName());
    }
}
