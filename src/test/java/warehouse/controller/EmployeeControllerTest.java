package warehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import warehouse.config.WebApplicationContextConfig;
import warehouse.domain.Employee;
import warehouse.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebApplicationContextConfig.class)
@WebAppConfiguration
public class EmployeeControllerTest {
    private final List<Employee> employees = new ArrayList<>();
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        addEmployees();
    }

    private static String asJsonString(final Object o) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employeeId").value(employees.get(0).getEmployeeId()))
                .andExpect(jsonPath("$[0].name").value(employees.get(0).getName()))
                .andExpect(jsonPath("$[0].surname").value(employees.get(0).getSurname()))
                .andExpect(jsonPath("$[1].employeeId").value(employees.get(1).getEmployeeId()))
                .andExpect(jsonPath("$[1].name").value(employees.get(1).getName()))
                .andExpect(jsonPath("$[1].surname").value(employees.get(1).getSurname()));
    }

    @Test
    public void shouldGetEmployeeById() throws Exception {
        mockMvc.perform(get("/employees/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.employeeId").value(employees.get(0).getEmployeeId()))
                .andExpect(jsonPath("$.name").value(employees.get(0).getName()))
                .andExpect(jsonPath("$.surname").value(employees.get(0).getSurname()));
    }

    @Test
    public void shouldReturnNotFoundStatusWhenEmployeeByIdNotFound() throws Exception {
        mockMvc.perform(get("/employees/employee/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetEmployeesByName() throws Exception {
        mockMvc.perform(get("/employees/employee/name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeId").value(employees.get(0).getEmployeeId()))
                .andExpect(jsonPath("$[0].name").value(employees.get(0).getName()))
                .andExpect(jsonPath("$[0].surname").value(employees.get(0).getSurname()));
    }

    @Test
    public void shouldReturnNotFoundStatusWhenEmployeesByNameNotFound() throws Exception {
        mockMvc.perform(get("/employees/employee/name/Foo"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetEmployeesBySurname() throws Exception {
        mockMvc.perform(get("/employees/employee/surname/Black"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeId").value(employees.get(0).getEmployeeId()))
                .andExpect(jsonPath("$[0].name").value(employees.get(0).getName()))
                .andExpect(jsonPath("$[0].surname").value(employees.get(0).getSurname()));
    }

    @Test
    public void shouldReturnNotFoundStatusWhenEmployeesBySurnameNotFound() throws Exception {
        mockMvc.perform(get("/employees/employee/surname/Foo"))
                .andExpect(status().isNotFound());
    }

    private void addEmployees() {
        employeeService.deleteAllEmployees();

        Employee employee1 = new Employee();
        employee1.setEmployeeId("1");
        employee1.setName("John");
        employee1.setSurname("Black");
        employees.add(employee1);
        employeeService.create(employee1);

        Employee employee2 = new Employee();
        employee2.setEmployeeId("2");
        employee2.setName("Sandra");
        employee2.setSurname("Cambridge");
        employees.add(employee2);
        employeeService.create(employee2);
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId("15");
        employee.setName("Roger");
        employee.setSurname("Crank");
        employees.add(employee);

        mockMvc.perform(post("/employees")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestWhenPassingEmptyEmployeeToCreate() throws Exception {
        mockMvc.perform(post("/employees"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnConflictStatusWhenTryingCreateEmployeeWithIdThatExist() throws Exception {
        mockMvc.perform(post("/employees")
                .content(asJsonString(employees.get(0)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
