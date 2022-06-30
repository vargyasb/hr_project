package hu.webuni.hr.vargyasb.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.min;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.CompanyType;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.Position;
import hu.webuni.hr.vargyasb.repository.CompanyRepository;
import hu.webuni.hr.vargyasb.repository.CompanyTypeRepository;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.PositionRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class CompanyServiceIT {

	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyTypeRepository companyTypeRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	EmployeeService employeeService;
	
	@BeforeEach
	public void init() {
		//companyRepository.deleteAll();
		//employeeRepository.deleteAll();
		//companyTypeRepository.deleteAll();
		//positionRepository.deleteAll();
	}
	
	@Test
	void testCreateCompanyType() throws Exception {
		String companyType  = "ZRT";
		long companyTypeId = createCompanyType(companyType);
		
		Optional<CompanyType> savedCompanyTypeOptional = companyTypeRepository.findById(companyTypeId);
		assertThat(savedCompanyTypeOptional).isNotEmpty();
		CompanyType savedCompanyType = savedCompanyTypeOptional.get();
		assertThat(savedCompanyType.getType()).isEqualTo(companyType);
	}
	
	@Test
	void testCreateEmployee() throws Exception {
		String testEmployeeName = "Tim Buchalka";
		int salary = 1209;
		
		String testCompanyRegNumber = "111";
		String testCompanyName = "Udemy";
		String testCompanyAddress = "Frist str 2.";
		String companyType = "ZRT";
		long companyTypeId = createCompanyType(companyType);
		
		long companyId = createCompany(testCompanyRegNumber, testCompanyName, testCompanyAddress, companyTypeId);
		
		String positionName = "Java Developer";
		String education = "főiskola";
		int minSalary = 1200;
		long positionId = createPosition(positionName, education, minSalary);
		
		long employeeId = createEmployee(testEmployeeName, salary, companyId, positionId);
		
		Optional<Employee> savedEmployeeOptional = employeeRepository.findById(employeeId);
		assertThat(savedEmployeeOptional).isNotEmpty();
		Employee savedEmployee = savedEmployeeOptional.get();
		assertThat(savedEmployee.getName()).isEqualTo(testEmployeeName);
		assertThat(savedEmployee.getSalary()).isEqualTo(salary);
		assertThat(savedEmployee.getCompany().getId()).isEqualTo(companyId);
		assertThat(savedEmployee.getCompany().getCompanyType().getId()).isEqualTo(companyTypeId);
		assertThat(savedEmployee.getPosition().getId()).isEqualTo(positionId);
	}

	@Test
	void testCreateCompany() throws Exception {
		String testCompanyRegNumber = "111";
		String testCompanyName = "Udemy";
		String testCompanyAddress = "Frist str 2.";
		String companyType = "ZRT";
		long companyTypeId = createCompanyType(companyType);
		
		long companyId = createCompany(testCompanyRegNumber, testCompanyName, testCompanyAddress, companyTypeId);
		
		Optional<Company> savedCompanyOptional = companyRepository.findById(companyId);
		assertThat(savedCompanyOptional).isNotEmpty();
		Company savedCompany = savedCompanyOptional.get();
		assertThat(savedCompany.getRegistrationNumber()).isEqualTo(testCompanyRegNumber);
		assertThat(savedCompany.getName()).isEqualTo(testCompanyName);
		assertThat(savedCompany.getAddress()).isEqualTo(testCompanyAddress);
		assertThat(savedCompany.getCompanyType().getId()).isEqualTo(companyTypeId);
	}
	
	@Test
	void testAddEmployee() throws Exception {
		String testEmployeeName = "Tim Buchalka";
		int salary = 1209;

		String companyType = "ZRT";
		long companyTypeId = createCompanyType(companyType);
		
		String testCompanyRegNumber = "111";
		String testCompanyName = "Udemy";
		String testCompanyAddress = "Frist str 2.";
		long companyId = createCompany(testCompanyRegNumber, testCompanyName, testCompanyAddress, companyTypeId);
		
		String positionName = "Java Developer";
		String education = "főiskola";
		int minSalary = 1200;
		long positionId = createPosition(positionName, education, minSalary);
		
//		long employeeId = createEmployee(testEmployeeName, salary, companyId, positionId);
		Employee employee = new Employee(testEmployeeName, salary, LocalDateTime.now());
		Company company = companyRepository.getById(companyId);
		employee.setCompany(company);
		Position position = positionRepository.getById(positionId);
		employee.setPosition(position);
		long employeeId = employeeRepository.save(employee).getId();
//		Employee savedEmployee = employeeService.save(employee);
//		long employeeId = savedEmployee.getId();
		Employee savedEmployee = employeeRepository.getById(employeeId);
		
		
		
		
//		Employee savedEmployee = employeeRepository.getById(employeeId);
//		Company testCompany = companyService.addEmployee(companyId, savedEmployee);
		Company testCompany = companyRepository.findByIdWithEmployees(companyId).get();
		testCompany.addEmployee(savedEmployee);
		
		employeeService.save(savedEmployee);
		
		assertThat(testCompany.getEmployees().stream()
				.map(Employee::getId)
				.collect(Collectors.toList()))
				.containsExactly(employeeId);
	}
	
	private long createEmployee(String name, int salary, long companyId, long positionId) {
		Employee employee = new Employee(name, salary, LocalDateTime.now());
		Company company = companyRepository.getById(companyId);
		employee.setCompany(company);
		Position position = positionRepository.getById(positionId);
		employee.setPosition(position);
		return employeeRepository.save(employee).getId();
	}
	
	private long createCompany(String regNr, String name, String address, long companyTypeId) {
		Company company = new Company(regNr, name, address);
		CompanyType companyType = companyTypeRepository.getById(companyTypeId);
		company.setCompanyType(companyType);
		return companyRepository.save(company).getId();
	}
	
	private long createCompanyType(String companyType) {
		return companyTypeRepository.save(new CompanyType(companyType)).getId();
	}
	
	
	private long createPosition(String positionName, String education, int minSalary) {
		return positionRepository.save(new Position(positionName, education, minSalary)).getId();
	}
}
