package elements;

import core.BaseTest;
import org.testng.annotations.Test;

public class ButtonElementTest extends BaseTest {

    private final TextElement successMessage = new TextElement("div[class='alert-success alert']");

    @Test
    public void testClick() {
        openApp();
        InputElement emailInputField = new InputElement("input[placeholder='Your email address']");
        ButtonElement testingButtonElement = new ButtonElement("button[id='subscribe']");
        String successAlert = "You have been successfully subscribed!";
        emailInputField.setValue("myemail@mail.com");
        testingButtonElement.click();
        successMessage.shouldBeVisible().shouldHaveExactText(successAlert);
    }
}