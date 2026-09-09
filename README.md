# personal-aqa-framework

A UI + API test automation framework built from scratch, reproducing the architecture and
patterns of a production project. The application under test is the public demo site
[automationexercise.com](https://automationexercise.com).

The goal was not to cover a site, but to build the framework itself: layers, configuration,
test data lifecycle, reporting and CI — every technical decision made and owned by me.

## Stack

| Area | Tool |
|---|---|
| Language / build | Java 17, Maven |
| Test runner | TestNG 7.10.1 |
| UI | Selenide 7.2.3 |
| API | REST Assured 6.0.1 |
| Database | PostgreSQL 16 (JDBC driver 42.7.13) |
| Reporting | Allure 2.35.3 (TestNG, Selenide and REST Assured integrations) |
| Logging | Logback |
| CI | Jenkins controller + agent, both in Docker |
| Misc | Lombok, Jackson, Jsoup, AspectJ weaver |

## Architecture

```
src/main/java
├── config           — configuration resolved from environment, system properties or a file
├── elements         — custom wrappers over raw web elements (button, input, dropdown, table)
├── pages            — Page Objects built on top of the element layer
├── api
│   ├── core         — REST client
│   ├── services     — API services used for preconditions and test data
│   └── models       — request/response models with builders
├── utils            — database connection, date/time helpers, random data generator
├── reusableactions  — composite actions shared across tests
└── constants
```

Two ideas drive this structure:

- **Tests read as plain language.** All technical machinery lives inside the element and page
  layers, so a test says *open the cart, add a product, check the total* — nothing else.
  A new engineer writes a test on day one instead of studying the framework for a week.
- **Preconditions go through the API, verification happens in the UI.** Setting up state
  through the interface is slow and fragile; the API layer creates it directly.

Each test creates its own data, performs its actions and cleans up after itself, so tests
do not depend on execution order or on a shared database state.

## Test suites

Two suites are defined in `src/test/resources/test-suites/`:

| Suite | Purpose | Content |
|---|---|---|
| `smoke.xml` | fast feedback on the critical path | login and the main product flow, methods run in parallel |
| `regression.xml` | full check before a release | 15 classes: UI flows, API services, database, element layer, utilities |

The regression suite runs **34 tests across 15 classes**. Note what the regression
suite covers: not only business scenarios, but the framework's own building blocks — the
element wrappers, the database utility and the data generators are tested too, because a bug
there produces false failures everywhere else.

## Running the tests

Regression suite (the default):

```bash
mvn clean test
```

Smoke suite:

```bash
mvn clean test -Dsurefire.suiteXmlFile=src/test/resources/test-suites/smoke.xml
```

The suite is passed as a Maven property, so CI can run a different set without editing
`pom.xml`.

Allure report:

```bash
mvn allure:serve
```

## Database

Some tests verify records directly in PostgreSQL. The database runs in Docker:

```bash
docker compose up -d
```

Copy `dbconnect.example.properties` to `src/main/resources/dbconnect.properties` and fill in
your own values — the real file is git-ignored and never committed.

## CI

`docker-compose-jenkins.yml` starts a Jenkins controller (`jenkins/jenkins:lts`) and builds an
agent image from `Dockerfile.agent`. The agent uses the inbound scheme: it initiates the
connection to the controller, so no ports are published for it — only the controller's web
interface is exposed.

The pipeline in `Jenkinsfile` is parameterised:

- **`SUITE`** — `smoke` or `regression`, passed straight into the Maven property, so the same
  pipeline serves both fast feedback and a full pre-release run;
- **`ENVIRONMENT`** — selects the base URL from a map, so adding a staging environment is
  one line, not a new pipeline;
- the browser runs headless (`-Dselenide.headless=true`) on the agent;
- the database password comes from **Jenkins credentials**, never from the repository —
  only the non-secret connection details are in the pipeline;
- the Allure report is published in `post { always }`, so a failed run still produces a report.

## Flaky test handling

`RetryOnFailureAnalyzer` retries a failed test up to three times, and
`RetryAnnotationTransformer` applies it to every test without touching the tests themselves.
Both are registered through the TestNG service loader
(`META-INF/services/org.testng.ITestNGListener`), so no listener declaration is needed in the
suite files.

The important part is that it does **not** retry everything. A retry only happens when the
failure is recognised as transient — for example an `ElementClickInterceptedException` caused
by an overlay intercepting the click. A real assertion failure is never retried, because a
retry loop that hides genuine defects is worse than a flaky test: it turns a red build into
a silent one.
