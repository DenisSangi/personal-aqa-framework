package api.services;

import api.models.AccountModel;
import api.models.AccountResponseModel;
import core.TestAccountFixture;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.RandomDataGenerator;

import static api.models.AccountFactory.createValidAccountModel;
import static org.testng.Assert.*;

public class AccountApiServiceTest {

    AccountApiService accountApiService = new AccountApiService();
    LoginApiService loginApiService = new LoginApiService();

    private final AccountModel accountModelValid = createValidAccountModel();

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
        String actualName = accountApiService.getUserFirstnameByEmail(TestAccountFixture.VALID_ACCOUNT_MODEL.getEmail());
        String expectedName = TestAccountFixture.VALID_ACCOUNT_MODEL.getFirstName();
        assertEquals(actualName, expectedName);
    }

    @Test
    public void updateAccountTest() {
        String newFirstName = "NewName";
        accountApiService.createAccount(accountModelValid);
        accountApiService.updateAccount(accountModelValid.toBuilder().firstName(newFirstName).build());
        assertEquals(accountApiService.getUserFirstnameByEmail(accountModelValid.getEmail()), newFirstName);
    }

    @Test
    public void getAccountDetailsByEmailTest() {
        accountApiService.createAccount(accountModelValid);
        AccountResponseModel accountResponseModel = accountApiService.getAccountDetailsByEmail(accountModelValid.getEmail());
        assertEquals(accountResponseModel.getBirthDay(), accountModelValid.getBirthDate());
        assertEquals(accountResponseModel.getBirthMonth(), accountModelValid.getBirthMonth());
        assertEquals(accountResponseModel.getBirthYear(), accountModelValid.getBirthYear());
        assertEquals(accountResponseModel.getFirstName(), accountModelValid.getFirstName());
        assertEquals(accountResponseModel.getLastName(), accountModelValid.getLastName());
    }

    @Test
    public void getAccountDetailsByEmailExceptionTest() {
        RuntimeException exception = expectThrows(RuntimeException.class, () -> accountApiService.getAccountDetailsByEmail(RandomDataGenerator.generateEmail()));
        assertTrue(exception.getMessage().contains("Unable to get account due to 404"));
    }
}