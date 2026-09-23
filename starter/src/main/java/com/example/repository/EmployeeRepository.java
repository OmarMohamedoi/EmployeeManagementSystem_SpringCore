package com.example.repository;

import com.example.entity.Employee;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeRepository {

    public void save(Employee employee);
    public Employee findById(Integer id);
    public List<Employee> findAll();

}
