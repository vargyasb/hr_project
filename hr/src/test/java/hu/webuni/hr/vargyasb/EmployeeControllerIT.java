package hu.webuni.hr.vargyasb;

import static org.assertj.core.api.Assertions.assertThat;

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
import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.Position;
import hu.webuni.hr.vargyasb.repository.CompanyRepository;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.PositionDetailsByCompanyRepository;
import hu.webuni.hr.vargyasb.repository.PositionRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class EmployeeControllerIT {

	private static final String BASE_URI = "api/employees";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	
	@BeforeEach
	public void init() {
		employeeRepository.deleteAll();
		positionDetailsByCompanyRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
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
		long employeeId = createTestDataAndReturnEmployeeId();

		Employee example = new Employee();
		example.setId(employeeId);

		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getId() == employeeId).isTrue();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleName() throws Exception {
		long employeeId = createTestDataAndReturnEmployeeId();

		Employee example = new Employee();
		example.setName("Teszt ");
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getId() == employeeId).isTrue();
	}
	
	@Test
	void testThatEmployeeIsFoundByExamplePosition() throws Exception {
		long employeeId = createTestDataAndReturnEmployeeId();

		EmployeeDto example = new EmployeeDto();
		example.setPosition("Developer");
		
		List<EmployeeDto> results = findEmployeeByExampleDto(example);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getId() == employeeId).isTrue();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleSalary() throws Exception {
		long employeeId = createTestDataAndReturnEmployeeId();

		Employee example = new Employee();
		example.setSalary(10200);
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getId() == employeeId).isTrue();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleSalaryWithNoResults() throws Exception {
		createTestDataAndReturnEmployeeId();

		Employee example = new Employee();
		example.setSalary(6000);
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results).isEmpty();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleStartOfEmployment() throws Exception {
		long employeeId = createTestDataAndReturnEmployeeId();

		Employee example = new Employee();
		example.setStartOfEmployment(LocalDateTime.of(2010, 10, 13, 0, 0));
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getId() == employeeId).isTrue();
	}
	
	@Test
	void testThatEmployeeIsFoundByExampleCompany() throws Exception {
		long employeeId = createTestDataAndReturnEmployeeId();

		Employee example = new Employee();
		example.setCompany(new Company(null, "Udemy", null));
		
		List<EmployeeDto> results = findEmployeeByExample(example);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getId() == employeeId).isTrue();
	}
	
	private long createTestDataAndReturnEmployeeId() {
		Employee employee = new Employee("Teszt Elek", 9999, LocalDateTime.of(2010, 10, 13, 10, 25));
		Company company = new Company("1234", "Udemy", "Something str 99");
		Position position = new Position("Developer", "BSC", 10000);
		position = positionRepository.save(position);
		company = companyRepository.save(company);
		employee.setCompany(company);
		employee.setPosition(position);
		employee = employeeRepository.save(employee);
		long employeeId = employee.getId();
		
		employee = new Employee("Teszt2 Elek", 1234, LocalDateTime.of(2015, 10, 13, 10, 25));
		company = new Company("4444", "Webuni", "Something str 12");
		position = new Position("Tester", "BSC", 1000);
		position = positionRepository.save(position);
		company = companyRepository.save(company);
		employee.setCompany(company);
		employee.setPosition(position);
		employee = employeeRepository.save(employee);
		
		employee = new Employee("3Teszt Elek", 4312, LocalDateTime.of(2020, 10, 13, 10, 25));
		company = new Company("1111", "Coursera", "Something str 1");
		position = new Position("Scrum Master", "BSC", 5230);
		position = positionRepository.save(position);
		company = companyRepository.save(company);
		employee.setCompany(company);
		employee.setPosition(position);
		employee = employeeRepository.save(employee);
		
		return employeeId;
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

	private List<EmployeeDto> findEmployeeByExample(Employee example) {
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
	
	private List<EmployeeDto> findEmployeeByExampleDto(EmployeeDto example) {
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
