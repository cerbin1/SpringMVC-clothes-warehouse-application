package warehouse.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import warehouse.domain.Employee;
import warehouse.exception.EmployeeAlreadyExistException;
import warehouse.exception.EmployeeNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static warehouse.DatabaseNames.TABLE_NAME_EMPLOYEES;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public InMemoryEmployeeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM " + TABLE_NAME_EMPLOYEES;
        Map<String, Object> params = new HashMap<>();
        return jdbcTemplate.query(sql, params, new EmployeeMapper());
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        String sql = "SELECT * FROM " + TABLE_NAME_EMPLOYEES + " WHERE ID=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", employeeId);
        try {
            return jdbcTemplate.queryForObject(sql, params, new EmployeeMapper());
        } catch (DataAccessException exception) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    @Override
    public List<Employee> getEmployeesByName(String name) {
        String sql = "SELECT * FROM " + TABLE_NAME_EMPLOYEES + " WHERE NAME=:name";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return jdbcTemplate.query(sql, params, new EmployeeMapper());
    }

    @Override
    public List<Employee> getEmployeesBySurname(String surname) {
        String sql = "SELECT * FROM " + TABLE_NAME_EMPLOYEES + " WHERE SURNAME=:surname";
        Map<String, Object> params = new HashMap<>();
        params.put("surname", surname);
        return jdbcTemplate.query(sql, params, new EmployeeMapper());
    }

    @Override
    public void deleteAllEmployees() {
        String sql = "TRUNCATE TABLE " + TABLE_NAME_EMPLOYEES;
        jdbcTemplate.update(sql, new HashMap<>());
    }

    @Override
    public void create(Employee employee) {
        if (employeeWithIdExist(employee.getEmployeeId())) {
            throw new EmployeeAlreadyExistException();
        }
        String sql = "INSERT INTO " + TABLE_NAME_EMPLOYEES + " VALUES (" +
                ":id, :name, :surname)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", employee.getEmployeeId());
        params.put("name", employee.getName());
        params.put("surname", employee.getSurname());
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void update(String employeeId, Employee updatedEmployee) {
        if (employeeWithIdExist(employeeId)) {
            String sql = "UPDATE " + TABLE_NAME_EMPLOYEES + " SET " +
                    "NAME=:name, " +
                    "SURNAME=:surname " +
                    "WHERE ID=:id";
            Map<String, Object> params = new HashMap<>();
            params.put("name", updatedEmployee.getName());
            params.put("surname", updatedEmployee.getSurname());
            params.put("id", employeeId);
            jdbcTemplate.update(sql, params);
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    private boolean employeeWithIdExist(String employeeId) {
        List<Employee> employees = getAllEmployees();
        return employees
                .stream()
                .filter(employee -> employee
                        .getEmployeeId()
                        .equals(employeeId))
                .findAny()
                .orElse(null) != null;
    }

    private final class EmployeeMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee employee = new Employee();
            employee.setEmployeeId(resultSet.getString("ID"));
            employee.setName(resultSet.getString("NAME"));
            employee.setSurname(resultSet.getString("SURNAME"));
            return employee;
        }
    }
}
