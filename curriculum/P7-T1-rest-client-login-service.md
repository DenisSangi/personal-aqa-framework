# P7-T1 — RestClient adapter + первый API-сервис (LoginApiService)

**Фаза / слой:** Фаза 7 — Слой API-сервисов
**Теги:** java, rest-assured

## Описание

Открываем новый, параллельный UI слой — HTTP. Три маленьких класса:

1. `RestClient` — тонкая обёртка над REST Assured: конструктор принимает `baseUrl`, наружу — `get(String endpoint)` и `post(String endpoint, Map<String, String> formParams)`, оба возвращают `Response`.
2. `BaseApiService` — держит `protected final RestClient restClient`, инициализированный `FrameworkConfig.APP_URL` (ровно то же значение, которым уже открывается браузер в UI-тестах).
3. `LoginApiService extends BaseApiService` — первый конкретный сервис, метод `verifyLogin(String email, String password)`.

Это прямая параллель тому, как была устроена Фаза 3: тогда `BaseElement` + `InputElement` вводились в одной задаче, чтобы сразу увидеть паттерн на конкретном примере, а не абстрактно. Здесь то же самое — `BaseApiService`/`RestClient` без конкретного сервиса нечем было бы проверить.

## Место в фреймворке

Зависимость: опирается на Фазу 1 (`FrameworkConfig.APP_URL`, `FrameworkConfig.APP_USER_EMAIL`/`APP_USER_PASSWORD` — те же креды, уже подтверждённые рабочими в UI-логине на P4-T2). Полностью независима от Фаз 3–6 (UI-слой) — этот слой не знает про Selenide, `WebDriver`, `$`/`$x`.

От неё зависит: P7-T2 (модель ответа + фабрика тестовых данных поверх этого же `RestClient`), а в перспективе — Фаза 10 (`P10-T3`, API CRUD-тесты) и общий принцип «UI проверяет UI, setup — через API» (`Framework explanation/4-api-services-layer.md`, раздел 1).

## Мотивировка

Прямо сейчас в проекте уже есть один живой пример прямого вызова REST Assured — `FrameworkConfigSmokeTest` (P1-T1), где `given().baseUri(url).when().get(...).then()...` написан inline, внутри самого теста. Это нормально для однократной проверки конфигурации, но представь: как только в проекте появится второй HTTP-вызов (а он появляется прямо в этой задаче — `verifyLogin`), придётся либо копировать этот же `given()...` блок заново, либо — что хуже — каждый вызывающий код будет сам решать, как собрать запрос, где обрабатывать ошибку, откуда брать `baseUrl`.

Без адаптера миграция HTTP-библиотеки (например, если завтра решишь попробовать другой HTTP-клиент) означала бы правку в каждом месте, где сейчас разбросаны вызовы REST Assured. С адаптером — правка одного класса. Это тот же принцип Adapter (`design-patterns.md` #5), который уже применялся в Фазе 3 — `BaseElement` прятал Selenide за собой так же, как здесь `RestClient` прячет REST Assured.

## Открытые вопросы (обсудим перед кодом)

### Вопрос 1 — где ловить ошибку: HTTP-статус или тело ответа?

Референсный `RestClient.java` (`clincard-ui-test-automation`) в каждом методе делает:

```java
if (response.getStatusCode() >= 400) {
    throw new RuntimeException("HTTP Error " + response.getStatusCode() + ": " + response.getBody().asString());
}
```

Я реально проверил, как ведёт себя API `automationexercise.com` (curl, не предположение):

```
POST /api/verifyLogin  email=<несуществующий>&password=<любой>
→ HTTP 200
→ тело: {"responseCode": 404, "message": "User not found!"}

POST /api/verifyLogin  password=<без email>
→ HTTP 200
→ тело: {"responseCode": 400, "message": "Bad request, email or password parameter is missing..."}
```

Обрати внимание: **HTTP-статус в обоих случаях — 200.** Реальный код результата спрятан внутри JSON-тела, в поле `responseCode`. Если бы `RestClient` слепо скопировал проверку `status >= 400` из референса — она НИКОГДА бы не сработала на этом API, ошибка тихо прошла бы дальше как «успешный» ответ.

Вопрос: должен ли `RestClient` (общий адаптер) вообще решать, что считать ошибкой? Или на этом конкретном API это — забота вызывающего кода (`LoginApiService`/теста), который знает про `responseCode`, а `RestClient` should just return `Response` as-is?

### Вопрос 2 — тело запроса: `Map<String, String> formParams` или `Object body` + `ContentType`?

Референсный `RestClient` умеет отправлять и JSON (`.contentType(ContentType.JSON).body(object)`), и multipart/form-data, и `application/x-www-form-urlencoded` — потому что реальный ClinCard-бэкенд использует все три формата в разных местах.

Я проверил (curl), что `automationexercise.com/api/verifyLogin` принимает данные как `application/x-www-form-urlencoded` — обычные form-параметры, не JSON:

```
curl -X POST .../api/verifyLogin -d "email=...&password=..."
```

Вопрос: заводить ли `post(...)` сразу под несколько форматов (по образцу референса — с расчётом на будущее), или сделать сейчас только то, что реально нужно для `verifyLogin` (`Map<String, String> formParams`), а JSON/другие форматы добавить отдельной перегрузкой, когда/если появится реальный эндпоинт, которому это нужно?

## Теория

Новый материал — `theory/rest-assured-basics-and-adapter.md`: `given()/when()/then()` синтаксис REST Assured, `RequestSpecification`, form-параметры vs JSON body, чтение полей из JSON-ответа через `.jsonPath()` (без модели — модели будут в P7-T2), и короткое связывание с Adapter (уже разобран в Фазе 3 — не переобъясняется с нуля, только перенос на новый слой).

## Definition of Done

- [ ] Осознанные ответы на оба открытых вопроса, обсуждённые до начала кода.
- [ ] `RestClient(String baseUrl)` — методы `get(String endpoint)` и `post(String endpoint, Map<String, String> formParams)`, оба возвращают `Response`.
- [ ] `BaseApiService` — `protected final RestClient restClient`, инициализирован `FrameworkConfig.APP_URL`.
- [ ] `LoginApiService extends BaseApiService` — `verifyLogin(String email, String password)`.
- [ ] Живой тест: вызов `verifyLogin(FrameworkConfig.APP_USER_EMAIL, FrameworkConfig.APP_USER_PASSWORD)` — assert по содержимому JSON-тела (`responseCode`/`message`), НЕ по `response.getStatusCode()` (см. Вопрос 1). Плюс минимум один негативный кейс (неверный пароль → `responseCode` 404).
- [ ] `mvn clean test` — весь проект зелёный.

## Типичные ошибки

Открытые вопросы решены верно и обоснованно ДО кода: `RestClient` — глухой транспорт, ничего не интерпретирует; `LoginApiService` владеет разбором `responseCode` (по аналогии с `ClinCardSiteService` из референса); `post(...)` сужен до `Map<String, String> formParams`, без задела на JSON. Финальный контракт `verifyLogin`: `boolean` — `200` → `true`, `404` → `false`, иначе → исключение.

Дальше — несколько раундов ревью, самая насыщенная находками задача проекта на сегодняшний день:

1. **Неверный эндпоинт, первая версия:** `"login/"` — это путь UI-страницы логина (HTML), а не API. Денис поймал симптом сам (через дебаг увидел HTML вместо JSON), но не сразу понял причину.
2. **Неверный эндпоинт, вторая попытка:** `"verifyLogin/"` — без сегмента `/api/` и с лишним слэшем на конце. Реальный API всё ещё не найден (`HTTP 404`, HTML "Page not found"). Урок: если тело ответа — HTML с "Page not found", это почти всегда «не туда стучимся», а не «не тот формат». Исправлено сверкой с `curl`-командой, уже зафиксированной в этом же лесон-файле.
3. **Контракт метода разошёлся с согласованным:** первая версия `verifyLogin` возвращала `String` (текст `message`) и не различала `200`/`404` — обе ветки трактовались одинаково (`if (200 || 404) return message`). Возвращено к согласованному `boolean`.
4. **Тест с придуманными, непроверенными строками:** ожидаемые значения (`"Succesful login"`, `"Email or paswword incorrect"` — с опечатками) не совпадали с реальным ответом API (`"User exists!"`/`"User not found!"`) и не были ничем сверены. Плюс переменная `wrongCreds` была объявлена, но нигде не использовалась — по факту негативного кейса не было вообще, хотя DoD его требовал.
5. **Не было техники проверки исключений:** первая попытка `exceptionTest` пыталась сравнить `assertEquals(verifyLogin(...), expectedPattern)` — бессмысленно, потому что при `throw` метод не возвращает значение вообще, до `assertEquals` дело не доходит. Решилось через разбор изолированного примера (`divide(a, b)` + `Assert.assertThrows(...)`) — перенёс сам после примера.
6. **Не понимал, чем «пустое значение» отличается от «отсутствующего ключа» на уровне HTTP:** пробовал вызывать `verifyLogin` с мусорными/пустыми строками, ожидая поймать `400 Bad request` — но API возвращает `400` только когда форм-параметр отсутствует **полностью**, а не когда он пустая строка (проверено `curl`: `email=&password=test` всё ещё даёт `404`, а не `400`). Метод `verifyLogin` при этом безусловно клал оба ключа в `formParams`, поэтому физически не мог сформировать запрос с отсутствующим ключом. Решение — сделать `put(...)` условным (только если значение не `null` и не пустое), додумался сам после наводящего вопроса про то, что должно произойти в методе, чтобы ключ не попал в мапу.
7. **Гигиена констант:** эндпоинт сначала был инстанс-полем (`private final String endpoint`), а не `private static final String LOGIN_ENDPOINT` — разошлось с конвенцией, принятой во всём остальном проекте и в референсе. Поправлено последним шагом перед аппрувом.

`mvn clean test` — весь проект зелёный (живой прогон, exit code 0), `LoginApiServiceTest` — 3/3 (позитивный кейс на реальные креды из `FrameworkConfig`, негативный на неверные email/password по отдельности, кейс на исключение при отсутствующем email и при пустом password). **P7-T1 аппрувед 2026-07-31.**

## Ссылка на реальный код java-selenide-pp

- `clincard-ui-test-automation/src/main/java/com/suvoda/automation/api/core/RestClient.java` — полный референс (350+ строк, десятки перегрузок — сознательно урезаем до двух методов, см. Вопрос 2).
- `clincard-ui-test-automation/src/main/java/com/suvoda/automation/api/core/ClinCardBaseApiService.java` — прообраз `BaseApiService` (минималистичный: только `restClient` + `BASE_URL`).
- `Framework explanation/4-api-services-layer.md` — полное описание слоя, разделы 1 («Зачем нужен этот слой») и 3 («Как устроена логика слоя») особенно релевантны.
- `Framework explanation/design-patterns.md` #5 — Adapter, короткое «без паттерна»/«где» объяснение (уже разбирался в Фазе 3, здесь — перенос на новый слой).
