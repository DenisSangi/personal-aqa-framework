# TestNG lifecycle и Template Method через наследование тестов

Опора для [P6-T1](../curriculum/P6-T1-base-test-lifecycle.md).

## 1. Порядок выполнения хуков TestNG

```
@BeforeSuite     ← один раз за весь прогон (все классы, все тесты)
  @BeforeClass   ← один раз перед первым @Test КОНКРЕТНОГО класса
    @BeforeMethod ← перед КАЖДЫМ @Test этого класса
      @Test
    @AfterMethod  ← после КАЖДОГО @Test этого класса
  @AfterClass    ← один раз после последнего @Test класса
@AfterSuite      ← один раз в самом конце
```

Если в классе три `@Test`-метода — `@BeforeMethod`/`@AfterMethod` выполнятся три раза (по разу на каждый), а `@BeforeClass`/`@AfterClass` — один раз, независимо от количества тестов в классе.

Наглядно на нашем случае: у `TableElementTest` один `@Test`, у `BaseNavigationTest` — два (`baseNavigationTest`, `loginFlowTest`). Если оба класса унаследуют `@AfterMethod` из `BaseTest`, `Selenide.closeWebDriver()` вызовется один раз после `TableElementTest`-теста и **дважды** — по разу после каждого `@Test` в `BaseNavigationTest`.

## 2. Зачем `alwaysRun = true`

По умолчанию TestNG может **пропустить** `@AfterMethod`/`@AfterClass`, если соответствующий `@Before*`-хук упал с исключением — логика TestNG: "раз подготовка не удалась, откатывать нечего".

Проблема: `@AfterMethod` в нашем случае — это `Selenide.closeWebDriver()`. Если, например, у нас появится `@BeforeMethod`, который упадёт (допустим, сайт недоступен и `Selenide.open(...)` кидает исключение) — без `alwaysRun = true` браузер, который `@BeforeMethod` всё-таки успел открыть до падения, останется висеть открытым, потому что teardown не запустится. При последовательном прогоне десятков тестов это утечка процессов браузера.

`alwaysRun = true` говорит TestNG: "выполнить этот хук в любом случае, даже если предыдущий шаг lifecycle упал". Правило простое: **любой teardown-хук (`@After*`) должен помечаться `alwaysRun = true`**, если он освобождает ресурс (закрывает браузер, соединение, файл) — иначе освобождение ресурса становится зависимым от того, успешно ли прошла подготовка, что логически не связано.

## 3. Template Method через наследование тестовых классов — изолированный пример

Прежде чем смотреть на Selenide/TestNG, минимальный пример без них вообще:

```java
abstract class Recipe {
    final void cook() {           // "скелет" — фиксированная последовательность шагов
        prepare();
        cookMainStep();            // единственный шаг, который меняется от рецепта к рецепту
        serve();
    }
    private void prepare() { System.out.println("heat the pan"); }
    private void serve()   { System.out.println("put on a plate"); }
    abstract void cookMainStep();  // "дырка" в скелете — заполняет наследник
}

class Omelette extends Recipe {
    void cookMainStep() { System.out.println("whisk eggs, fry 2 min"); }
}

class Pancake extends Recipe {
    void cookMainStep() { System.out.println("pour batter, flip once"); }
}
```

`cook()` — метод шаблона: он **финальный** (или хотя бы не переопределяется), задаёт порядок раз и навсегда. Каждый наследник получает `prepare()`/`serve()` бесплатно, не может их случайно забыть — и переопределяет только ту часть, которая у него действительно своя.

## 4. Перенос на тестовые классы

Разница с примером выше только в том, что "скелетом" управляет не наш код напрямую, а TestNG через рефлексию по аннотациям:

```java
public class BaseTest {
    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}

public class InputElementTest extends BaseTest {
    @Test
    public void testSetValue() { ... }   // конкретный класс добавляет только это
}
```

`InputElementTest` не объявляет `tearDown()` вообще — TestNG находит его в родителе `BaseTest` через наследование и вызывает после каждого `@Test` из `InputElementTest`, как будто он объявлен прямо в этом классе. Это и есть Template Method: `BaseTest` фиксирует "что происходит после теста" один раз, все наследники получают это поведение автоматически, без copy-paste и без риска забыть скопировать в очередной новый класс.

Реальный проект идёт на шаг дальше — `GreenphireBaseTest` (`gp-test-core`) даёт общий для всех продуктов lifecycle, а `ClinCardBaseTest` наследуется от него и добавляет специфику одного конкретного продукта (свой логин, свой cleanup) — то есть цепочка наследования может быть не в два, а в три и больше уровней, каждый добавляет свой слой к общему скелету.
