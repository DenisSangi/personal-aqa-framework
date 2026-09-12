package elements;

import org.openqa.selenium.By;

public class InputElement extends BaseElement<InputElement> {

    public InputElement(By locator) {
        super(locator);
    }

    public InputElement(String cssSelector) {
        super(cssSelector);
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
