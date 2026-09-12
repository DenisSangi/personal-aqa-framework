package elements;

import org.openqa.selenium.By;

public class TextElement extends BaseElement<TextElement> {

    public TextElement(By locator) {
        super(locator);
    }

    public TextElement(String cssSelector) {
        super(cssSelector);
    }

    public TextElement shouldHaveExactText(String expectedText) {
        waitAndGetElement();
        element.getText().equals(expectedText);
        return this;
    }
}
