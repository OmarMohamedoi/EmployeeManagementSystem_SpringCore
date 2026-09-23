---
title: Lab - Spring Bean Lifecycle & Bean Definition (XML)
tags: [spring, spring-core, lab, xml-config, bean-lifecycle, bean-definition]
created: 2026-09-13
---

# 🧪 Lab: Bean Lifecycle & Bean Definition (XML Configuration)

> [!info] Prerequisites
> Students should have the **spring-bean-lifecycle-xml** starter project (Maven + Java 11+) with `UserService`, `UserRepository`, `MyBeanPostProcessor`, `applicationContext.xml`, and `MainApp`. All tasks below build on top of it.

---

## 🎯 Part A — Bean Lifecycle Lab Tasks

> [!abstract] Goal
> Verify, step by step, the exact order Spring executes: **Instantiation → DI → Aware → BeanPostProcessor → Init → Use → Destroy.**

### Task A1 — Baseline Run
- [ ] Run `MainApp` as-is with no changes.
- [ ] Copy the console output into your lab report.
- [ ] Number each printed line in the order it appeared.

> [!question]
> Which line executes first: the constructor or the dependency injection (`setUserRepository`)? Why does it have to be in that order?

---

### Task A2 — Remove `init-method` and `destroy-method`
- [ ] In `applicationContext.xml`, remove the `init-method="customInit"` and `destroy-method="customDestroy"` attributes from the `userService` bean.
- [ ] Re-run the app.

> [!question]
> Do lines 5 (`customInit`) and 8 (`customDestroy`) still print? Which lifecycle hooks still fire, and why?

---

### Task A3 — Remove `InitializingBean` / `DisposableBean`
- [ ] Restore Task A2's XML attributes.
- [ ] In `UserService.java`, remove `implements InitializingBean, DisposableBean` and delete the `afterPropertiesSet()` and `destroy()` methods.
- [ ] Re-run the app.

> [!question]
> Compare the output to the baseline (Task A1). What is now missing? Confirm that `customInit()` / `customDestroy()` still execute independently of the removed interfaces.

---

### Task A4 — Order When Both Hooks Exist
- [ ] Restore `UserService` to its original state (both `InitializingBean`/`DisposableBean` AND `init-method`/`destroy-method` present).
- [ ] Add a `System.out.println` with a timestamp (`System.currentTimeMillis()`) inside `afterPropertiesSet()` and inside `customInit()`.

> [!question]
> Which one runs first — `afterPropertiesSet()` or `customInit()`? Does the same order apply on the destroy side (`destroy()` vs `customDestroy()`)?

---

### Task A5 — BeanPostProcessor Scope
- [ ] Add a **second** simple bean (e.g. `OrderService`, no custom hooks at all) to `applicationContext.xml`.
- [ ] Re-run the app.

> [!question]
> Does `MyBeanPostProcessor` print before/after lines for `OrderService` too, even though you never referenced it from `MyBeanPostProcessor`? What does that tell you about how `BeanPostProcessor` is applied?

---

### Task A6 — Prototype Scope and Destruction
- [ ] Change `userService` bean's `scope` to `"prototype"` in the XML.
- [ ] In `MainApp`, call `context.getBean("userService", UserService.class)` **twice** and print `System.identityHashCode()` of each returned object.
- [ ] Call `context.close()` as usual.

> [!question]
> 1. Are the two retrieved objects the same instance or different?
> 2. Does `destroy()` / `customDestroy()` get called when the context closes? Explain why prototype-scoped beans behave this way.

---

### Task A7 — Global Default Init/Destroy Methods
- [ ] Remove the per-bean `init-method`/`destroy-method` attributes.
- [ ] Add `default-init-method="customInit"` and `default-destroy-method="customDestroy"` to the root `<beans>` tag.
- [ ] Ensure **both** `UserService` and your new bean from Task A5 have methods named `customInit`/`customDestroy` (even if empty for the second bean).
- [ ] Re-run the app.

> [!question]
> Do both beans now run their init/destroy methods without declaring the attributes individually? What happens if a bean does **not** have a method with that name — does Spring throw an error?

---

### Task A8 — Break It on Purpose (Exception Handling)
- [ ] Temporarily throw a `RuntimeException` inside `afterPropertiesSet()`.
- [ ] Re-run the app and observe the result.

> [!question]
> Does the application context start successfully? What happens to beans that were already initialized before the failing bean? (Hint: look at the stack trace and consider what `ClassPathXmlApplicationContext`'s constructor does if initialization fails.)

---

### ✅ Part A Deliverable
A short report (or Obsidian note) containing:
1. Console output for each task
2. Answers to every `[!question]`
3. A final diagram (hand-drawn or Mermaid) of the confirmed lifecycle order, annotated with **which hook is optional vs. Spring-managed automatically**

---

## 🎯 Part B — Bean Definition Attributes Lab Tasks

> [!abstract] Goal
> Configure and observe every major `<bean>` attribute Spring's `BeanDefinition` supports, using nothing but XML.

Create a fresh context file `beanDefinitionContext.xml` and a few throwaway classes (`Vehicle`, `Car`, `Engine`) for this part — keep it separate from Part A's project so the two labs don't interfere.

```java
public class Engine {
    private String type = "default-engine";
    public void setType(String type) { this.type = type; }
    public String toString() { return "Engine[" + type + "]"; }
}

public class Vehicle {
    private String color;
    private Engine engine;
    public void setColor(String color) { this.color = color; }
    public void setEngine(Engine engine) { this.engine = engine; }
    public String toString() { return "Vehicle[color=" + color + ", engine=" + engine + "]"; }
}
```

---

### Task B1 — `id` vs `name`
```xml
<bean id="vehicle1" name="car, automobile" class="com.example.Vehicle" />
```
- [ ] Retrieve the bean using `context.getBean("vehicle1")`, then `context.getBean("car")`, then `context.getBean("automobile")`.

> [!question]
> Are all three references pointing to the **same** bean instance? What's the practical difference between `id` and `name` (hint: which one allows special characters/multiple aliases)?

---

### Task B2 — `class`
- [ ] Change the `class` attribute to a non-existent class name (typo it).
- [ ] Re-run.

> [!question]
> At what point does Spring fail — when the context loads, or only when you call `getBean()`? What does this tell you about eager vs lazy class resolution?

---

### Task B3 — `scope`
```xml
<bean id="vehicle2" class="com.example.Vehicle" scope="prototype" />
```
- [ ] Retrieve `vehicle2` twice and compare identity hash codes.
- [ ] Change `scope` to `"singleton"` (or remove it, since that's the default) and repeat.

> [!question]
> Document the identity comparison for both scopes. Which scope is the default when the attribute is omitted?

---

### Task B4 — `abstract` and `parent`
```xml
<bean id="baseVehicle" abstract="true">
    <property name="color" value="white" />
</bean>

<bean id="redCar" class="com.example.Vehicle" parent="baseVehicle">
    <property name="color" value="red" />
</bean>
```
- [ ] Try calling `context.getBean("baseVehicle")` directly.
- [ ] Retrieve `redCar` and print it.

> [!question]
> 1. What exception do you get when trying to instantiate `baseVehicle` directly?
> 2. Does `redCar` inherit the `color` property from the parent, or does its own `<property>` override it? What is `abstract="true"` used for in real-world configs (hint: think templates)?

---

### Task B5 — `lazy-init`
```xml
<bean id="engine1" class="com.example.Engine" lazy-init="true">
</bean>
```
- [ ] Add a `println` inside `Engine`'s constructor.
- [ ] Start the context **without** calling `getBean("engine1")` and observe whether the constructor runs.
- [ ] Then call `context.getBean("engine1")` and observe again.

> [!question]
> When exactly is the bean instantiated with `lazy-init="true"` versus the default (`false`)? Why might you want to lazily initialize a bean in a real application?

---

### Task B6 — `autowire` (byName / byType)
```xml
<bean id="engine" class="com.example.Engine" />

<bean id="vehicle3" class="com.example.Vehicle" autowire="byType" />
```
- [ ] Run and print `vehicle3` — confirm the `engine` property got wired automatically with no `<property>` tag.
- [ ] Switch `autowire` to `"byName"` and make sure the `Engine` bean's `id` exactly matches the property name (`engine`). Test what happens if the `id` does **not** match the property name.

> [!question]
> What's the difference in matching logic between `byType` and `byName`? What happens with `byType` if there are **two** `Engine` beans defined?

---

### Task B7 — `autowire-candidate`
- [ ] With two `Engine` beans defined, set `autowire-candidate="false"` on one of them.
- [ ] Use `autowire="byType"` on `vehicle3` again.

> [!question]
> Does the excluded bean ever get selected for autowiring? What real-world scenario would justify marking a bean as `autowire-candidate="false"`?

---

### Task B8 — `primary`
- [ ] Keep both `Engine` beans, remove `autowire-candidate="false"`.
- [ ] Mark **one** of them `primary="true"`.
- [ ] Autowire `vehicle3` `byType` again.

> [!question]
> Which `Engine` bean wins now? How does `primary` resolve the ambiguity that caused an error/warning in Task B6 with two candidates?

---

### Task B9 — `depends-on`
```xml
<bean id="engine" class="com.example.Engine" depends-on="logger" />
<bean id="logger" class="com.example.SimpleLogger" />
```
- [ ] Create a trivial `SimpleLogger` class that prints a message in its constructor.
- [ ] Run and observe the order of constructor prints for `engine` vs `logger`, even though nothing in the Java code references `SimpleLogger`.

> [!question]
> Why would you need `depends-on` when there is no actual property/constructor reference between two beans? Give a real-world example (e.g. a bean that requires a JDBC driver to be registered first).

---

### Task B10 — `init-method` / `destroy-method` (Bean Definition level)
- [ ] Reuse what you built in Part A: add `initMethod`/`destroyMethod`-style methods to `Vehicle` and wire them via `init-method` / `destroy-method` on `vehicle1`.

> [!question]
> How is this identical to what you tested in Part A Task A2? Confirm both parts of the lab produce consistent behavior.

---

### Task B11 — `factory-method` (static factory)
```java
public class VehicleFactory {
    public static Vehicle createSportsCar() {
        Vehicle v = new Vehicle();
        v.setColor("yellow");
        return v;
    }
}
```
```xml
<bean id="sportsCar" class="com.example.VehicleFactory" factory-method="createSportsCar" />
```
- [ ] Retrieve `sportsCar` and print it.

> [!question]
> Does Spring call `VehicleFactory`'s constructor to create the bean, or does it just invoke the static method directly? Check by adding a `println` in `VehicleFactory`'s (implicit) constructor — does it ever print?

---

### Task B12 — `factory-bean` + `factory-method` (instance factory)
```java
public class VehicleInstanceFactory {
    public Vehicle createSedan() {
        Vehicle v = new Vehicle();
        v.setColor("black");
        return v;
    }
}
```
```xml
<bean id="vehicleInstanceFactory" class="com.example.VehicleInstanceFactory" />

<bean id="sedan" factory-bean="vehicleInstanceFactory" factory-method="createSedan" />
```
- [ ] Retrieve `sedan` and print it.

> [!question]
> Why does this `<bean>` definition for `sedan` have **no `class` attribute**? Compare this pattern to Task B11 — when would you use an instance factory method instead of a static one?
