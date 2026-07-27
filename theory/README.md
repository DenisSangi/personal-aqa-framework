# theory/

Самостоятельные теоретические конспекты. Каждый — законченный артефакт (не абзац внутри задачи), на который ссылаются файлы-уроки из `../curriculum/`.

## Индекс

- [maven-basics.md](maven-basics.md) — GAV-координаты, dependency vs plugin, scope, build lifecycle, структура папок, версия Java в pom.xml. Опора для [P0-T1](../curriculum/P0-T1-repo-maven-skeleton.md).
- [config-resolution.md](config-resolution.md) — `System.getenv()` vs `System.getProperty()`, порядок приоритета источников конфигурации, `Properties`+classpath, параллель с `Optional`. Опора для [P1-T1](../curriculum/P1-T1-config-facade.md).
- [random-and-threadlocalrandom.md](random-and-threadlocalrandom.md) — `java.util.Random` vs `ThreadLocalRandom`, проблема общего состояния при параллелизме, генерация случайных строк через диапазоны символов без внешней библиотеки. Опора для [P2-T1](../curriculum/P2-T1-random-generators.md).
- [datetime-formatting.md](datetime-formatting.md) — `LocalDateTime`/`DateTimeFormatter`, буквы паттерна (`MM` vs `mm`), роль `Locale`, потокобезопасность `DateTimeFormatter` vs `SimpleDateFormat`. Опора для [P2-T2](../curriculum/P2-T2-datetime-util.md).
