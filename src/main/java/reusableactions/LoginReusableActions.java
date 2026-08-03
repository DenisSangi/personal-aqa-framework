package reusableactions;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import pages.HomePage;

public class LoginReusableActions {

    public HomePage loginAsDefaultUser() {
        Selenide.open(FrameworkConfig.APP_URL);
        return new HomePage().verifyPageIsOpen()
                .clickSignupLoginLink()
                .verifyPageIsOpen()
                .setLoginEmail(FrameworkConfig.APP_USER_EMAIL)
                .setPassword(FrameworkConfig.APP_USER_PASSWORD)
                .clickLoginButton()
                .verifyLoggedUsername(FrameworkConfig.APP_USERNAME);
    }
}
