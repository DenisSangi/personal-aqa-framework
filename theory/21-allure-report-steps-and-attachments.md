# Allure: шаги, вложения и как они физически попадают в отчёт

Опора для [P11-T1](../curriculum/P11-T1-allure-steps-attachments.md).

Все числа и все «работает / не работает» ниже — **замерены** на изолированных харнессах и на самом проекте 2026-08-19, а не взяты из документации. Где факт получен замером, это сказано прямо.

---

## 1. Отчёт — это два разных шага, а не один

Тут легко запутаться, потому что словом «Allure» называют три разные вещи.

```
прогон тестов  ──►  allure-results/   ──►  allure-report/
                    (сырые JSON)          (HTML-страница)
     ↑                    ↑                      ↑
 allure-testng      просто папка          генератор:
 (библиотека,       с файлами            CLI `allure` или
 уже в pom.xml)                          плагин allure-maven
```

1. **Библиотека времени прогона** (`io.qameta.allure:allure-testng`) — подписывается на события TestNG и на каждый тест пишет в папку `allure-results/` по файлу `<uuid>-result.json`.
2. **Папка `allure-results/`** — сырьё. Открывать её человеку бессмысленно, это JSON.
3. **Генератор отчёта** — отдельная программа, которая превращает сырьё в HTML. Её в проекте **нет ни в каком виде** (проверено: `allure` не в `PATH`, плагина `allure-maven` в `pom.xml` нет).

**Что это значит для проекта прямо сейчас.** Пункт 1 работает с 2026-07-24 — в `allure-results/` лежит **6797 файлов, 27 МБ**, накопленных за 26 дней (2642 `result.json` + 4155 `container.json`). Пункт 3 не выполнялся **ни разу**. То есть отчёт исправно производится и ни разу не был открыт.

Вот как выглядит типичный `result.json` из этой папки (реальный файл проекта, сокращён):

```json
{
  "fullName": "api.services.LoginApiServiceTest.loginApiTest",
  "status": "passed",
  "steps": [],
  "attachments": []
}
```

`steps: []` и `attachments: []` — **во всех 2642 файлах**. Отчёт существует, но внутри каждого теста пусто: видно только «зелёный / красный» и имя метода. Ровно то, что задача P11-T1 и должна изменить.

---

## 2. Шаг: два способа его создать, и они устроены по-разному

### Способ А — аннотация `@Step`

```java
@Step("Открыть страницу {url}")
void open(String url) { ... }
```

Читается красиво: пометил метод — он стал шагом отчёта, `{url}` подставится реальным значением аргумента.

### Способ Б — вызов `Allure.step(...)`

```java
Allure.step("Открыть страницу " + url);
```

Обычный статический вызов, никакой магии.

### Разница, которую видно только на замере

Возьмём минимальный проект: `allure-testng` в зависимостях, один тест, в нём оба способа сразу.

```java
public class ProbeTest {
    @Step("шаг через аннотацию: открыть {url}")
    void annotatedStep(String url) { }

    @Test
    public void probe() {
        annotatedStep("https://example.com");
        Allure.step("шаг через runtime-API Allure.step(...)");
    }
}
```

Прогон **как есть** (ровно та конфигурация, что сейчас в `personal-aqa-framework`):

```
probe | steps: ['шаг через runtime-API Allure.step(...)']
```

Шага от `@Step` **нет**. При этом:
- проект скомпилировался;
- тест прошёл;
- ни ошибки, ни предупреждения, ни строчки в логе.

Тот же проект, добавлен один флаг запуска:

```
-javaagent:.../aspectjweaver-1.9.22.jar
```

```
probe | steps: ['шаг через аннотацию: открыть https://example.com',
                'шаг через runtime-API Allure.step(...)']
```

Шаг появился, и `{url}` подставился реальным аргументом.

### Почему так

`@Step` — не «фича Java», а точка для **AOP** (aspect-oriented programming). Аннотация сама по себе ничего не делает — это пометка в байткоде. Кто-то должен эту пометку прочитать и вставить вокруг метода дополнительный код («заплести», *weave*). Этим занимается **AspectJ weaver** — он подключается как **javaagent**, то есть как надстройка над JVM, стартующая раньше самих классов и переписывающая их на лету при загрузке.

Аспекты Allure лежат прямо в jar'е (проверено `unzip -l` по `allure-java-commons-2.35.3.jar`):

```
io/qameta/allure/aspects/StepsAspects.class
io/qameta/allure/aspects/AttachmentsAspects.class
```

Классы есть, а того, кто их применит, нет. Проверено `mvn dependency:tree` — в дереве зависимостей проекта **нет ни одного артефакта `org.aspectj`**, и в `<configuration>` surefire нет `<argLine>`.

`Allure.step(...)` ничего этого не требует: это прямой вызов метода библиотеки, он работает в любой конфигурации.

> **Формулировка для интервью.** «Аннотация — это данные, а не поведение. Поведение появляется только тогда, когда есть тот, кто аннотацию читает: компилятор, фреймворк или, как здесь, javaagent. Если читателя нет — аннотация компилируется, ничего не ломает и молча ничего не делает.» Это ровно тот же класс отказа, что `logback.xml` в неправильной папке из P1-T2: конструкция на месте, механизм её не подхватывает.

---

## 3. Вложения (attachments) — и место, где они теряются

Вложение — произвольный файл, привязанный к тесту в отчёте: скриншот, HTML страницы, тело HTTP-ответа, лог.

API выглядит просто:

```java
Allure.addAttachment("имя", "text/plain", "содержимое", ".txt");
```

Три замера на одном и том же падающем тесте, отличается только **место вызова**:

| Откуда вызван `Allure.addAttachment(...)` | Что в `result.json` |
|---|---|
| из тела `@Test` | `attachments: [{name: 'из тела теста', source: '...-attachment.txt'}]` ✅ |
| из `@AfterMethod(alwaysRun = true)` с параметром `ITestResult` | `attachments: []` ❌ |
| из `ITestListener.onTestFailure(...)` | `attachments: []` ❌ |

Во всех трёх случаях код выполнился — строка `>>> attachment добавлен` печаталась в консоль. Молча потерялось именно вложение.

Заодно замерен порядок вызовов, потому что интуитивно кажется, что дело в закрытом браузере:

```
>>> тело теста
>>> onTestFailure
>>> @AfterMethod tearDown
```

`onTestFailure` срабатывает **раньше** `@AfterMethod`, то есть браузер в этот момент ещё жив (`Selenide.closeWebDriver()` ещё не вызывался). Поэтому объяснение «скриншот не сделать, браузера уже нет» — **неверно**: скриншот делается прекрасно, он и лежит на диске. Теряется именно привязка вложения к тесту.

Это и объясняет, почему в проекте `attachments: []` в 2642 файлах, хотя `ScreenshotOnFailureListener` (P6-T2) исправно работает: скриншоты есть, но на диске в `build/reports/tests/` (например `incorrectSignupWithExistedEmail.png`), а не в отчёте.

---

## 4. Готовые интеграции — те же паттерны, что уже построены

### `allure-selenide` — Observer на третьем механизме

У Selenide есть собственная шина событий (то же самое `subscribe`/`notify`, что разбиралось в [08-observer-and-testng-listeners.md](08-observer-and-testng-listeners.md), только subject — не TestNG, а сам Selenide):

```java
SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
```

Проверено по jar'у, `javap`:

```java
public class AllureSelenide implements com.codeborne.selenide.logevents.LogEventListener {
    public AllureSelenide screenshots(boolean);
    public AllureSelenide savePageSource(boolean);
    public AllureSelenide includeSelenideSteps(boolean);
    ...
}
```

То есть это **третье применение Observer в проекте**: `ITestListener` (P6-T2) → `ISuiteListener` (P8-T3) → `LogEventListener` (здесь). Тот же механизм подписки на события чужого фреймворка, третий раз подряд — хороший материал на интервью.

Ключевое отличие от `@Step`: подписчик работает **во время тела теста**, а не после, и превращает в шаг каждое действие Selenide (`$(...).click()`, `shouldBe(visible)`). AspectJ ему не нужен — это обычный вызов метода.

Тот же самый механизм используется в боевом `java-selenide-pp` — но для другой задачи: `gp-test-core/src/test/java/listeners/CustomSoftAssertListener.java:33` подписывает свой listener на `SelenideLogger` ради soft-assert'ов.

### `allure-rest-assured` — Filter

У REST Assured свой хук — `filter(...)`, который видит запрос и ответ и может приложить их к отчёту. В проекте есть ровно одна точка, где строится любой HTTP-запрос — `RestClient` (пять методов, все через `given()`).

### Как это сделано в референсе `java-selenide-pp`

Важный проверенный факт: **`@Step` в боевом проекте не встречается ни разу** (`grep` по всем модулям — 0 совпадений), и `aspectjweaver` в его `pom.xml` тоже нет. Шаги там берутся из логов: `clincard-ui-test-automation/src/main/java/com/suvoda/automation/logging/AllureAppender.java` — это appender log4j2, который на каждое лог-сообщение делает

```java
if (Allure.getLifecycle().getCurrentTestCase().isPresent()) {
    Allure.step(event.getMessage().getFormattedMessage());
}
```

То есть каждая строка лога становится шагом отчёта. Мост «лог → Allure», а не аннотации.

Оговорка, найденная ещё в Сессии 20: этот appender **написан внутри компании**, публичного аналога у Allure нет ни для log4j2, ни для logback (проверено: среди всех 66 публичных артефактов `io.qameta.allure` нет ни `allure-logback`, ни `allure-slf4j`). Значит третий путь не бесплатный — его пришлось бы писать руками поверх логгера, который появился в P1-T2.

---

## 5. Как отчёт вообще посмотреть

Два штатных способа, оба сейчас в проекте отсутствуют:

| Способ | Что нужно | Команда |
|---|---|---|
| CLI | установить `allure` (brew/scoop/архив) | `allure serve allure-results` |
| Maven-плагин | `io.qameta.allure:allure-maven` в `<build><plugins>` | `mvn allure:report` / `mvn allure:serve` |

Отдельный момент — **гигиена папки результатов**. `allure-results/` лежит в корне проекта, а не внутри `target/`, поэтому `mvn clean` её не трогает: за 26 дней там накопились результаты примерно 80 прогонов вперемешку. Отчёт, построенный по такой папке, покажет всё это одной кучей. Путь папки задаётся файлом `allure.properties` в ресурсах (`allure.results.directory=...`) — в проекте его нет, поэтому работает дефолт.

---

## 6. Короткая сводка проверенных фактов

| Утверждение | Как проверено |
|---|---|
| Все 2642 `result.json` проекта имеют `steps: []` и `attachments: []` | разбор папки `allure-results/` |
| `@Step` без `-javaagent:aspectjweaver` не создаёт шага и не сообщает об этом | изолированный харнесс, прогон с флагом и без |
| `Allure.step(...)` работает без всякого weaver'а | тот же харнесс |
| `org.aspectj` отсутствует в дереве зависимостей | `mvn dependency:tree` |
| `Allure.addAttachment` работает из тела теста и теряется из `@AfterMethod` / `onTestFailure` | три прогона харнесса |
| `onTestFailure` вызывается **до** `@AfterMethod` | харнесс с печатью порядка |
| Скриншоты P6-T2 лежат на диске в `build/reports/tests/`, а не в отчёте | `find build -name "*.png"` |
| `AllureSelenide implements LogEventListener` | `javap` по jar'у |
| В референсе нет ни одного `@Step`; шаги делает `AllureAppender` из логов | `grep` по всем модулям |
| Ни `allure` CLI, ни плагина `allure-maven` в проекте нет | `which allure`, чтение `pom.xml` |
