package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ВЛОЖЕННАЯ модель — объект {@code category} внутри {@link ProductResponseModel} (2-й уровень).
 *
 * <pre>
 * "category": {"usertype": {"usertype": "Women"}, "category": "Tops"}
 * </pre>
 *
 * <p>Ловушка этого куска тела: слово {@code category} встречается на ДВУХ уровнях — снаружи как
 * имя объекта, внутри как имя строки. Одноимённое поле {@code category} ниже — это внутренняя
 * строка, а не ссылка на саму себя.
 */
@Data
public class CategoryModel {

    /**
     * {@code @JsonProperty} нужен из-за РЕГИСТРА: в JSON ключ {@code usertype} сплошным нижним,
     * поле Java — {@code userType}. Jackson сверяет имена точно и camelCase со сплошным
     * не сопоставит; без аннотации — {@code UnrecognizedPropertyException}.
     * Расхождением считается любое несовпадение написания, не только snake_case.
     */
    @JsonProperty("usertype")
    private UserTypeModel userType;

    /** Строка {@code "Tops"} — вложенное поле, не путать с именем самого объекта-владельца. */
    private String category;
}
