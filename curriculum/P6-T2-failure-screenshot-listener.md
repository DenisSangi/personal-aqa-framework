# P6-T2 — Listener на падение теста (скриншот)

**Фаза / слой:** Фаза 6 — Базовый тест и lifecycle
**Теги:** java, testng

## Описание

Класс `ScreenshotOnFailureListener`, реализующий `org.testng.ITestListener`, с переопределённым методом `onTestFailure(ITestResult result)` — внутри вызывается `Selenide.screenshot(...)`, чтобы при падении ЛЮБОГО теста в проекте автоматически сохранялся скриншот состояния браузера в этот момент.

Подключается через `@Listeners(ScreenshotOnFailureListener.class)` — **один раз**, на `BaseTest`, а не на каждый тестовый класс по отдельности.

## Место в фреймворке

Зависимость: опирается на P6-T1 — именно то, что все тестовые классы уже наследуются от `BaseTest`, позволяет подключить листенер в одном месте и получить его во всех пяти классах сразу, без правки каждого.

От неё будет зависеть Фаза 11 (Allure) — тот же самый хук `onTestFailure` в будущем, скорее всего, будет не просто сохранять файл, а прикреплять скриншот к Allure-отчёту.

## Мотивировка

Вспомни, чем мы только что пользовались при разборе падения `loginFlowTest` в предыдущей задаче: скриншотом, который Selenide сохранил сам. Но обрати внимание — тот скриншот появился не потому, что мы что-то для этого написали, а потому что падение произошло ВНУТРИ `shouldBe`/`shouldHave` — у Selenide есть свой внутренний механизм скриншота именно на такой тип ошибки.

А что если тест упадёт по другой причине — например, обычный `Assert.assertEquals(...)` из TestNG вернул `false`, без единого обращения к Selenide-условию в этот момент? Скриншота не будет вообще — при разборе такого падения не на чём будет посмотреть, что происходило на странице.

Без паттерна логику "если тест упал — сделать скриншот" пришлось бы либо дублировать вручную в каждом тесте (`try/catch` вокруг тела), либо явно проверять статус в `@AfterMethod`. Вместо этого TestNG сам уведомляет подписанные листенеры о событии `onTestFailure` — не зная и не заботясь, что именно листенер делает в ответ. Это и есть паттерн **Observer** (design-patterns.md #9): TestNG — subject, `ScreenshotOnFailureListener` — observer.

## Открытый вопрос (обсудим перед кодом)

В самом `java-selenide-pp` бок о бок существуют ДВА разных технических решения одной и той же задачи "сделать скриншот при падении теста":

**Вариант A** — `ClinCardBaseTest.afterMethodInBaseClass`, обычный `@AfterMethod`, которому TestNG сам подставляет параметр `ITestResult`:
```java
@AfterMethod(alwaysRun = true)
public void afterMethodInBaseClass(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE && WebDriverRunner.hasWebDriverStarted()) {
        allureTmi.screenshot("screenshotOnTestFailure");
    }
}
```

**Вариант B** — `GreenphireTestListener`, отдельный класс, реализующий `ITestListener`, подключаемый через `@Listeners(...)`:
```java
public class GreenphireTestListener extends TestListener {
    @Override
    public void onTestFailure(ITestResult result) { ... }
}
```

Внешне результат одинаковый — при падении что-то происходит. Вопросы для обсуждения:
1. Какой из двух вариантов — это на самом деле паттерн Observer (TestNG как subject уведомляет независимого наблюдателя), а какой — просто "базовый класс сам себя проверяет" внутри уже знакомого нам `@AfterMethod`?
2. Представь, что в проекте появится тестовый класс, который по какой-то причине НЕ наследуется от `BaseTest` (гипотетически). У какого из двух вариантов есть шанс сработать и для него тоже, а у какого — нет?

Задача просит реализовать именно **Вариант B** — но важно понимать, чем он отличается от Варианта A не только по синтаксису, а по устройству.

## Теория

Новый материал — `theory/08-observer-and-testng-listeners.md`: Observer на изолированном примере (без TestNG — простая «кнопка» и подписанные на её клик слушатели), затем перенос на `ITestListener`/`@Listeners` и разбор `Selenide.screenshot(...)`.

## Definition of Done

- [ ] Осознанный ответ на оба открытых вопроса, обсуждённый до кода.
- [ ] `ScreenshotOnFailureListener implements ITestListener`, `onTestFailure(ITestResult result)` вызывает `Selenide.screenshot(...)`.
- [ ] `@Listeners(ScreenshotOnFailureListener.class)` подключён один раз — на `BaseTest`.
- [ ] Живая проверка: временно сломать любой тест (например, заведомо неверный assert), прогнать, убедиться, что скриншот сохранился, затем откатить временную поломку.
- [ ] `mvn clean test` — весь проект зелёный (не считая уже задокументированной внешней нестабильности `loginFlowTest` из P6-T1).

## Типичные ошибки

**Оговорка:** та же, что в P6-T1 — сессия 2026-07-30 закрыла эту задачу вживую, стенограмма ревью не сохранилась. Ниже — реконструкция по финальному коммиту (`1092de6`), не пересказ диалога.

- `ScreenshotOnFailureListener implements ITestListener` реализован по Варианту B, как требовал DoD, — отдельный класс, `onTestFailure(ITestResult result)` → `Selenide.screenshot(result.getName())`, подключён единожды через `@Listeners(ScreenshotOnFailureListener.class)` на `BaseTest`.
- Тем же коммитом `BaseTest.tearDown()` дополнительно получил `alwaysRun = true` (в версии из P6-T1 его не было) — это ровно тот нюанс, на который прямо указывала ссылка на `GreenphireBaseTest.java` в задаче P6-T1 («обрати внимание на сам факт этого параметра»). Похоже, что он был замечен и добавлен именно на этой задаче, а не пропущен — без `alwaysRun = true` `@AfterMethod` не выполнился бы для теста, упавшего до штатного завершения, и браузер остался бы висеть.
- Попутно удалён неиспользуемый `PlaceholderTest.java`, оставшийся от P0-T1 — не по теме задачи, но валидная гигиена, не ошибка.

## Ссылка на реальный код java-selenide-pp

- `gp-test-core/src/test/java/listeners/GreenphireTestListener.java` — структура `ITestListener`-реализации (в реальном коде — только логирование, без скриншота, но именно тот механизм подключения через `@Listeners`, который нужен здесь).
- `Framework explanation/1-test-layer.md`, раздел «Скриншот на падении» — пример Варианта A (`ClinCardBaseTest.afterMethodInBaseClass`), источник открытого вопроса.
- `Framework explanation/design-patterns.md` #9 — Observer, короткое «без паттерна»/«где» объяснение.
