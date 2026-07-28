package elements;

import com.codeborne.selenide.SelenideElement;

public class InputElement extends BaseElement<InputElement> {

    public InputElement(SelenideElement element) {
        super(element);
    }

    public InputElement setValue(String value) {
        waitAndGetElement();
        element.setValue(value);
        return this;
    }

    public String getValue() {
        waitAndGetElement();
        return element.getValue();
    }
}
