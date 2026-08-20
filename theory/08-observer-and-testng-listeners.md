# Observer и TestNG Listeners

Опора для [P6-T2](../curriculum/P6-T2-failure-screenshot-listener.md).

## 1. Observer на изолированном примере

Прежде чем смотреть на TestNG, минимальный пример без него вообще — «кнопка», у которой есть список подписчиков на клик:

```java
interface ClickListener {
    void onClick();
}

class Button {
    private final List<ClickListener> listeners = new ArrayList<>();

    void subscribe(ClickListener listener) {
        listeners.add(listener);
    }

    void click() {
        // Button ничего не знает о том, ЧТО делают подписчики — только оповещает их
        for (ClickListener listener : listeners) {
            listener.onClick();
        }
    }
}
```

```java
button.subscribe(() -> System.out.println("log: clicked"));
button.subscribe(() -> System.out.println("play sound"));
button.click(); // выведет обе строки — оба подписчика уведомлены
```

Ключевая идея: `Button` (**subject**, «наблюдаемый») ничего не знает про логирование или звук — она просто хранит список подписчиков и оповещает их о событии. Что конкретно делает подписчик (**observer**, «наблюдатель») — не забота `Button`. Если завтра добавится третий подписчик, `Button` не меняется вообще.

## 2. Перенос на TestNG

TestNG — это `Button` из примера выше, только событие не «клик», а «тест упал»/«тест прошёл»/«тест пропущен». Интерфейс `org.testng.ITestListener` — это `ClickListener`:

```java
public interface ITestListener {
    default void onTestSuccess(ITestResult result) {}
    default void onTestFailure(ITestResult result) {}
    default void onTestSkipped(ITestResult result) {}
    // и ещё несколько методов — все default, переопределяешь только нужные
}
```

`ITestResult` — объект с информацией о конкретном прогоне теста: имя метода (`result.getName()` / `result.getMethod().getMethodName()`), статус (`result.getStatus()`), исключение, если было (`result.getThrowable()`).

Подписка происходит не через `.subscribe(...)` в коде, а декларативно — аннотацией:

```java
@Listeners(ScreenshotOnFailureListener.class)
public class BaseTest {
    ...
}
```

TestNG сам находит эту аннотацию через рефлексию, создаёт экземпляр `ScreenshotOnFailureListener` и дёргает его методы в нужные моменты — по духу то же самое `button.subscribe(...)`, только механизм подписки — аннотация вместо явного вызова метода.

**Важно, где стоит `@Listeners`:** на `BaseTest`, а не на каждом тестовом классе — благодаря наследованию (P6-T1) все пять тестовых классов получают подписку автоматически, ни один файл в `elements`/`pages` не редактируется.

## 3. `Selenide.screenshot(String fileName)`

Статический метод Selenide, который делает скриншот текущего состояния браузера и сохраняет его (по умолчанию — в `build/reports/tests/`, ту же папку, где мы уже видели автоматические скриншоты Selenide при падении `shouldBe`/`shouldHave`). Требует, чтобы браузер был открыт в момент вызова — если тест упал ДО открытия браузера (например, сама `Selenide.open(...)` не выполнилась), скриншот в буквальном смысле нечего снимать.

## 4. Два способа узнать, что тест упал — не одно и то же

TestNG умеет подставлять `ITestResult` не только в методы `ITestListener`, но и напрямую как параметр в `@AfterMethod`:

```java
@AfterMethod(alwaysRun = true)
public void afterMethod(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        Selenide.screenshot("onFailure");
    }
}
```

Результат для одного конкретного тестового класса — тот же самый. Разница — в том, ОТКУДА это поведение может быть подключено:

- `@AfterMethod` — часть lifecycle конкретного класса (или унаследована от родителя, как наш `BaseTest`). Работает только там, где есть наследование от этого родителя.
- `@Listeners` — можно повесить не только на класс, но и на весь suite целиком через `testng.xml`, независимо от того, как устроена иерархия наследования тестовых классов. Слушатель в этом смысле более развязан (decoupled) с конкретной иерархией — TestNG уведомляет его о событии как о факте прогона, а не как о шаге lifecycle конкретного класса.

## 5. `ITestListener` — не единственный «наблюдатель» в TestNG (замер P11-T2, 2026-08-20)

`ITestListener.onTestFailure(...)` узнаёт о падении ПОСЛЕ того, как TestNG уже полностью обработал результат метода — включая уведомление всех остальных `ITestListener`, зарегистрированных раньше. Порядок регистрации не то же самое, что порядок `@Listeners` в коде: слушатели, подключённые через `META-INF/services/org.testng.ITestNGListener` (так подключён сторонний `AllureTestNg` — из своего же jar'а, и так же в этом проекте подключены `RetryAnnotationTransformer`/`TestAccountFixture`), встают в список раньше слушателей, объявленных через `@Listeners` на классе.

Практическое следствие, подтверждённое изолированным экспериментом: если `AllureTestNg` успевает закрыть Allure-результат теста ДО того, как отработает наш `ITestListener.onTestFailure`, любой `Allure.addAttachment(...)`, вызванный оттуда, не попадёт в отчёт — файл на диск запишется, а ссылки на него в результате не будет (см. [P11-T2](../curriculum/P11-T2-allure-hardening.md)).

Другой интерфейс с похожей ролью — `IInvokedMethodListener`:

```java
public interface IInvokedMethodListener {
    default void beforeInvocation(IInvokedMethod method, ITestResult testResult) {}
    default void afterInvocation(IInvokedMethod method, ITestResult testResult) {}
}
```

Он не «слушает событие после факта», а **оборачивает сам вызов** тестового (или configuration-) метода — `afterInvocation` срабатывает сразу после того, как метод завершился (успешно или с исключением), но раньше, чем TestNG успевает пройтись по списку `ITestListener`. На практике это означает: то, что не получается надёжно сделать из `onTestFailure`, стоит проверить через `afterInvocation` — порядок относительно `AllureTestNg` там другой.
