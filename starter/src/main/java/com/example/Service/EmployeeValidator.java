package com.example.Service;

import com.example.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {
    public void validate(Employee e) throws Exception {
        if(e.getName().isBlank()){
            throw new Exception("Cant have empty name");
        } else if (e.getSalary()<0) {
            throw new Exception("Can't have negative salary");
        }
        else{
            System.out.println("Employee safe to add.");
        }
    }
}
