package api.services;

import config.FrameworkConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.RandomDataGenerator;

import static org.testng.Assert.*;

public class LoginApiServiceTest {

    private final LoginApiService loginApiService = new LoginApiService();

    @DataProvider(name = "credentialsDataSet")
    public Object[][] credentialsTestData() {
        return new Object[][] {
                {FrameworkConfig.APP_USER_EMAIL, FrameworkConfig.APP_USER_PASSWORD, true},
                {RandomDataGenerator.generateEmail(), FrameworkConfig.APP_USER_PASSWORD, false},
                {FrameworkConfig.APP_USER_EMAIL, RandomDataGenerator.generateAlphanumeric(10), false},
                {RandomDataGenerator.generateEmail(), RandomDataGenerator.generateAlphanumeric(10), false},
        };
    }

    @Test(dataProvider = "credentialsDataSet")
    public void loginApiTest(String email, String password, boolean expected) {
        assertEquals(loginApiService.verifyLogin(email, password), expected);
    }

    @Test
    public void exceptionTest() {
        assertThrows(RuntimeException.class, () -> loginApiService.verifyLogin(null, FrameworkConfig.APP_USER_PASSWORD));
        assertThrows(RuntimeException.class, () -> loginApiService.verifyLogin(FrameworkConfig.APP_USER_EMAIL, ""));
    }
}
