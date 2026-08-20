package core;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class AllureFilterListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        RestAssured.filters(new AllureRestAssured());
    }
}
