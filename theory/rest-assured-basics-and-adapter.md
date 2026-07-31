# REST Assured — базовый синтаксис, и снова Adapter

Опора для [P7-T1](../curriculum/P7-T1-rest-client-login-service.md).

## 1. `given() / when() / then()` — откуда это вообще

REST Assured построен вокруг fluent-цепочки из трёх частей, которая читается почти как английское предложение:

```java
given()                          // "дано" — что мы настраиваем перед запросом
    .baseUri("https://automationexercise.com")
    .formParam("email", "test@test.com")
    .formParam("password", "1234")
.when()                          // "когда" — какое действие выполняем
    .post("/api/verifyLogin")
.then()                          // "тогда" — что проверяем / что достаём из ответа
    .extract().response();
```

Это ровно та же идея, что BDD-формат Given/When/Then в тестировании вообще — только здесь это не комментарий, а реальные методы API.

**Важный практический момент:** `.when()` и `.then()` — не обязательны, если тебе не нужна валидация внутри самой цепочки. Можно короче:

```java
Response response = given()
    .baseUri("https://automationexercise.com")
    .formParam("email", "test@test.com")
    .formParam("password", "1234")
    .post("/api/verifyLogin");
```

`given()` возвращает `RequestSpecification` — объект-строитель запроса. У него есть метод `.post(endpoint)` (как и `.get(endpoint)`, `.put(endpoint)`) — вызов сразу отправляет запрос и возвращает `Response`. `.when()` в середине — просто синтаксический разделитель для читаемости, самой цепочке методов он не обязателен. Референсный `RestClient.java` этим и пользуется: `return given().baseUri(baseUrl).get(endpoint);` — без единого `.when()`/`.then()`.

Уже использованный в проекте пример (`FrameworkConfigSmokeTest`, P1-T1) — с `.when().then().extract()` — тоже корректен, просто чуть многословнее для простого случая. Оба стиля равнозначны, выбор — вопрос читаемости в конкретном месте.

## 2. `RequestSpecification` — что это и зачем разделять на переменную

```java
public RequestSpecification createRequest() {
    return given().baseUri(baseUrl);
}
```

`RequestSpecification` — это «недособранный запрос»: базовый URL уже задан, но метод (`GET`/`POST`) и путь ещё нет. Смысл выносить это в отдельный метод/переменную — переиспользовать общую часть настройки (`baseUri`, возможно заголовки), не повторяя её в каждом методе `RestClient`.

## 3. Form-параметры vs JSON body

Два разных способа передать данные в теле POST-запроса — зависят от того, что ожидает конкретный backend:

```java
// application/x-www-form-urlencoded — как обычная HTML-форма
given().baseUri(url)
    .formParam("email", email)
    .formParam("password", password)
    .post("/api/verifyLogin");

// application/json
given().baseUri(url)
    .contentType(ContentType.JSON)
    .body(someObject)          // сериализуется в JSON автоматически
    .post("/api/createSomething");
```

Для нескольких параметров сразу удобнее `.formParams(Map<String, String> map)` — та же идея, но одним вызовом вместо цепочки `.formParam(...).formParam(...)`.

`automationexercise.com/api/verifyLogin` ожидает именно form-параметры (проверено `curl -d "email=...&password=..."`), не JSON — это и определило сигнатуру `post(String endpoint, Map<String, String> formParams)` в задаче.

## 4. Чтение ответа: `Response`, `.asString()`, `.jsonPath()`

`Response` — объект с несколькими способами прочитать, что пришло:

```java
response.getStatusCode();          // int — HTTP-статус (200, 404, ...)
response.getBody().asString();     // String — сырое тело ответа как есть
response.jsonPath().getInt("responseCode");     // достать конкретное поле из JSON
response.jsonPath().getString("message");       // ещё одно поле
```

`.jsonPath()` — встроенный в REST Assured способ читать поля JSON-ответа **без создания класса-модели** — просто по имени поля, как путь. Это ровно то, что нужно для `LoginApiService.verifyLogin(...)`: тело ответа `{"responseCode": 404, "message": "User not found!"}` — читаем `responseCode` и/или `message` напрямую, без модели. Модели (через `record`/`@Builder`) — тема следующей задачи, P7-T2, когда полей станет больше и типизация начнёт окупаться.

**Ключевой нюанс этой задачи:** `response.getStatusCode()` для `automationexercise.com` почти бесполезен как индикатор ошибки — API возвращает HTTP 200 даже на логически неверный запрос (несуществующий email, отсутствующий параметр). Реальный результат — только в теле, в поле `responseCode`. Это не универсальное правило REST Assured, а особенность конкретного backend'а — но именно из-за таких особенностей `RestClient` не может «зашить» проверку ошибок внутри себя одинаково для всех API (см. Вопрос 1 в лесон-файле).

## 5. Снова Adapter — короткое напоминание, не с нуля

Уже разбирался в Фазе 3 (`theory/selenide-basics-and-generics.md`, где `BaseElement` прятал Selenide). Идея переносится один в один:

| | Что прячет | Что было бы без адаптера |
|---|---|---|
| `BaseElement` (Фаза 3) | Selenide (`$`, `SelenideElement`) | Каждый POM работает с Selenide напрямую — миграция драйвера ломает все POM |
| `RestClient` (эта задача) | REST Assured (`given()/when()/then()`) | Каждый вызывающий код сам собирает `given()...` — миграция HTTP-клиента ломает всё |

Один и тот же паттерн, два разных слоя (`design-patterns.md` #5).
