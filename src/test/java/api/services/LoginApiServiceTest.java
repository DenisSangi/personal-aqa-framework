package api.services;

import core.TestAccountFixture;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.RandomDataGenerator;

import static org.testng.Assert.*;

public class LoginApiServiceTest {

    private final LoginApiService loginApiService = new LoginApiService();

    @DataProvider(name = "credentialsDataSet")
    public Object[][] credentialsTestData() {
        return new Object[][] {
                {TestAccountFixture.VALID_ACCOUNT_MODEL.getEmail(), TestAccountFixture.VALID_ACCOUNT_MODEL.getPassword(), true},
                {RandomDataGenerator.generateEmail(), TestAccountFixture.VALID_ACCOUNT_MODEL.getPassword(), false},
                {TestAccountFixture.VALID_ACCOUNT_MODEL.getEmail(), RandomDataGenerator.generateAlphanumeric(10), false},
                {RandomDataGenerator.generateEmail(), RandomDataGenerator.generateAlphanumeric(10), false},
        };
    }

    @Test(dataProvider = "credentialsDataSet")
    public void loginApiTest(String email, String password, boolean expected) {
        assertEquals(loginApiService.verifyLogin(email, password), expected);
    }

    @Test
    public void exceptionTest() {
        assertThrows(RuntimeException.class, () -> loginApiService.verifyLogin(null, TestAccountFixture.VALID_ACCOUNT_MODEL.getPassword()));
        assertThrows(RuntimeException.class, () -> loginApiService.verifyLogin(TestAccountFixture.VALID_ACCOUNT_MODEL.getEmail(), ""));
    }
}
