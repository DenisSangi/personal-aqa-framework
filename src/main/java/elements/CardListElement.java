package elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class CardListElement extends BaseElement<CardListElement> {

    /**
     * @deprecated leaks Selenide {$/$x locator} into POM
     * Use {@link #CardListElement(String cssSelector)}, {@link #CardListElement(By locator)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    public CardListElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

    public CardListElement(String cssSelector) {
        super(cssSelector);
    }

    public CardListElement(By locator) {
        super(locator);
    }

    //region verifiers
    public boolean shouldHaveSize(int expectedSize) {
        return getRowCount() == expectedSize;
    }

    public boolean shouldHaveRowWithText(String text) {
        waitAndGetElement();
        return getRows().texts().contains(shouldHaveText(text));
    }
    //endregion

    //region getters
    private ElementsCollection getRows() {
        waitAndGetElement();
        return element.$$x(".//div[@class='product-image-wrapper']");
    }

    private int getRowCount() {
        return getRows().size();
    }
    //endregion
}
