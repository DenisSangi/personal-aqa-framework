package elements;

import org.openqa.selenium.By;

public class DropdownElement extends BaseElement<DropdownElement> {

    public DropdownElement(String cssSelector) {
        super(cssSelector);
    }

    public DropdownElement(By locator) {
        super(locator);
    }

    public void selectOption(String option) {
        waitAndGetElement();
        element.selectOption(option);
    }

    public String getSelectedOptionText() {
        waitAndGetElement();
        return element.getSelectedOptionText();
    }
}
