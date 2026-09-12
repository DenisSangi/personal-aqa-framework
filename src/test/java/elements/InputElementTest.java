package elements;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;


public class InputElementTest extends BaseTest {

    @Test
    public void testSetValue() {
        openApp("/login");

        InputElement loginEmailInputField = new InputElement("input[data-qa='login-email']");
        String testEmail = "myEmail@gmail.com";
        loginEmailInputField.setValue(testEmail);

        Assert.assertEquals(loginEmailInputField.getValue(), "myEmail@gmail.com");
    }
}