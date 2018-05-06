package warehouse.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import warehouse.domain.Employee;

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
        return jdbcTemplate.queryForObject(sql, params, new EmployeeMapper());
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
    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO " + TABLE_NAME_EMPLOYEES + " VALUES (" +
                ":id, :name, :surname)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", employee.getEmployeeId());
        params.put("name", employee.getName());
        params.put("surname", employee.getSurname());
        jdbcTemplate.update(sql, params);
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