package elements;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;

public abstract class BaseElement<T extends BaseElement<T>> {

    protected SelenideElement element;

    public BaseElement(SelenideElement selenideElement) {
        this.element = selenideElement;
    }

    @SuppressWarnings("unchecked")
    public T waitAndGetElement() {
        Selenide.executeJavaScript("document.querySelectorAll('ins[data-vignette-loaded=\"true\"]').forEach(e => e.remove());");
        element.should(exist);
        element.scrollIntoView(true);
        element.shouldBe(visible);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldBe(WebElementCondition... conditions) {
        waitAndGetElement();
        element.shouldBe(conditions);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldHave(WebElementCondition... conditions) {
        waitAndGetElement();
        element.shouldHave(conditions);
        return (T) this;
    }

    public String getText() {
        waitAndGetElement();
        return element.getText();
    }
}
