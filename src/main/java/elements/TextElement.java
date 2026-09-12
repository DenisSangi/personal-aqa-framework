package elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class TextElement extends BaseElement<TextElement> {

    /**
     * @deprecated leaks Selenide {$/$x locator} into POM
     * Use {@link #TextElement(By locator)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    public TextElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

    public TextElement(By locator) {
        super(locator);
    }

    public TextElement(String cssSelector) {
        super(cssSelector);
    }
}
