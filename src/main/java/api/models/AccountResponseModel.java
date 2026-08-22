package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * ВЛОЖЕННАЯ модель — объект под ключом {@code user} внутри {@link ResponseAccountContainerModel}.
 * Напрямую в {@code response.as(...)} не передаётся: {@code as()} применяется к корню тела,
 * а этот объект лежит уровнем ниже.
 *
 * <p>Не путать с {@link AccountModel} — та описывает ЗАПРОС (что мы отправляем) и собирается
 * билдером; эта описывает ОТВЕТ (что нам вернули) и собирается Jackson, отсюда {@code @Data}:
 * комбинация {@code @Builder + @Getter} не десериализуется вовсе.
 *
 * <p>Имена полей у запроса и ответа РАЗНЫЕ для одной и той же сущности — стенд использует три
 * системы написания: {@code firstName} (Java) → {@code firstname} (ключ запроса)
 * → {@code first_name} (ключ ответа). Поэтому здесь snake_case размечен явно.
 */
@Data
public class AccountResponseModel {
    private int id;
    private String name;
    private String email;
    private String title;
    @JsonProperty("birth_day")
    private String birthDay;
    @JsonProperty("birth_month")
    private String birthMonth;
    @JsonProperty("birth_year")
    private String birthYear;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String company;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String city;
    private String zipcode;
}
