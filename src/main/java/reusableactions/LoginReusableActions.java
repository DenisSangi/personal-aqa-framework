package reusableactions;

import api.models.AccountModel;
import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import pages.HomePage;

public class LoginReusableActions {

    public HomePage loginAsValidUser(AccountModel accountModel) {
        Selenide.open(FrameworkConfig.APP_URL);
        return new HomePage().verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen()
                .setLoginEmail(accountModel.getEmail())
                .setPassword(accountModel.getPassword())
                .clickLoginButton()
                .verifyLoggedUsername(accountModel.getName());
    }
}
