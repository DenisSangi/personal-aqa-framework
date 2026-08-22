package api.models;

import lombok.Data;

/**
 * ВЛОЖЕННАЯ модель — ОДИН товар, элемент массива {@code products} внутри
 * {@link ResponseProductContainerModel}.
 *
 * <p>Имя в единственном числе принципиально: класс описывает один элемент, а не коллекцию.
 * Множественность живёт в типе поля-владельца ({@code List<ProductResponseModel>}), а не в имени
 * этого класса.
 *
 * <pre>
 * {"id": 1, "name": "Blue Top", "price": "Rs. 500", "brand": "Polo",
 *  "category": {"usertype": {"usertype": "Women"}, "category": "Tops"}}
 * </pre>
 */
@Data
public class ProductResponseModel {

    private int id;
    private String name;

    /**
     * Строка, а не число: стенд отдаёт {@code "Rs. 500"} — валюта и значение в одном поле.
     * Тип модели обязан повторять тип в JSON, а не желаемый.
     */
    private String price;

    private String brand;

    /** Третий уровень вложенности начинается здесь: {@code category.usertype.usertype}. */
    private CategoryModel category;

}
