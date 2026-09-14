package elements;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;

public class CardListElement extends BaseElement<CardListElement> {

    public CardListElement(String cssSelector) {
        super(cssSelector);
    }

    public CardListElement(By locator) {
        super(locator);
    }

    //region verifiers
    public CardListElement shouldHaveSize(int expectedSize) {
        getRows().shouldHave(size(expectedSize));
        return this;
    }

    public CardListElement shouldHaveRowWithText(String expectedText) {
        waitAndGetElement();
        getRows().findBy(text(expectedText)).should(exist);
        return this;
    }
    //endregion

    //region getters
    private ElementsCollection getRows() {
        waitAndGetElement();
        return element.$$x(".//div[@class='product-image-wrapper']");
    }
    //endregion
}
