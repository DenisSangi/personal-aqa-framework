package api.services;

import api.models.AccountFactory;
import api.models.AccountModel;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AccountApiServiceTest {

    AccountApiService accountApiService = new AccountApiService();
    LoginApiService loginApiService = new LoginApiService();

    private final AccountModel accountModelValid = AccountFactory.createValidAccountModel();

    @AfterMethod(alwaysRun = true)
    public void deleteTestingData() {
        if (loginApiService.verifyLogin(accountModelValid.getEmail(), accountModelValid.getPassword())) {
            accountApiService.deleteAccount(accountModelValid.getEmail(), accountModelValid.getPassword());
            assertFalse(loginApiService.verifyLogin(accountModelValid.getEmail(), accountModelValid.getPassword()));
        }
    }

    @Test
    public void createAccountTest() {
        accountApiService.createAccount(accountModelValid);
        assertTrue(loginApiService.verifyLogin(accountModelValid.getEmail(), accountModelValid.getPassword()));
    }

    @Test
    public void getUserFirstnameByEmailTest() {
        String actualName = accountApiService.getUserFirstnameByEmail(FrameworkConfig.APP_USER_EMAIL);
        String expectedName = "Den";
        assertEquals(actualName, expectedName);
    }

    @Test
    public void updateAccountTest() {
        String newFirstName = "NewName";
        accountApiService.createAccount(accountModelValid);
        accountApiService.updateAccount(accountModelValid.toBuilder().firstName(newFirstName).build());
        assertEquals(accountApiService.getUserFirstnameByEmail(accountModelValid.getEmail()), newFirstName);
    }
}