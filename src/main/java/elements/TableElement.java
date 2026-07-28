package elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class TableElement extends BaseElement<TableElement> {

    public TableElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

    public ElementsCollection getRows() {
        waitAndGetElement();
        return element.$$x(".//tr");
    }

    public boolean containsText(String text) {
        waitAndGetElement();
        return element.getText().contains(text);
    }
}
