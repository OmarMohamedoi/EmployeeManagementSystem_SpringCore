# Lab 6 — Spring Core (XML-Based Dependency Injection)

## Structure

```
lab6-spring-core/
  lab6-spring-core-guide.md   ← Obsidian-formatted guide with all 8 exercises + collapsible solutions
  starter/                    ← Maven project with TODOs (won't compile/run correctly until filled in)
  solution/                   ← fully working Maven project
```

## Important Difference From Labs 1–5

Unlike the earlier labs (functional interfaces, streams, generics, records), **this lab requires Spring as a dependency**, so plain `javac` won't work. Both `starter/` and `solution/` are full Maven projects with a `pom.xml` that pulls in `spring-context`.

## How to Run

You need [Maven](https://maven.apache.org/) installed, and an internet connection the first time (to download Spring's jars).

```bash
cd solution   # or starter, once the TODOs are filled in
mvn compile exec:java -Dexec.mainClass="com.example.MainApp"
```

Or, if you prefer an IDE (IntelliJ, Eclipse, VS Code with the Java extensions): open the `solution/` (or `starter/`) folder as a Maven project, let it download dependencies, and run `MainApp.java` directly.

## What's Wired Up

The `solution/` project's `MainApp` runs through every exercise in sequence when executed, printing labeled sections to the console:

- **6.1** — retrieves and uses the `greeter` bean
- **6.2 / 6.3 / 6.4 / 6.6** — retrieves the `car` bean (mixing constructor injection for `Notifier`, setter injection for `Engine`, and simple value injection for `model`/`year`)
- **6.5** — proves singleton scope by comparing two `getBean("engine")` calls with `==`
- **6.7** — eager initialization is visible from the very first line: `"Engine bean created"` prints as soon as the context loads, before any `getBean()` call
- **6.8** — runs the full `OrderService → PaymentService → Notifier` capstone chain

## Exercise Index

| Exercise | Concept | File(s) |
|---|---|---|
| 6.1 | Your first bean | `Greeter.java` |
| 6.2 | Constructor injection | `Engine.java`, `Car.java` |
| 6.3 | Setter injection | `Car.java` |
| 6.4 | Injecting simple values | `Car.java` |
| 6.5 | Singleton scope | `MainApp.java` |
| 6.6 | Swapping implementations | `Notifier.java`, `EmailNotifier.java`, `SmsNotifier.java` |
| 6.7 | Eager vs. lazy initialization | `Engine.java`, `MainApp.java` |
| 6.8 | Capstone: 3-layer wiring | `OrderService.java`, `PaymentService.java` |

All XML wiring lives in `src/main/resources/applicationContext.xml`.
