# curriculum/

Задачи-уроки. Один файл на задачу, создаётся в момент, когда задача реально даётся Денису (не заранее списком — см. `../PLAN.md` для дорожной карты id/названий).

## Схема файла-урока

```
# <id> — <заголовок>

**Фаза / слой:** ...
**Теги:** java / selenide / testng / rest-assured / sql / jenkins (можно несколько)

## Описание
Что построить.

## Место в фреймворке
Зависимости вверх/вниз — от чего опирается эта задача, что будет опираться на неё.

## Мотивировка
Зачем + цена отказа (в духе разделов «Без паттерна» из design-patterns.md).

## Теория
Нужные концепции + ссылка на файл в ../theory/.

## Definition of Done
Критерии приёмки.

## Типичные ошибки
Заполняется ПОСЛЕ ревью — реальные ошибки Дениса на этой задаче. Самая ценная секция для будущего веб-приложения.

## Ссылка на реальный код java-selenide-pp
Абсолютный путь к файлу(ам)-прообразу.
```

## Индекс

- [P0-T1 — Скелет репозитория и Maven-тулинга](P0-T1-repo-maven-skeleton.md) — ✅ аппрувед 2026-07-23.
- [P1-T1 — Config Facade + порядок разрешения параметров](P1-T1-config-facade.md) — ✅ аппрувед 2026-07-24.
- [P2-T1 — RandomGenerators](P2-T1-random-generators.md) — ✅ аппрувед 2026-07-27.
- [P2-T2 — DateTimeUtil](P2-T2-datetime-util.md) — ✅ аппрувед 2026-07-28.
- [P3-T1 — BaseElement + InputElement](P3-T1-base-element-input.md) — ✅ аппрувед 2026-07-28.
- [P3-T2 — ButtonElement / DropdownElement / TableElement](P3-T2-button-dropdown-table.md) — ✅ аппрувед 2026-07-28.
- [P4-T1 — HomePage/LoginPage + навигационный переход](P4-T1-home-login-page.md) — ✅ аппрувед 2026-07-29.
- [P4-T2 — Полноценный LoginPage + сквозной сценарий логина](P4-T2-login-scenario.md) — ✅ аппрувед 2026-07-29.
- [P5-T1 — LoginReusableActions](P5-T1-login-reusable-actions.md) — ✅ аппрувед 2026-07-29.
- [P6-T1 — BaseTest (Template Method) + lifecycle](P6-T1-base-test-lifecycle.md) — ✅ аппрувед 2026-07-30.
- [P6-T2 — Listener на падение теста (скриншот)](P6-T2-failure-screenshot-listener.md) — ✅ аппрувед 2026-07-30 (закрыта живой сессией в тот же день, запись обнаружена и восстановлена задним числом в Сессии 7).
- [P7-T1 — RestClient adapter + LoginApiService](P7-T1-rest-client-login-service.md) — ✅ аппрувед 2026-07-31.
- [P7-T2 — AccountModel (Builder) + AccountApiService + AccountFactory](P7-T2-account-model-factory.md) — ✅ аппрувед 2026-07-31.
- [P8-T1 — @DataProvider + параметризованный тест](P8-T1-dataprovider.md) — ✅ аппрувед 2026-08-01. Там же — решение по P8-T2: отложена до реальной потребности (критерий ≥3-4 связанных сущностей не выполняется), Фаза 8 закрыта.
