package elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class CardListElement extends BaseElement<CardListElement> {

    public CardListElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

    public ElementsCollection getRows() {
        waitAndGetElement();
        return element.$$x(".//div[@class='product-image-wrapper']");
    }


}
