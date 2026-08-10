# P4-T2 — Полноценный LoginPage + сквозной сценарий логина (LoginPage → HomePage)

**Фаза / слой:** Фаза 4 — Page Object
**Теги:** java, selenide

## Описание

Закрываем Фазу 4. Два направления работы:

1. **`LoginPage` доводится до полноценного состояния:** добавляются поле пароля (`InputElement`, локатор `input[data-qa='login-password']`), поле кнопки логина (`ButtonElement`, локатор `button[data-qa='login-button']`), fluent-сеттеры `setEmail(String)`/`setPassword(String)` (каждый возвращает `this`, как `verifyPageIsOpen()`) и навигационный метод `clickLoginButton()`.
2. **`clickLoginButton()` — это переход, а не действие на месте.** Успешный логин на `automationexercise.com` реально уводит браузер обратно на главную страницу (проверено вживую: после логина URL становится `automationexercise.com/`, в шапке появляется `Logged in as <Username>` + ссылка `/logout`, а прежняя ссылка `Signup / Login` пропадает из DOM). Значит `clickLoginButton()` должен возвращать `new HomePage()` — точно та же логика, что `HomePage.clickSignupLoginLink()` в P4-T1.

Реальные тестовые данные уже есть в проекте и уже проверены рабочими — `FrameworkConfig.APP_USER_EMAIL`/`APP_USER_PASSWORD` (см. `framework.properties`) уже логинили тебя по-настоящему в `TableElementTest` (Фаза 3, P3-T2). Ничего нового придумывать не нужно, credentials переиспользуются как есть.

## Место в фреймворке

Зависимость: опирается на P4-T1 (`HomePage`/`LoginPage` уже существуют, `BaseElement.shouldBe(...)` только что доработан — пригодится напрямую в этой задаче) и на Фазу 1 (`FrameworkConfig` для credentials).

От этой задачи зависит вся Фаза 5 (Reusable Actions — первый сценарный хелпер "открыть сайт → залогиниться" будет оркестровать именно эту пару POM) и Фаза 6 (базовый тестовый класс, скорее всего, будет логиниться в `@BeforeMethod` через готовый POM-путь).

## Мотивировка

Без второй (полноценной) страницы Page Object в этом проекте демонстрирует только "туда" (HomePage → LoginPage), но не "туда и обратно" — а реальные сценарии в `java-selenide-pp` почти всегда многошаговые цепочки через несколько POM подряд (см. `ClinCardLoginPage.clickSubmitButton()` → `ClinCardLookUpParticipantPage`, а дальше цепочка идёт ещё дальше). Не закрыв этот цикл сейчас, Фаза 5 (Reusable Actions) не может начаться — оркестровать нечего, если POM только открывает страницы, но не логинится по-настоящему.

Отдельная, более тонкая причина взять именно логин (а не что-то попроще): он даёт первый в проекте случай, когда **одна и та же страница (тот же URL, тот же класс `HomePage`) существует в двух разных DOM-состояниях** — до логина и после. Это заставляет явно решить: что вообще значит "страница открыта" в такой ситуации, и годится ли для этого случая уже существующий `verifyPageIsOpen()`.

## Открытые вопросы (обсудим перед кодом)

**Вопрос 1 — какая верификация нужна на HomePage после логина?**
`HomePage.verifyPageIsOpen()` сейчас проверяет `signupLoginLink.shouldBe(clickable)` — а после логина этой ссылки `a[href='/login']` уже физически нет в DOM (её место в шапке занимает `Logged in as <Username>` + ссылка `/logout`). Что если после `clickLoginButton()` вызвать существующий `verifyPageIsOpen()` — что произойдёт? Нужен ли для проверки факта логина **отдельный** метод, или существующий можно как-то переиспользовать?

**Вопрос 2 — нужно ли явно ждать редирект внутри `clickLoginButton()`?**
В `TableElementTest` (P3-T2) после `loginButton.click()` пришлось добавить `Selenide.webdriver().shouldHave(url(FrameworkConfig.APP_URL + "/"))` ПЕРЕД следующим действием — иначе ловили race condition (сабмит логина и следующая явная навигация "гонялись" за редиректом). Нужно ли то же самое здесь, в `clickLoginButton()`, перед `return new HomePage()`? Вспомни, что мы разбирали в теории P4-T1 про Lazy Initialization и то, ПОЧЕМУ `return new NextPage()` обычно не требует явного ожидания редиректа — тот же аргумент применим здесь, или ситуация в `TableElementTest` отличалась чем-то важным?

## Теория

Новой теории не нужно — обе идеи уже разобраны: Fluent Interface и навигация `return new NextPage()` — в [theory/06-page-object-and-navigation.md](../theory/06-page-object-and-navigation.md) (P4-T1), Lazy Initialization — там же (Вопрос 2 выше — применение той же идеи в НОВОМ контексте, не повтор объяснения).

## Definition of Done

- [x] `LoginPage` — поля `passwordInput` (`InputElement`, `input[data-qa='login-password']`) и `loginButton` (`ButtonElement`, `button[data-qa='login-button']`), локаторы инлайн (без констант — та же логика, что в P4-T1: локаторы фиксированные).
- [x] `LoginPage.setEmail(String)` / `setPassword(String)` — fluent, каждый возвращает `this`.
- [x] `LoginPage.clickLoginButton()` — навигационный метод, возвращает `new HomePage()`.
- [x] Осознанное решение по Вопросу 1 — отдельный новый метод `HomePage.verifyLoggedUsername(String username)`, `verifyPageIsOpen()` не тронут и сохранил исходный смысл.
- [x] Осознанное решение по Вопросу 2 — без явного ожидания внутри `clickLoginButton()`; обоснование дано верно (см. «Типичные ошибки» — race condition в `TableElementTest` был вызван явным `Selenide.open(...)` сразу после клика, у нас такого вызова нет).
- [x] Живой тест: полный сквозной сценарий — `Selenide.open(FrameworkConfig.APP_URL)` → `new HomePage().verifyPageIsOpen().clickSignupLoginLink()` → `.verifyPageIsOpen().setEmail(FrameworkConfig.APP_USER_EMAIL).setPassword(FrameworkConfig.APP_USER_PASSWORD).clickLoginButton()` → верификация залогиненного состояния на `HomePage`. Реальные credentials, реальный логин, реальный редирект.
- [x] `mvn clean test` — весь проект зелёный (16/16; отдельные разовые CDP/рекламные таймауты на реальном сайте не воспроизводятся стабильно — не логическая ошибка).

## Типичные ошибки

1. **`HomePage.verifyPageIsOpen()` был переписан, а не дополнен новым методом.** Первая версия ответа на Вопрос 1 заменила проверку `signupLoginLink.shouldBe(clickable)` на `Selenide.webdriver().shouldHave(url(FrameworkConfig.HOMEPAGE_URL))` — притом что сам Денис верно ответил на Вопрос 1 словами ("нужен отдельный метод, verifyPageIsOpen тут не подходит"), а в коде вместо отдельного метода получилась замена существующего. Побочный эффект: верификация факта логина вообще пропала (URL мог совпасть по любой причине), а метод перестал использовать элементную обёртку (`ButtonElement`), впервые в проекте обратившись к `Selenide.webdriver()` напрямую. Поправлено — `verifyPageIsOpen()` вернул исходный смысл, добавлен отдельный `verifyLoggedUsername(...)`.
2. **Результат `.equals(...)` не был никуда передан — третий по счёту случай той же ошибки (после P1-T1 и P2-T1).** Первая версия `verifyLoggedUsername` вызывала `loggedUsername.getText().equals("Logged in as %s".formatted(username))`, но результат (`boolean`) нигде не использовался — метод не мог провалиться независимо от реального текста на странице. Доказано эмпирически: тест прошёл зелёным (2/2) при заведомо нерабочей проверке.
3. **Локатор указывал на иконку, а не на текст.** `SelenideElement loggedUsername = element($("i[class='fa fa-user']"))` — реальный DOM (`<a><i class="fa fa-user"></i> Logged in as <b>Den</b></a>`) показывает, что у иконки `<i>` собственного текста нет вообще (`textContent === ""`), а нужная строка лежит в родительском `<a>`. Плюс поле было заведено как сырой `SelenideElement` (`Selenide.element(...)`) — впервые в проекте в обход `elements.*`-обёрток. У самого `<a>` при этом нет ни одного атрибута (ни `href`, ни `class`, ни `id`) — задача построить локатор оказалась реально нетривиальной (CSS не умеет искать "родителя элемента" и не умеет сравнивать по тексту). Денис сам вышел на решение через XPath и структуру текстовых узлов: `//a[text()=' Logged in as ']`, затем — по замечанию про хрупкость точного совпадения — на `//a[contains(text(), 'Logged in as')]`. Поле переведено на `ButtonElement` (в `BaseElement`/`ButtonElement` заодно добавлен `getText()`), локатор больше не обходит слой обёрток.
4. **После исправления локатора — искажённая логика самой проверки.** Промежуточная версия: `loggedUsername.shouldHave(text(String.valueOf(loggedUsername.getText().contains(username))))` — вычисленный `boolean` конвертировался в строку `"true"`/`"false"` и подставлялся в `Condition.text(...)`, которое сравнивает НАСТОЯЩИЙ текст элемента с переданной строкой. В итоге сравнивалось "содержит ли `'Logged in as Den'` подстроку `'true'`" — падало ВСЕГДА, даже когда логин был полностью успешным (подтверждено живым прогоном и точным текстом ошибки: `Element should have text "true"`, `Actual value: text="Logged in as Den"`). После вопроса "что именно сравнивает `text(expected)`, когда передан в `shouldHave`" Денис сам убрал лишний слой и оставил `loggedUsername.shouldHave(text(username))` — прямое и правильное использование готового API Selenide.
5. Мелкая гигиена: поле `homePage` в `BaseNavigationTest` изначально использовалось только в одном из двух тестов, второй создавал `new HomePage()` инлайн — унифицировано по замечанию.

## Ссылка на реальный код java-selenide-pp

- `px-ui-test-automation/src/main/java/pages/login/PxLoginPage.java` — короткий образец навигационного логин-метода `pxLogin(...)` → `return new OktaLoginPage()`.
- `clincard-ui-test-automation/src/main/java/com/suvoda/automation/pages/main/ClinCardLoginPage.java` — `clickSubmitButton()` → `return new ClinCardLookUpParticipantPage()`, тот же принцип "логин — это навигация, не действие на месте".
- Свой же `src/test/java/elements/TableElementTest.java` (Фаза 3, P3-T2 этого проекта) — уже рабочий логин теми же credentials, источник Вопроса 2 (race condition после логина).
