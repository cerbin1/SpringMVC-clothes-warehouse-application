package warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import warehouse.domain.Employee;
import warehouse.domain.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeServiceImplementation implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        return employeeRepository.getEmployeeById(employeeId);
    }

    @Override
    public List<Employee> getEmployeesByName(String name) {
        return employeeRepository.getEmployeesByName(name);
    }

    @Override
    public List<Employee> getEmployeesBySurname(String surname) {
        return employeeRepository.getEmployeesBySurname(surname);
    }

    @Override
    public void deleteAllEmployees() {
        employeeRepository.deleteAllEmployees();
    }

    @Override
    public void create(Employee employee) {
        employeeRepository.create(employee);
    }

    @Override
    public void updateEmployee(String employeeId, Employee updatedEmployee) {
        employeeRepository.update(employeeId, updatedEmployee);
    }
}
