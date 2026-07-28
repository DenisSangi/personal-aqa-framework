# Selenide-основы + self-bounded generics (CRTP)

Нужны для Задачи [P3-T1](../curriculum/P3-T1-base-element-input.md).

## Selenide: минимум для старта

Selenide — обёртка над Selenium с "zero-configuration" браузером: не нужно вручную поднимать `WebDriver`, он стартует лениво при первом обращении к элементу.

```java
import static com.codeborne.selenide.Selenide.*;

open("https://automationexercise.com"); // открывает браузер (Chrome по умолчанию) и переходит по URL
SelenideElement element = $x("//input[@id='some_id']"); // находит элемент (лениво — до первого действия ничего не ищет)
element.shouldBe(Condition.visible); // ждёт видимости с дефолтным timeout
element.setValue("текст");
```

`$(...)` — по CSS-селектору, `$x(...)` — по XPath. `SelenideElement` — не сам `WebElement`, а обёртка с встроенными ожиданиями (`.should*` методы сами ждут условие вместо мгновенной проверки — в этом ключевое отличие от чистого Selenium, где `findElement` либо сразу нашёл, либо кинул исключение).

## Проблема: `this` в цепочке методов теряет тип наследника

Изолированный пример (не про Selenide, чистая Java):

```java
class Animal {
    Animal eat() { System.out.println("eating"); return this; }
}

class Dog extends Animal {
    Dog bark() { System.out.println("bark"); return this; }
}

Dog dog = new Dog();
dog.eat().bark(); // ОШИБКА КОМПИЛЯЦИИ: eat() возвращает Animal, а у Animal нет метода bark()
```

`this` внутри `eat()` физически является объектом `Dog`, но объявленный тип возврата — `Animal`. Компилятор видит только объявленный тип, поэтому `.bark()` после `.eat()` не существует с его точки зрения.

## Решение: self-bounded generic (Curiously Recurring Generic Pattern)

```java
class Animal<T extends Animal<T>> {
    @SuppressWarnings("unchecked")
    T eat() { System.out.println("eating"); return (T) this; }
}

class Dog extends Animal<Dog> {
    Dog bark() { System.out.println("bark"); return this; }
}

Dog dog = new Dog();
dog.eat().bark(); // компилируется: eat() теперь возвращает Dog
```

`T extends Animal<T>` — базовый класс параметризован СВОИМ ЖЕ будущим наследником. `Dog extends Animal<Dog>` подставляет себя же в качестве `T`. Теперь `eat()` объявлен как возвращающий `T`, а `T` для `Dog` — это и есть `Dog`.

`(T) this` — непроверяемое приведение (`unchecked cast`), компилятор не может гарантировать на уровне типов, что `this` реально является экземпляром `T` — это держится на дисциплине: контракт `class Dog extends Animal<Dog>` (а не, например, по ошибке `class Dog extends Animal<Cat>`) никто, кроме программиста, не проверяет. Отсюда `@SuppressWarnings("unchecked")` — осознанное подавление предупреждения, а не баг.

## Как это применяется в `BaseElement`

`BaseElement<T extends BaseElement<T>>` — та же схема. `InputElement extends BaseElement<InputElement>`. Метод вроде `waitAndGetElement()` объявлен в базовом классе как возвращающий `T`, поэтому вызванный на `InputElement` он возвращает именно `InputElement`, а не голый `BaseElement` — и все специфичные для `InputElement` методы остаются доступны дальше в цепочке.

## Template Method — где здесь этот паттерн

Базовый класс задаёт "прелюдию" (`waitAndGetElement()` — дождаться существования, проскроллить, дождаться видимости), а наследник вызывает её первым шагом внутри своего специфичного действия (например, `InputElement.setValue(...)` сначала зовёт `waitAndGetElement()`, потом уже делает `element.setValue(...)`). Логика ожидания живёт в одном месте — не копируется в каждый конкретный класс-обёртку.
