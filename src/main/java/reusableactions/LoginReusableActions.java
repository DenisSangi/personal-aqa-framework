package reusableactions;

import api.models.AccountModel;
import pages.HomePage;

public class LoginReusableActions {

    public HomePage loginAsValidUser(AccountModel accountModel) {
        return new HomePage().verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen()
                .setLoginEmail(accountModel.getEmail())
                .setPassword(accountModel.getPassword())
                .clickLoginButton()
                .verifyLoggedUsername(accountModel.getName());
    }
}
