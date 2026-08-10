package core;

import com.codeborne.selenide.Selenide;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotOnFailureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Selenide.screenshot(result.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(Thread.currentThread().getName() + " -> " + result.getName());
    }
}
