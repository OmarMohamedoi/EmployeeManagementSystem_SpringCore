package com.example.repository;

import com.example.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Profile("dev")
@Repository
public class EmployeeRepoImpl implements EmployeeRepository{

    private final InMemoryEmployeRepo e;

    @Autowired
    public EmployeeRepoImpl(@Qualifier("inMemoryEmployeRepo") InMemoryEmployeRepo e) {
        this.e = e;
    }

    @Override
    public void save(Employee employee) {
        e.employees.add(employee);
    }

    @Override
    public Employee findById(Integer id) {
        return e.employees.stream()
                .filter(emp->emp.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Employee> findAll() {
       return e.employees;
    }
}
