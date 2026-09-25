package com.example.Service;

import com.example.entity.Employee;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeService {
    void addEmployee(Employee e);
    Employee getEmployeeById(int id);
    List<Employee> getAllEmployees();
    void giveRaise(int id, double percentage);
}
