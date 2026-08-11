package core;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryOnFailureAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int MAX = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (isTransient(result.getThrowable()) && count < MAX) {
            count++;
            System.out.println("Test " + result.getName() + " retry attempt: " + count + " due to " + result.getThrowable());
            return true;
        }
        return false;
    }

    private boolean isTransient(Throwable throwable) {
        while (throwable != null) {
            if (throwable instanceof TimeoutException || throwable instanceof StaleElementReferenceException) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }
}
