package elements;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;

public class InputElementTest {

    @AfterMethod
    public void quit() {
        Selenide.closeWebDriver();
    }

    @Test
    public void testSetValue() {
        Selenide.open(FrameworkConfig.APP_URL + "/login");

        InputElement loginEmailInputField = new InputElement($("input[data-qa='login-email']"));
        String testEmail = "myEmail@gmail.com";
        loginEmailInputField.setValue(testEmail);

        Assert.assertEquals(loginEmailInputField.getValue(), "myEmail@gmail.com");
    }
}