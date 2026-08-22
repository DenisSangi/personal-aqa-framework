package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ВЛОЖЕННАЯ модель — самый глубокий, 3-й уровень тела {@code productsList}:
 * {@code products[].category.usertype.usertype}.
 *
 * <pre>
 * "usertype": {"usertype": "Women"}
 *  └ ключ объекта  └ ключ строки внутри него
 * </pre>
 *
 * Отдельный класс ради ОДНОГО строкового поля выглядит избыточно, но иначе нельзя:
 * в JSON здесь именно объект, а не строка. Сколько уровней объектов в теле —
 * столько классов в цепочке.
 * <p>
 * Ключ {@code usertype} повторяется на обоих уровнях, поэтому {@code @JsonProperty}
 * стоит и здесь, и в {@link CategoryModel} — по той же причине регистра.
 */
@Data
public class UserTypeModel {

    @JsonProperty("usertype")
    private String userType;
}
