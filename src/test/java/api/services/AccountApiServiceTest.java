package api.services;

import api.models.AccountFactory;
import api.models.AccountModel;
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
    public void testCreateAccount() {
        accountApiService.createAccount(accountModelValid);
        assertTrue(loginApiService.verifyLogin(accountModelValid.getEmail(), accountModelValid.getPassword()));
    }
}