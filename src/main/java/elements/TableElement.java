package elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;

public class TableElement extends BaseElement<TableElement> {

    /**
     * @deprecated leaks Selenide {$/$x locator} into POM
     * Use {@link #TableElement(String cssSelector)}, {@link #TableElement(By locator)}
     * Removed once every POM is migrated
     */
    @Deprecated(forRemoval = true)
    public TableElement(SelenideElement selenideElement) {
        super(selenideElement);
    }

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

    private int getRowCount() {
        return getRows().size() - 1;
    }

    public String getRowText(int index) {
        return getRows().get(index).getText();
    }
    //endregion

    //region verifiers
    public boolean shouldHaveSize(int expectedSize) {
        return getRowCount() == expectedSize;
    }

    public boolean shouldHaveRowWithText(String text) {
        waitAndGetElement();
        return getRows().texts().contains(shouldHaveText(text));
    }
    //endregion

}
