package com.example.repository;

import com.example.entity.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Scope("singleton")
@Profile("dev")
@Repository
public class InMemoryEmployeRepo {

    ArrayList<Employee> employees = new ArrayList<>();

    public InMemoryEmployeRepo() {

    }
}
