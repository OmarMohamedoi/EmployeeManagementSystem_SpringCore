package com.example.Service;

import com.example.entity.Employee;

import java.util.ArrayList;

public interface EmployeeService {
    void addEmployee(Employee e);
    Employee getEmployeeById(int id);
    ArrayList<Employee> getAllEmployees();
    void giveRaise(int id, double percentage);
}
