package elements;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;

public class InputElementTest extends BaseTest {

    @Test
    public void testSetValue() {
        Selenide.open(FrameworkConfig.APP_URL + "/login");

        InputElement loginEmailInputField = new InputElement($("input[data-qa='login-email']"));
        String testEmail = "myEmail@gmail.com";
        loginEmailInputField.setValue(testEmail);

        Assert.assertEquals(loginEmailInputField.getValue(), "myEmail@gmail.com");
    }
}