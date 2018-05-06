package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import warehouse.domain.Employee;
import warehouse.service.EmployeeService;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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


    @GetMapping("employees/employee/{name:[A-Z][a-z]+}")
    public ResponseEntity<List<Employee>> getEmployeesByName(
            @PathVariable String name) {
        List<Employee> employees = employeeService.getEmployeesByName(name);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(employees, OK);
    }
}
