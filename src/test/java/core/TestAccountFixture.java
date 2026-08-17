package core;

import api.models.AccountFactory;
import api.models.AccountModel;
import api.services.AccountApiService;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class TestAccountFixture implements ISuiteListener {

    private final AccountApiService accountApiService = new AccountApiService();

    public static final AccountModel VALID_ACCOUNT_MODEL = AccountFactory.createValidAccountModel();

    @Override
    public void onStart(ISuite suite) {
        accountApiService.createAccount(VALID_ACCOUNT_MODEL);
    }

    @Override
    public void onFinish(ISuite suite) {
        accountApiService.deleteAccount(VALID_ACCOUNT_MODEL.getEmail(), VALID_ACCOUNT_MODEL.getPassword());
    }

}
