package core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryOnFailureAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int MAX = 3;

    @Override
    public boolean retry(ITestResult result){
        if (count < MAX) {
            count++;
            return true;
        }
        return false;
    }
}
