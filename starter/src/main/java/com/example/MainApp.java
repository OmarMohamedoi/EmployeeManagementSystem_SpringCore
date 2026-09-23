package com.example;

import com.example.Service.EmployeeServiceImpl;
import com.example.config.AppConfig;
import com.example.entity.Employee;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext();

        context.getEnvironment().setActiveProfiles("dev");

        context.register(AppConfig.class);
        context.refresh();

        Employee e = new Employee("Omar", "HR", 250000);
        EmployeeServiceImpl impl =context.getBean("employeeServiceImpl", EmployeeServiceImpl.class);
//        System.out.println(e.getId());;
//        impl.addEmployee(e);
//        var resultset = impl.getAllEmployees();
//        for (Employee employee : resultset) {
//            System.out.println(employee);
//        }
//        impl.giveRaise(0, 30);
//        var resultset2 = impl.getAllEmployees();
//
//        for (Employee employee : resultset2) {
//            System.out.println(employee);
//        }
    Employee eBlank= new Employee("Omar", "HR", 100);
    impl.addEmployee(eBlank);

    var result = impl.getAllEmployees();
        for (Employee employee : result) {
            System.out.println(employee);
        }


    }
}
