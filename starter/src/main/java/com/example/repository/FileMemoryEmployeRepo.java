package com.example.repository;

import com.example.entity.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Profile("prod")
@Repository
public class FileMemoryEmployeRepo implements EmployeeRepository {

    private static final String FILE_PATH = "employees.txt";

    @Override
    public void save(Employee employee) {
        List<Employee> all = findAll();
        // Auto-assign ID if it's not set
        if (employee.getId() == 0) {
            employee.setId(all.size() + 1);
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            out.println(employee.getId() + "," + employee.getName() + "," + employee.getDepartment() + "," + employee.getSalary());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee findById(Integer id) {
        return findAll().stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return employees; // Return empty list if file doesn't exist yet
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String department = parts[2];
                    float salary = Float.parseFloat(parts[3]);

                    Employee emp = new Employee(name, department, salary);
                    emp.setId(id); //

                    employees.add(emp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }
}