# record vs Lombok @Builder, и Factory

Опора для [P7-T2](../curriculum/P7-T2-account-model-factory.md).

## 1. `record` — что это и откуда взялось

Обычный класс данных до `record` требовал писать вручную (или генерировать через IDE/Lombok) конструктор, геттеры, `equals`/`hashCode`/`toString`:

```java
public final class Point {
    private final int x, y;
    public Point(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
    // + equals, hashCode, toString вручную
}
```

`record` (Java 16+) генерирует всё это автоматически по списку полей — **компактно**:

```java
public record Point(int x, int y) {}
```

Использование: `new Point(1, 2)`, доступ к полям — `point.x()`, `point.y()` (не `getX()` — без префикса `get`, это сознательное отличие от Java Bean-конвенции).

**Ключевое свойство: `record` неизменяем (immutable) и требует ВСЕ поля в конструкторе.** Нет способа сказать "это поле необязательное, вот значение по умолчанию" средствами самого `record` — либо передаёшь значение, либо пишешь отдельный (перегруженный) конструктор вручную:

```java
public record Account(String name, String company) {
    public Account(String name) {
        this(name, "");   // "компактный" вызов канонического конструктора с дефолтом
    }
}
```

При 2 полях это терпимо. При 17 (как в `AccountModel`) — придётся писать много таких перегрузок вручную, чтобы получить гибкость, которую `@Builder` даёт "из коробки".

## 2. Lombok `@Builder` / `@Builder.Default` — напоминание

Уже упоминалось в `Framework explanation/4-api-services-layer.md` (`ClinCardSiteModel`), здесь — подробнее, с изолированным примером:

```java
@Getter
@Builder
public class Account {
    private String name;
    @Builder.Default
    private String company = "";   // используется, только если builder НЕ передал своё значение
}
```

```java
Account a = Account.builder()
    .name("Denis")
    // .company(...) не вызван — останется "" из @Builder.Default
    .build();
```

Без `@Builder.Default` поле, не заданное через builder, получило бы Java-дефолт по типу (`null` для `String`, `0` для `int`) — не то значение, которое ты явно хотел. `@Builder.Default` — способ сказать "если вызывающий код не укажет своё значение, используй вот это", прямо в объявлении поля, а не в отдельном конструкторе.

`toBuilder = true` — отдельная опция, позволяющая получить builder из уже готового объекта (`existingAccount.toBuilder().email("new@test.com").build()`) — immutable update: не меняем исходный объект, создаём новый с одним изменённым полем.

## 3. Когда какой вариант

| | `record` | `@Builder` |
|---|---|---|
| Все поля обязательны, нет естественных дефолтов | ✅ хорошо подходит | избыточно |
| Много полей, часть — почти всегда одно и то же значение | нужно писать перегрузки руками | `@Builder.Default` — то, для чего он создан |
| Нужна read-only модель без "конструирования по шагам" | ✅ проще | избыточно многословно |
| Нужно собирать объект по одному полю за раз, по-разному в разных тестах | неудобно (нужно всё сразу) | ✅ ровно для этого и придуман |

## 4. Factory — короткое напоминание, не с нуля

Идея паттерна Factory (`design-patterns.md` #8): вместо того, чтобы вызывающий код каждый раз вручную собирал сложный объект с нуля, есть отдельный метод/класс, который знает "разумные дефолты" и возвращает готовый результат — вызывающий код передаёт только то, что для него действительно важно в конкретном сценарии.

Изолированный пример, без API:

```java
class Pizza {
    String size, topping;
}

class PizzaFactory {
    static Pizza standardMargherita() {
        Pizza p = new Pizza();
        p.size = "medium";
        p.topping = "tomato+cheese";
        return p;
    }
}
```

Вызывающему коду не нужно знать, что "стандартная маргарита" — это `size=medium` — он просто зовёт `PizzaFactory.standardMargherita()`. Ровно то же самое `AccountFactory` будет делать для `AccountModel`: скрывать 17 полей за одним вызовом, который отдаёт валидный, готовый к отправке объект с уникальными email/именем.
