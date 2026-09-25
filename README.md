# Employee Management System — Spring Core Mini Project

A lightweight, robust **Spring Core (non-Boot)** application built to demonstrate mastery of the Spring IoC container, Dependency Injection, bean scopes, lifecycle management, and enterprise-grade design patterns.

---

## 🚀 Key Features & Spring Core Concepts Demonstrated

* **Inversion of Control (IoC) & Dependency Injection (DI):** Fully managed by the Spring container using constructor injection for loose coupling and immutability.
* **Component Scanning & Java-Based Configuration:** Combines `@Configuration` / `@Bean` definitions with stereotype annotations (`@Service`, `@Repository`, `@Component`).
* **Environment Profiles (`@Profile`):** Dual repository implementation switching seamlessly between an in-memory store (`dev`) and a file-backed store (`prod`).
* **Collection Injection & Ordering:** Automatically injects all `Notifier` implementations into a `List` within `NotificationManager`, enforcing execution sequence using `@Order`.
* **Custom Validation & Exception Handling:** Enforces business rules via `EmployeeValidator`, throwing a custom unchecked `InvalidEmployeeException`.
* **The Scoped-Bean Problem & Prototypes:** Resolves the singleton-to-prototype dependency hurdle using `ObjectProvider<AuditLogger>`, guaranteeing fresh instances on every call.
* **Externalized Configuration:** Loads custom properties from `application.properties` using `@PropertySource` and `@Value` to enforce constraints like maximum raise limits.
* **Bean Lifecycle Callbacks:** Utilizes `@PostConstruct` for initialization hooks and `@PreDestroy` for clean resource teardown upon application context closure.

---

## 📂 Project Structure

```text
com.example/
├── MainApp.java                    # Entry point & end-to-end demonstration
├── config/
│   └── AppConfig.java              # Java-based configuration & property source
├── entity/
│   └── Employee.java               # Domain model (id, name, department, salary)
├── repository/
│   ├── EmployeeRepository.java     # Repository interface
│   ├── EmployeeRepoImpl.java       # Dev profile implementation (In-Memory)
│   ├── FileMemoryEmployeRepo.java  # Prod profile implementation (File-backed)
│   └── InMemoryEmployeRepo.java    # Supporting data holder bean
├── Service/
│   ├── EmployeeService.java        # Service interface
│   ├── EmployeeServiceImpl.java    # Core business logic & validators
│   ├── EmployeeValidator.java      # Validation logic component
│   └── InvalidEmployeeException.java # Custom runtime exception
├── notify/
│   ├── Notifier.java               # Notification interface
│   ├── SmsNotifier.java            # @Order(1)
│   ├── PushNotifier.java           # @Order(2)
│   ├── EmailNotifier.java          # @Order(3)
│   └── NotifyManager.java          # Collection injection manager
└── audit/
    └── AuditLogger.java            # Prototype-scoped audit logger
```
## 🏛️ Architectural Design & Justifications

### 1. Bean Scopes: Singleton vs. Prototype
* **Singletons (`@Service`, `@Repository`, `NotificationManager`):** Default Spring scope. Used for stateless components and shared services to optimize memory and startup performance.
* **Prototype (`AuditLogger`):** Configured with `@Scope("prototype")`. A new instance is explicitly requested and generated every time an audit log is triggered, ensuring distinct timestamps and object identities.

### 2. Solving the Scoped-Bean Problem
Because `EmployeeServiceImpl` is a **Singleton**, injecting a prototype `AuditLogger` directly via standard `@Autowired` would lock in a single audit instance for the entire application lifecycle. 
* **Solution:** We utilized `ObjectProvider<AuditLogger>`, allowing the service to dynamically request a fresh prototype instance (`loggerManager.getObject()`) on every employee creation.

### 3. Dependency Injection Strategy
* **Constructor Injection** was chosen across all primary components (`EmployeeServiceImpl`, repositories, validators). This ensures mandatory dependencies cannot be `null`, promotes immutability, and makes unit testing significantly easier.

### 4. Environment Profiles (`dev` vs. `prod`)
* By annotating `EmployeeRepoImpl` with `@Profile("dev")` and `FileMemoryEmployeRepo` with `@Profile("prod")`, Spring dynamically provisions the appropriate data persistence mechanism based solely on the active environment profile set in `MainApp`.

---

## 🛠️ How to Run the Application

1. Open the project in your preferred IDE (e.g., IntelliJ IDEA).
2. Ensure dependencies (`spring-context`, `jakarta.annotation-api`) are loaded via Maven/Gradle.
3. Open `MainApp.java` and choose your active profile:
   ```java
   context.getEnvironment().setActiveProfiles("dev"); // or "prod"
Open MainApp.java and choose your active profile:

Java
context.getEnvironment().setActiveProfiles("dev"); // or "prod"
Run MainApp.main() to execute the full suite of validations, raises, collection notifications, prototype hash verifications, and lifecycle teardowns!
