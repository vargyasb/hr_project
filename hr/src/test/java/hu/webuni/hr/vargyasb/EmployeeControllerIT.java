package hu.webuni.hr.vargyasb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.vargyasb.dto.CompanyDto;
import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class EmployeeControllerIT {

	private static final String BASE_URI = "api/employees";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@BeforeEach
	public void init() {
		employeeRepository.deleteAll();
	}
	
	@Test
	void testThatCreatedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getEmployees();
		
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		createEmployee(employee);
		
		List<EmployeeDto> employeesAfter = getEmployees();
		
		assertThat(employeesAfter.subList(0, employeesBefore.size()))
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactlyElementsOf(employeesBefore);
		
		assertThat(employeesAfter.get(employeesAfter.size()-1))
		.usingRecursiveComparison().ignoringFields("id")
		.isEqualTo(employee);
	}
	
	@Test
	void testThatCreatedEmployeeIsListed_WithWrongInput() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		createEmployee(employee);
		List<EmployeeDto> employeesBefore = getEmployees();
		
		employee = new EmployeeDto("", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
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
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		long employeeId = employee.getId();
		
		EmployeeDto newEmployee = new EmployeeDto("Sima Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		
		EmployeeDto modifiedEmployee = modifyEmployee(employeeId, newEmployee);
		
		assertThat(modifiedEmployee)
		.usingRecursiveComparison().ignoringFields("id")
		.isEqualTo(newEmployee);
	}

	@Test
	void testThatEmployeeIsModified_WithWrongInput() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		long employeeId = employee.getId();
		
		EmployeeDto newEmployee = new EmployeeDto("", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		
		modifyEmployeeWithBadRequestStatus(employeeId, newEmployee);
		
		assertThat(employee)
		.usingRecursiveComparison().ignoringFields("id")
		.isNotEqualTo(newEmployee);
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleId() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		long employeeId = employee.getId();

		EmployeeDto example = new EmployeeDto();
		example.setId(employeeId);

		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.isEmpty()).isFalse();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleName() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		
		EmployeeDto example = new EmployeeDto();
		example.setName("Teszt");
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.isEmpty()).isFalse();
	}
	
	@Test
	void testThatEmployeeIsFoundByExamplePosition() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		
		EmployeeDto example = new EmployeeDto();
		example.setPosition("Developer");
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.isEmpty()).isFalse();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleSalary() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		
		EmployeeDto example = new EmployeeDto();
		example.setSalary(10200);
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.isEmpty()).isFalse();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleSalaryWithNoResults() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		
		EmployeeDto example = new EmployeeDto();
		example.setSalary(6000);
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.isEmpty()).isTrue();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleStartOfEmployment() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		employee = createEmployee(employee);
		
		EmployeeDto example = new EmployeeDto();
		example.setStartOfEmployment(LocalDateTime.of(2010, 10, 13, 0, 0));
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.isEmpty()).isFalse();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleCompany() throws Exception {
		EmployeeDto employee = new EmployeeDto("Teszt Elek", 9999, "Developer", LocalDateTime.of(2010, 10, 13, 10, 25));
		CompanyDto company = new CompanyDto(null, "Udemy Corporated", null, null, null);
		company = createCompany(company);
		employee.setCompany(company);
		employee = createEmployee(employee);
		
		EmployeeDto example = new EmployeeDto();
		example.setCompany(new CompanyDto(null, "Udemy", null, null, null));
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.isEmpty()).isFalse();
	}
	
	private CompanyDto createCompany(CompanyDto company) {
		return webTestClient
				.post()
				.uri("/api/companies")
				.bodyValue(company)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(CompanyDto.class)
				.returnResult()
				.getResponseBody();
	}

	private List<EmployeeDto> findEmployeeByExample(EmployeeDto example) {
		return webTestClient
		.post()
		.uri(BASE_URI + "?example")
		.bodyValue(example)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(EmployeeDto.class)
		.returnResult()
		.getResponseBody();
	}
	
	private void modifyEmployeeWithBadRequestStatus(long employeeId, EmployeeDto employee) {
		webTestClient
			.put()
			.uri(BASE_URI + "/" + employeeId)
			.bodyValue(employee)
			.exchange()
			.expectStatus()
			.isBadRequest();
	}

	private EmployeeDto modifyEmployee(long employeeId, EmployeeDto employee) {
		return webTestClient
			.put()
			.uri(BASE_URI + "/" + employeeId)
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

	private EmployeeDto createEmployee(EmployeeDto employee) {
		return webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(employee)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(EmployeeDto.class)
			.returnResult()
			.getResponseBody();
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
