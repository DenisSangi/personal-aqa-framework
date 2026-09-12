package elements;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;

public class CardListElement extends BaseElement<CardListElement> {

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
