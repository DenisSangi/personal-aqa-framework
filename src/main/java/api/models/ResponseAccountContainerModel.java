package api.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * КОНВЕРТ ответа {@code GET /api/getUserDetailByEmail} — описывает всё тело целиком.
 * Не путать с {@link AccountResponseModel}: тот описывает только вложенный объект {@code user}.
 * Полная карта уровней — в {@code package-info.java}.
 *
 * <pre>
 * успех:  {"responseCode": 200, "user": {...}}
 * ошибка: {"responseCode": 404, "message": "Account not found with this email, try another email!"}
 * </pre>
 *
 * Оба тела описаны ОДНИМ классом намеренно: разбор идёт до проверки {@code responseCode},
 * и если бы {@code message} здесь не было, на любом не-200 падал бы Jackson,
 * а ветка обработки ошибки не выполнялась бы никогда.
 */
@Data
public class ResponseAccountContainerModel {

    private int responseCode;

    /** Поле намеренно названо иначе, чем ключ в JSON, — отсюда явный {@code @JsonProperty}. */
    @JsonProperty("user")
    private AccountResponseModel accountResponseModel;

    /** Приходит только в теле ошибки; в успешном ответе остаётся {@code null}. */
    private String message;
}
