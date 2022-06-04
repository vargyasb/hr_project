package hu.webuni.hr.vargyasb;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

	private static final String BASE_URI = "api/employees";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Test
	void testThatCreatedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getEmployees();
		
		EmployeeDto employee = new EmployeeDto(5L, "Teszt Elek", "Főhős", 9999, LocalDateTime.of(2010, 10, 13, 10, 25));
		createEmployee(employee);
		
		List<EmployeeDto> employeesAfter = getEmployees();
		
		assertThat(employeesAfter.subList(0, employeesBefore.size()))
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactlyElementsOf(employeesBefore);
		
		assertThat(employeesAfter.get(employeesAfter.size()-1))
		.usingRecursiveComparison()
		.isEqualTo(employee);
	}
	
	@Test
	void testThatCreatedEmployeeIsListed_WithWrongInput() throws Exception {
		List<EmployeeDto> employeesBefore = getEmployees();
		
		EmployeeDto employee = new EmployeeDto(5L, "Teszt Elek", null, 9999, LocalDateTime.of(2010, 10, 13, 10, 25));
		createEmployeeWithBadRequestStatus(employee);
		
		List<EmployeeDto> employeesAfter = getEmployees();
		
		assertThat(employeesAfter.subList(0, employeesBefore.size()))
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactlyElementsOf(employeesBefore);
		
		assertThat(employeesAfter.get(employeesAfter.size()-1))
		.usingRecursiveComparison()
		.isNotEqualTo(employee);
	}
	
	@Test
	void testThatEmployeeIsModified() throws Exception {
		EmployeeDto employee = new EmployeeDto(5L, "Teszt Elek", "Főhős", 9999, LocalDateTime.of(2010, 10, 13, 10, 25));
		
		EmployeeDto modifiedEmployee = modifyEmployee(employee);
		
		assertThat(modifiedEmployee)
		.usingRecursiveComparison()
		.isEqualTo(employee);
	}

	@Test
	void testThatEmployeeIsModified_WithWrongInput() throws Exception {
		EmployeeDto employee = new EmployeeDto(5L, "", "Főhős", 9999, LocalDateTime.of(2010, 10, 13, 10, 25));
		
		EmployeeDto employeeToModify = getEmployeeById(employee.getId());
		
		modifyEmployeeWithBadRequestStatus(employee);
		
		assertThat(employeeToModify)
		.usingRecursiveComparison()
		.isNotEqualTo(employee);
	}
	
	private EmployeeDto modifyEmployeeWithBadRequestStatus(EmployeeDto employee) {
		return webTestClient
			.put()
			.uri(BASE_URI + "/" + employee.getId())
			.bodyValue(employee)
			.exchange()
			.expectStatus()
			.isBadRequest()
			.expectBody(EmployeeDto.class)
			.returnResult()
			.getResponseBody();
	}

	private EmployeeDto modifyEmployee(EmployeeDto employee) {
		return webTestClient
			.put()
			.uri(BASE_URI + "/" + employee.getId())
			.bodyValue(employee)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(EmployeeDto.class)
			.returnResult()
			.getResponseBody();
	}

	private EmployeeDto getEmployeeById(Long id) {
		return webTestClient
			.get()
			.uri(BASE_URI + "/" + id)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(EmployeeDto.class)
			.returnResult()
			.getResponseBody();
	}

	private void createEmployee(EmployeeDto employee) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(employee)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private void createEmployeeWithBadRequestStatus(EmployeeDto employee) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(employee)
			.exchange()
			.expectStatus()
			.isBadRequest();
	}

	private List<EmployeeDto> getEmployees() {
		List<EmployeeDto> employees = webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
		
		
		Collections.sort(employees, (e1, e2) -> Long.compare(e1.getId(), e2.getId()));
		return employees;
	}
	
}
