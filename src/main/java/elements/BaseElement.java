package elements;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BaseElement<T extends BaseElement<T>> {

    protected SelenideElement element;
    private static final String AD_FRAME = "ins[data-vignette-loaded='true'] iframe";

    /**
     * @deprecated leaks Selenide {$/$x locator} into POM
     * Use {@link #BaseElement(By locator)}, {@link #BaseElement(String cssSelector)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    public BaseElement(SelenideElement selenideElement) {
        this.element = selenideElement;
    }

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

    /**
     * @deprecated leaks Selenide {@code Condition} into POM
     * Use {@link #shouldBeVisible()}, {@link #shouldBeClickable()}, {@link #shouldHaveText(String)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings("unchecked")
    public T shouldBe(WebElementCondition... conditions) {
        waitAndGetElement();
        element.shouldBe(conditions);
        return (T) this;
    }

    /**
     * @deprecated leaks Selenide {@code Condition} into POM
     * Use {@link #shouldBeVisible()}, {@link #shouldBeClickable()}, {@link #shouldHaveText(String)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings("unchecked")
    public T shouldHave(WebElementCondition... conditions) {
        waitAndGetElement();
        element.shouldHave(conditions);
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
