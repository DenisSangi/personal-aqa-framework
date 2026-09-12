package elements;


import org.openqa.selenium.By;

public class ButtonElement extends BaseElement<ButtonElement> {

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
