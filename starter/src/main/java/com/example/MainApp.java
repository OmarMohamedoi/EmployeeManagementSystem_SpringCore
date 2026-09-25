package com.example;

import com.example.Service.EmployeeServiceImpl;
import com.example.audit.AuditLogger;
import com.example.config.AppConfig;
import com.example.entity.Employee;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {

        // 1. Initialize Application Context & Set Active Profile to 'dev' or 'prod' as you like.
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("prod");
        context.register(AppConfig.class);
        context.refresh();

        System.out.println("\n========================================");
        System.out.println("🚀 SPRING CORE SYSTEM INITIALIZED");
        System.out.println("========================================");

        // Retrieve EmployeeService bean
        EmployeeServiceImpl employeeService = context.getBean("employeeServiceImpl", EmployeeServiceImpl.class);

        // 2. Test Adding a Valid Employee (Triggers Validation, AuditLogger, & Notifiers)
        System.out.println("\n--- Test 1: Adding Valid Employee ---");
        Employee validEmp = new Employee("Omar Mohamed", "Software Engineering", 60000.0f);
        employeeService.addEmployee(validEmp);

        // 3. Test Adding an Invalid Employee (Blank Name -> Throws Exception)
        System.out.println("\n--- Test 2: Adding Invalid Employee (Blank Name) ---");
        Employee invalidEmp = new Employee("", "HR", 40000.0f);
        employeeService.addEmployee(invalidEmp);

        // 4. Test Giving a Valid Raise (Within Max Limit)
        System.out.println("\n--- Test 3: Giving Valid Raise (20%) ---");
        employeeService.giveRaise(1, 20.0);

        // 5. Test Giving an Invalid Raise (Exceeds Max Limit from application.properties)
        System.out.println("\n--- Test 4: Giving Invalid Raise (60% - Exceeds 50% Max) ---");
        employeeService.giveRaise(1, 60.0);

        // 6. Demonstrate Prototype Scope for AuditLogger
        System.out.println("\n--- Test 5: Proving Prototype Scope for AuditLogger ---");
        AuditLogger logger1 = context.getBean(AuditLogger.class);
        AuditLogger logger2 = context.getBean(AuditLogger.class);

        System.out.println("AuditLogger Instance 1 Hash: " + System.identityHashCode(logger1));
        System.out.println("AuditLogger Instance 2 Hash: " + System.identityHashCode(logger2));
        System.out.println("(Notice the hashes are different, proving a new instance is created every time because of prototype scope!)");

        logger1.log();
        logger2.log();

        // 7. List All Current Employees
        System.out.println("\n--- Test 6: Listing All Employees ---");
        var allEmployees = employeeService.getAllEmployees();
        for (Employee emp : allEmployees) {
            System.out.println(emp);
        }

        // 8. Close Context to Trigger @PreDestroy Callbacks
        System.out.println("\n========================================");
        System.out.println("🛑 CLOSING APPLICATION CONTEXT");
        System.out.println("========================================");
        context.close();
    }
}