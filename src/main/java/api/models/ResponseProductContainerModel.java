package api.models;

import lombok.Data;

import java.util.List;

/**
 * КОНВЕРТ ответа {@code GET /api/productsList} — описывает всё тело целиком.
 * Полная карта уровней — в {@code package-info.java}.
 *
 * <pre>
 * {"responseCode": 200, "products": [ {...}, {...}, ... 34 объекта ]}
 * </pre>
 *
 * Отдельный класс, а не переиспользование {@link ResponseAccountContainerModel}: конверт
 * описывает контракт СВОЕГО эндпоинта. Общий конверт с полями {@code user} и {@code products}
 * сразу означал бы, что одно из них всегда {@code null}, и ответ одного эндпоинта молча
 * разобрался бы моделью другого.
 */
@Data
public class ResponseProductContainerModel {

    private int responseCode;

    /**
     * {@code products} в JSON — МАССИВ объектов, поэтому здесь {@code List}, а не одиночная модель.
     * Объявить полем типа {@link ProductResponseModel} нельзя: Jackson падает с
     * {@code MismatchedInputException: Cannot deserialize value of type ... from Array value}.
     * <p>
     * Элемент списка — ЦЕЛЫЙ товар со всеми своими полями, а не одно поле:
     * {@code get(0)} возвращает первый товар с его {@code id}, {@code name}, {@code price},
     * {@code brand} и {@code category} сразу.
     */
    private List<ProductResponseModel> products;

    /** В успешном ответе отсутствует; описан ради ветки ошибки — см. {@code package-info.java}. */
    private String message;
}
