package elements;

import com.codeborne.selenide.Selenide;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.RandomDataGenerator;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.*;

public class DropdownElementTest {

    @AfterMethod
    public void quit() {
        Selenide.closeWebDriver();
    }

    @Test
    public void testSelectOption() {
        Selenide.open(FrameworkConfig.APP_URL + "/login");
        String userName = RandomDataGenerator.generateAlphabetic(10);
        String testingEmail = RandomDataGenerator.generateEmail();

        InputElement nameInputField = new InputElement($("input[data-qa='signup-name']"));
        InputElement emailInputField = new InputElement($("input[data-qa='signup-email']"));
        ButtonElement signupButton = new ButtonElement($("button[data-qa='signup-button']"));
        DropdownElement daysDropDown = new DropdownElement($("select[id='days']"));

        nameInputField.setValue(userName);
        emailInputField.setValue(testingEmail);
        signupButton.click();

        daysDropDown.selectOption("1");

        assertEquals(daysDropDown.getSelectedOptionText(), "1");
    }
}