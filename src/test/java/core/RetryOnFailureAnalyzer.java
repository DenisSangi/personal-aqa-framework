package core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryOnFailureAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int MAX = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (isTransient(result.getThrowable()) && count < MAX) {
            count++;
            log.warn("Test {} retry attempt {} :", result.getName(), count, result.getThrowable());
            return true;
        }
        return false;
    }

    private boolean isTransient(Throwable throwable) {
        while (throwable != null) {
            if (throwable instanceof TimeoutException || throwable instanceof StaleElementReferenceException) {
                return true;
            }
            if (throwable instanceof ElementClickInterceptedException && throwable.getMessage() != null && throwable.getMessage().contains("Other element would receive the click: <iframe")) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }
}
