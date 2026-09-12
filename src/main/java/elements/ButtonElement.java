package elements;


import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class ButtonElement extends BaseElement<ButtonElement> {

    /**
     * @deprecated leaks Selenide {$/$x locator} into POM
     * Use {@link #ButtonElement(By locator)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    public ButtonElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

    public ButtonElement(By locator) {
        super(locator);
    }

    public ButtonElement(String cssSelector) {
        super(cssSelector);
    }

    public void click() {
        waitAndGetElement();
        element.click();
    }
}
