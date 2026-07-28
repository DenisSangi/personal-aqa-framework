package elements;

import com.codeborne.selenide.SelenideElement;

public class DropdownElement extends BaseElement<DropdownElement> {
    public DropdownElement(SelenideElement selenideElement) {
        super(selenideElement);
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
