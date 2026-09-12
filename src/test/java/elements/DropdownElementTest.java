package elements;

import core.BaseTest;
import org.testng.annotations.Test;
import utils.RandomDataGenerator;

import static org.testng.Assert.*;

public class DropdownElementTest extends BaseTest {

    @Test
    public void testSelectOption() {
        openApp("/login");
        String userName = RandomDataGenerator.generateAlphabetic(10);
        String testingEmail = RandomDataGenerator.generateEmail();

        InputElement nameInputField = new InputElement("input[data-qa='signup-name']");
        InputElement emailInputField = new InputElement("input[data-qa='signup-email']");
        ButtonElement signupButton = new ButtonElement("button[data-qa='signup-button']");
        DropdownElement daysDropDown = new DropdownElement("select[id='days']");

        nameInputField.setValue(userName);
        emailInputField.setValue(testingEmail);
        signupButton.click();

        daysDropDown.selectOption("1");

        assertEquals(daysDropDown.getSelectedOptionText(), "1");
    }
}