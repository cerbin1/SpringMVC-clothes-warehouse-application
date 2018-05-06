package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.domain.Employee;
import warehouse.exception.EmployeeAlreadyExistException;
import warehouse.service.EmployeeService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), OK);
    }

    @GetMapping("employees/employee/{employeeId:[0-9]+}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable String employeeId) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            return new ResponseEntity<>(employee, OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @GetMapping("employees/employee/name/{employeeName:[A-Z][a-z]+}")
    public ResponseEntity<List<Employee>> getEmployeesByName(
            @PathVariable String employeeName) {
        List<Employee> employees = employeeService.getEmployeesByName(employeeName);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(employees, OK);
    }

    @GetMapping("employees/employee/surname/{employeeSurname:[A-Z][a-z]+(?:-|\\s)?[a-z]+}")
    public ResponseEntity<List<Employee>> getEmployeesBySurname(
            @PathVariable String employeeSurname) {
        List<Employee> employees = employeeService.getEmployeesBySurname(employeeSurname);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(employees, OK);
    }

    @PostMapping("/employees")
    public ResponseEntity createEmployee(
            @RequestBody Employee employee) {
        if (employee == null) {
            return new ResponseEntity(BAD_REQUEST);
        }
        try {
            employeeService.create(employee);
        } catch (EmployeeAlreadyExistException exception) {
            return new ResponseEntity(CONFLICT);
        }
        return new ResponseEntity(CREATED);
    }
}
