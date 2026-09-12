package elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class InputElement extends BaseElement<InputElement> {

    /**
     * @deprecated leaks Selenide {$/$x locator} into POM
     * Use {@link #InputElement(By locator)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    public InputElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

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
