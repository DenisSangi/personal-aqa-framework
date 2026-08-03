package elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

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

    public List<String> getTableHeaders() {
        return getRows().get(0).$$x(".//td").texts();
    }

    public String getCellValueByRowIndexAndColumnName(int rowIndex, String columnName) {
        List<String> columNames = getTableHeaders();
        int columnIndex = columNames.indexOf(columnName);
        if (columnIndex == -1) {
            throw new RuntimeException("Column with name " + columnName + " not found. Available colums in the table: " + columNames);
        }

        return getRows().get(rowIndex + 1).$$x(".//td").get(columnIndex).getText();
    }

    public int getTableSize() {
        return getRows().size() - 1;
    }
}
