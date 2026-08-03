package constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum TableHeaders {

    // Cart Table
    CT_ITEM("Item", TableName.CART_TABLE),
    CT_DESCRIPTION("Description", TableName.CART_TABLE),
    CT_PRICE("Price", TableName.CART_TABLE),
    CT_QUANTITY("Quantity", TableName.CART_TABLE),
    CT_TOTAL("Total", TableName.CART_TABLE),
    CT_EMPTY_COLUMN_NAME("", TableName.CART_TABLE);


    @Getter
    private final String headerName;
    private final TableName tableName;

    public enum TableName {
        CART_TABLE;
    }

    public static List<String> getHeadersByType(TableName tableName) {
        return Stream.of(TableHeaders.values())
                .filter(header -> header.tableName == tableName)
                .map(TableHeaders::getHeaderName)
                .collect(Collectors.toList());
    }
}
