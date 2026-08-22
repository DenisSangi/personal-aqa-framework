package api.models;

import lombok.Data;

/**
 * КОНВЕРТ ответа трёх МУТИРУЮЩИХ операций над аккаунтом:
 * {@code POST /api/createAccount}, {@code PUT /api/updateAccount}, {@code DELETE /api/deleteAccount}.
 *
 * <pre>
 * {"responseCode": 201, "message": "User created!"}
 * </pre>
 *
 * <p>Единственный конверт без полезной нагрузки: эти эндпоинты не возвращают сущность,
 * только исход операции. Отсюда и {@code Common} в имени — он общий для трёх методов,
 * потому что у них СОВПАДАЕТ форма тела, а не потому, что они «про аккаунт».
 *
 * <p>Почему не переиспользован {@link ResponseAccountContainerModel}, где эти два поля уже есть:
 * в теле этих трёх операций ключа {@code user} не бывает НИКОГДА. Взяв тот конверт, вызывающий код
 * получил бы тип с методом {@code getAccountResponseModel()}, всегда возвращающим {@code null}, —
 * тип обещал бы то, чего у эндпоинта нет. Плюс один класс оказался бы общим для четырёх методов,
 * и правка под изменившийся {@code getUserDetailByEmail} задевала бы три мутирующих.
 *
 * <p>Ощущение дублирования верное — это цена правила «конверт на эндпоинт». Устраняется оно
 * обобщённым {@code ApiResponse<T>}, а не переиспользованием более широкого конверта.
 */
@Data
public class AccountCommonResponseModel {

    private int responseCode;

    /**
     * Здесь, в отличие от других конвертов, приходит и в успехе, и в ошибке:
     * {@code "User created!"} либо текст отказа.
     */
    private String message;
}
