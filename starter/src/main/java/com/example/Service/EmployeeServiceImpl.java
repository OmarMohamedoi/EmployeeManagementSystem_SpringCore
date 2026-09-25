package com.example.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import com.example.audit.AuditLogger;
import com.example.entity.Employee;
import com.example.notify.NotifyManager;
import com.example.repository.EmployeeRepoImpl;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final NotifyManager notifyManager;
    private final EmployeeValidator employeeValidator;
    private final ObjectProvider<AuditLogger> loggerManager;

    @Value("${raise.max-percentage:50.0}")
    private double maxRaisePercentage;

    @Value("${company.name:DefaultCompany}")
    private String companyName;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepo, NotifyManager notifyManager,EmployeeValidator employeeValidator, ObjectProvider<AuditLogger> loggerManager){
        this.employeeRepo=employeeRepo;
        this.notifyManager=notifyManager;
        this.employeeValidator=employeeValidator;
        this.loggerManager=loggerManager;
    }

    @PostConstruct
    public void init(){
        System.out.println("[Life cycle] EmployeeServiceImpl initialized for company: "+ companyName);
    }

    @PreDestroy
    public void cleanup(){
        System.out.println("[Life cycle] EmployeeServiceImpl is being destroyed, closing...");
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
            System.out.println("[HANDLED EXCEPTION]: "+ exception.getMessage());
        }
    }

    @Override
    public Employee getEmployeeById(int id) {
        return employeeRepo.findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public void giveRaise(int id, double percentage) {
        if (percentage > maxRaisePercentage) {
            System.out.println("[Raise Denied]: Requested raise " + percentage + "% exceeds maximum allowed limit of " + maxRaisePercentage + "%");
            return;
        }
        Employee e = employeeRepo.findById(id);
        if (e!=null) {
            double newSalary = e.getSalary() + ((e.getSalary() * percentage) / 100);
            e.setSalary((float) newSalary);
            notifyManager.sendNotifiers("EMPLOYEE RECEIVED A " + percentage + "% RAISE!");
            System.out.println("Raise applied successfully. New salary: " + e.getSalary());
           // employeeRepo.save(e);

        }
        else{
            System.out.println("Invalid Employee id: " + id);
        }

    }
}
