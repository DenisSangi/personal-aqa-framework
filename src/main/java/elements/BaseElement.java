package elements;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BaseElement<T extends BaseElement<T>> {

    protected SelenideElement element;
    private static final String AD_FRAME = "ins[data-vignette-loaded='true'] iframe";


    public BaseElement(By locator) {
        this.element = Selenide.$(locator);
    }

    public BaseElement(String cssSelector) {
        this(By.cssSelector(cssSelector));
    }

    @SuppressWarnings("unchecked")
    public T waitAndGetElement() {
        //Selenide.executeJavaScript("document.querySelectorAll('ins[data-vignette-loaded=\"true\"]').forEach(e => e.remove());");
        dismissAdIfPresent();
        element.should(exist);
        element.scrollIntoView(true);
        element.shouldBe(visible);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldBeVisible() {
        waitAndGetElement();
        element.shouldBe(visible);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldBeClickable() {
        waitAndGetElement();
        element.shouldBe(clickable);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldHaveText(String expectedText) {
        waitAndGetElement();
        element.shouldHave(text(expectedText));
        return (T) this;
    }

    public String getText() {
        waitAndGetElement();
        return element.getText();
    }

    private void dismissAdIfPresent() {
        if ($$(AD_FRAME).get(0).isDisplayed()) {
            try {
                Selenide.switchTo().frame($$(AD_FRAME).get(0));
                $("div#dismiss-button").click();
            } finally {
                Selenide.switchTo().defaultContent();
            }
        }
    }
}
