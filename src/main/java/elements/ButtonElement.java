package elements;


import com.codeborne.selenide.SelenideElement;

public class ButtonElement extends BaseElement<ButtonElement> {

    public ButtonElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

    public void click() {
        waitAndGetElement();
        element.click();
    }
}
