package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.CompanyType;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.Position;
import hu.webuni.hr.vargyasb.repository.CompanyRepository;
import hu.webuni.hr.vargyasb.repository.CompanyTypeRepository;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.PositionRepository;

@Service
public class InitDbService {

	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyTypeRepository companyTypeRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	public void clearDB() {
		employeeRepository.deleteAll();
		companyRepository.deleteAll();
	}
	
	public void insertTestData() {
		
		CompanyType zrt = new CompanyType("ZRT");
		companyTypeRepository.save(zrt);
		CompanyType nyrt = new CompanyType("NYRT");
		companyTypeRepository.save(nyrt);
		CompanyType kft = new CompanyType("KFT");
		companyTypeRepository.save(kft);
		CompanyType bt = new CompanyType("BT");
		companyTypeRepository.save(bt);
		
		Company udemy = new Company(null, "111", "Udemy", "First str 2", zrt);
		setCompanyTypeAndSaveCompanyToRepository(udemy, zrt);
		Company webuni = new Company(null, "222", "Webuni", "Second str 1", kft);
		setCompanyTypeAndSaveCompanyToRepository(webuni, kft);
		Company tipikMagyar = new Company(null, "333", "Tipik Magyar Kft", "Fő utca 3", kft);
		setCompanyTypeAndSaveCompanyToRepository(tipikMagyar, kft);
		
		
		
		Position javaDev = new Position("Java Developer", "főiskola", 1200);
		Position springDev = new Position("Spring Boot Developer", "főiskola", 1600);
		Position fullStackDev = new Position("Full Stack Developer", "egyetem", 1700);
		positionRepository.save(javaDev);
		positionRepository.save(springDev);
		positionRepository.save(fullStackDev);
		
		setCompanyAndSaveToRepository(new Employee(null, "Tim Buchalka", 1209, LocalDateTime.of(2016, 05, 22, 10, 10)), udemy, javaDev);
		setCompanyAndSaveToRepository(new Employee(null, "John Thompson", 1899, LocalDateTime.of(2018, 11, 22, 10, 10)), udemy, springDev);
		setCompanyAndSaveToRepository(new Employee(null, "Chad Darby", 2100, LocalDateTime.of(2017, 11, 22, 10, 10)), udemy, springDev);
		setCompanyAndSaveToRepository(new Employee(null, "Colt Steele", 1899, LocalDateTime.of(2018, 11, 22, 10, 10)), udemy, fullStackDev);
		setCompanyAndSaveToRepository(new Employee(null, "Goran Lochert", 1399, LocalDateTime.of(2018, 11, 22, 10, 10)), udemy, javaDev);		
		
		Position angularTeacher = new Position("Angular Teacher", "főiskola", 1100);
		Position reactTeacher = new Position("React Teacher", "főiskola", 1100);
		positionRepository.save(angularTeacher);
		positionRepository.save(reactTeacher);
		
		setCompanyAndSaveToRepository(new Employee(null, "Oktató 1", 1123, LocalDateTime.of(2021, 04, 21, 22, 01)), webuni, angularTeacher);
		setCompanyAndSaveToRepository(new Employee(null, "Oktató 2", 1333, LocalDateTime.of(2022, 04, 21, 22, 01)), webuni, reactTeacher);
		setCompanyAndSaveToRepository(new Employee(null, "Oktató 3 ", 1400, LocalDateTime.of(2018, 04, 21, 22, 01)), webuni, angularTeacher);
		setCompanyAndSaveToRepository(new Employee(null, "Oktató 4", 1111, LocalDateTime.of(2019, 04, 21, 22, 01)), webuni, reactTeacher);
		
		Position logMunkatars = new Position("Logisztikai munkatárs", "érettségi", 900);
		Position itEngineer = new Position("IT Engineer", "egyetem", 1900);
		Position belsoEllenor = new Position("Belső ellenőr", "főiskola", 1100);
		Position munkas = new Position("Munkás", "érettségi", 700);
		positionRepository.save(logMunkatars);
		positionRepository.save(itEngineer);
		positionRepository.save(belsoEllenor);
		positionRepository.save(munkas);
		
		
		setCompanyAndSaveToRepository(new Employee(null, "Nagy Árpi", 1999, LocalDateTime.of(2010, 10, 13, 10, 25)), tipikMagyar, logMunkatars);
		setCompanyAndSaveToRepository(new Employee(null, "Kiss Jenő", 3888, LocalDateTime.of(2012, 06, 13, 10, 25)), tipikMagyar, itEngineer);
		setCompanyAndSaveToRepository(new Employee(null, "Kovács János", 1255, LocalDateTime.of(2015, 03, 13, 10, 25)), tipikMagyar, munkas);
		setCompanyAndSaveToRepository(new Employee(null, "Horváth Géza", 1100, LocalDateTime.of(2016, 03, 13, 10, 25)), tipikMagyar, logMunkatars);
		setCompanyAndSaveToRepository(new Employee(null, "Varga Sándor", 3000, LocalDateTime.of(2017, 03, 13, 10, 25)), tipikMagyar, itEngineer);
		setCompanyAndSaveToRepository(new Employee(null, "Tamás István", 2500, LocalDateTime.of(2014, 03, 13, 10, 25)), tipikMagyar, itEngineer);
		setCompanyAndSaveToRepository(new Employee(null, "Laci", 800, LocalDateTime.of(2015, 03, 13, 10, 25)), tipikMagyar, munkas);
		setCompanyAndSaveToRepository(new Employee(null, "Tóth Péter", 2000, LocalDateTime.of(2020, 03, 13, 10, 25)), tipikMagyar, belsoEllenor);
		setCompanyAndSaveToRepository(new Employee(null, "Németh Endre", 2222, LocalDateTime.of(2022, 03, 13, 10, 25)), tipikMagyar, itEngineer);
		setCompanyAndSaveToRepository(new Employee(null, "Takács Benedek", 1111, LocalDateTime.of(2008, 03, 13, 10, 25)), tipikMagyar, logMunkatars);
		setCompanyAndSaveToRepository(new Employee(null, "Kiss Balázs", 1400, LocalDateTime.of(2010, 03, 13, 10, 25)), tipikMagyar, belsoEllenor);
		setCompanyAndSaveToRepository(new Employee(null, "Pista", 987, LocalDateTime.of(1987, 03, 13, 10, 25)), tipikMagyar, munkas);
		setCompanyAndSaveToRepository(new Employee(null, "Józsi", 887, LocalDateTime.of(1986, 03, 13, 10, 25)), tipikMagyar, munkas);
	}
	
	private void setCompanyAndSaveToRepository(Employee employee, Company company, Position position) {
		employee.setCompany(company);
		employee.setPosition(position);
		employeeRepository.save(employee);
	}
	
	private void setCompanyTypeAndSaveCompanyToRepository(Company company, CompanyType companyType) {
		company.setCompanyType(companyType);
		companyRepository.save(company);
	}
}
