package elements;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;

public class TableElement extends BaseElement<TableElement> {

    public TableElement(String cssSelector) {
        super(cssSelector);
    }

    public TableElement(By locator) {
        super(locator);
    }

    //region getters
    private ElementsCollection getRows() {
        waitAndGetElement();
        return element.$$x(".//tr");
    }

    private ElementsCollection getTableRowsWithoutHeader() {
        waitAndGetElement();
        return element.$$x(".//tbody/tr");
    }

    private List<String> getTableHeaders() {
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

    public String getRowText(int index) {
        return getRows().get(index).getText();
    }
    //endregion

    //region verifiers
    public TableElement shouldHaveSize(int expectedSize) {
        getTableRowsWithoutHeader().shouldHave(size(expectedSize));
        return this;
    }

    public boolean shouldHaveRowWithText(String text) {
        waitAndGetElement();
        return getTableRowsWithoutHeader().texts().contains(shouldHaveText(text));
    }
    //endregion

}
