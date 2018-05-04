package warehouse.domain.repository;

import warehouse.domain.Employee;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> getAllEmployees();

    Employee getEmployeeById(String employeeId);

    List<Employee> getEmployeesByName(String name);

    List<Employee> getEmployeesBySurname(String surname);
}
