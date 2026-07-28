package elements;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.*;

public class ButtonElementTest {

    @AfterMethod
    public void quit() {
        Selenide.closeWebDriver();
    }

    @Test
    public void testClick() {
        Selenide.open(FrameworkConfig.APP_URL);
        InputElement emailInputField = new InputElement($("input[placeholder='Your email address']"));
        ButtonElement testingButtonElement = new ButtonElement($("button[id='subscribe']"));
        String successAlert = "You have been successfully subscribed!";
        emailInputField.setValue("myemail@mail.com");
        testingButtonElement.click();
        assertEquals($("div[class='alert-success alert']").text(), successAlert);
    }
}