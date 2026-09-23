package com.example.Service;

import com.example.audit.AuditLogger;
import com.example.entity.Employee;
import com.example.notify.NotifyManager;
import com.example.repository.EmployeeRepoImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepoImpl employeeRepo;
    private final NotifyManager notifyManager;
    private final EmployeeValidator employeeValidator;
    private final ObjectProvider<AuditLogger> loggerManager;

    @Autowired
    public EmployeeServiceImpl(@Qualifier("employeeRepoImpl") EmployeeRepoImpl employeeRepo, NotifyManager notifyManager,EmployeeValidator employeeValidator, ObjectProvider<AuditLogger> loggerManager){
        this.employeeRepo=employeeRepo;
        this.notifyManager=notifyManager;
        this.employeeValidator=employeeValidator;
        this.loggerManager=loggerManager;
    }
    @Override
    public void addEmployee(Employee e) {

        try{

            employeeValidator.validate(e);
            employeeRepo.save(e);
            var log = loggerManager.getObject();
            log.log();
            notifyManager.sendNotifiers("EMPLOYEE SUCCESSFULLY ADDED TO SYSTEM!");

        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public Employee getEmployeeById(int id) {
        return employeeRepo.findById(id);
    }

    @Override
    public ArrayList<Employee> getAllEmployees() {
        return (ArrayList<Employee>)employeeRepo.findAll();
    }

    @Override
    public void giveRaise(int id, double percentage) {
        Employee e = employeeRepo.findById(id);
        if (e!=null) {
            double newSalary = e.getSalary() + ((e.getSalary() * percentage) / 100);
            e.setSalary((float) newSalary);
           // employeeRepo.save(e);

        }
        else{
            System.out.println("Invalid Employee");
        }

    }
}
