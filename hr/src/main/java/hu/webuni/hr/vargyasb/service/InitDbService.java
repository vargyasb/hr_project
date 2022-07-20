package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.CompanyType;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.Position;
import hu.webuni.hr.vargyasb.model.PositionDetailsByCompany;
import hu.webuni.hr.vargyasb.repository.CompanyRepository;
import hu.webuni.hr.vargyasb.repository.CompanyTypeRepository;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.PositionDetailsByCompanyRepository;
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
	
	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
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
		
		Company udemy = new Company("111", "Udemy", "First str 2");
		setCompanyTypeAndSaveCompanyToRepository(udemy, zrt);
		Company webuni = new Company("222", "Webuni", "Second str 1");
		setCompanyTypeAndSaveCompanyToRepository(webuni, kft);
		Company tipikMagyar = new Company("333", "Tipik Magyar Kft", "Fő utca 3");
		setCompanyTypeAndSaveCompanyToRepository(tipikMagyar, kft);
		
		
		
		Position javaDev = new Position("Java Developer", "főiskola", 1200);
		Position springDev = new Position("Spring Boot Developer", "főiskola", 1600);
		Position fullStackDev = new Position("Full Stack Developer", "egyetem", 1700);
		positionRepository.save(javaDev);
		positionRepository.save(springDev);
		positionRepository.save(fullStackDev);
		
		Employee manager1 = setCompanyAndSaveToRepository(new Employee("Tim Buchalka", /*pozicio volt itt,*/1209, LocalDateTime.of(2016, 05, 22, 10, 10)), udemy, javaDev, "timb", "pass", null);
		setCompanyAndSaveToRepository(new Employee("John Thompson", /*pozicio volt itt,*/1899, LocalDateTime.of(2018, 11, 22, 10, 10)), udemy, springDev, "johnt", "pass", manager1);
		setCompanyAndSaveToRepository(new Employee("Chad Darby", /*pozicio volt itt,*/2100, LocalDateTime.of(2017, 11, 22, 10, 10)), udemy, springDev, "chadd", "pass", manager1);
		setCompanyAndSaveToRepository(new Employee("Colt Steele", /*pozicio volt itt,*/1899, LocalDateTime.of(2018, 11, 22, 10, 10)), udemy, fullStackDev, "colts", "pass", manager1);
		setCompanyAndSaveToRepository(new Employee("Goran Lochert", /*pozicio volt itt,*/1399, LocalDateTime.of(2018, 11, 22, 10, 10)), udemy, javaDev, "goranl", "pass", manager1);		
		
		Position angularTeacher = new Position("Angular Teacher", "főiskola", 1100);
		Position reactTeacher = new Position("React Teacher", "főiskola", 1100);
		positionRepository.save(angularTeacher);
		positionRepository.save(reactTeacher);
		
		Employee manager2 = setCompanyAndSaveToRepository(new Employee("Oktató 1", /*pozicio volt itt,*/1123, LocalDateTime.of(2021, 04, 21, 22, 01)), webuni, angularTeacher, "okt1", "pass", null);
		setCompanyAndSaveToRepository(new Employee("Oktató 2", /*pozicio volt itt,*/1333, LocalDateTime.of(2022, 04, 21, 22, 01)), webuni, reactTeacher, "okt2", "pass", manager2);
		setCompanyAndSaveToRepository(new Employee("Oktató 3 ", /*pozicio volt itt,*/1400, LocalDateTime.of(2018, 04, 21, 22, 01)), webuni, angularTeacher, "okt3", "pass", manager2);
		setCompanyAndSaveToRepository(new Employee("Oktató 4", /*pozicio volt itt,*/1111, LocalDateTime.of(2019, 04, 21, 22, 01)), webuni, reactTeacher, "okt4", "pass", manager2);
		
		Position logMunkatars = new Position("Logisztikai munkatárs", "érettségi", 900);
		Position itEngineer = new Position("IT Engineer", "egyetem", 1900);
		Position belsoEllenor = new Position("Belső ellenőr", "főiskola", 1100);
		Position munkas = new Position("Munkás", "érettségi", 700);
		positionRepository.save(logMunkatars);
		positionRepository.save(itEngineer);
		positionRepository.save(belsoEllenor);
		positionRepository.save(munkas);
		
		
		Employee manager3 = setCompanyAndSaveToRepository(new Employee("Nagy Árpi", /*pozicio volt itt,*/1999, LocalDateTime.of(2010, 10, 13, 10, 25)), tipikMagyar, logMunkatars, "nagya", "pass", null);
		setCompanyAndSaveToRepository(new Employee("Kiss Jenő", /*pozicio volt itt,*/3888, LocalDateTime.of(2012, 06, 13, 10, 25)), tipikMagyar, itEngineer, "kissj", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Kovács János", /*pozicio volt itt,*/1255, LocalDateTime.of(2015, 03, 13, 10, 25)), tipikMagyar, munkas, "kovacsj", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Horváth Géza", /*pozicio volt itt,*/1100, LocalDateTime.of(2016, 03, 13, 10, 25)), tipikMagyar, logMunkatars, "horvathg", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Varga Sándor", /*pozicio volt itt,*/3000, LocalDateTime.of(2017, 03, 13, 10, 25)), tipikMagyar, itEngineer, "vargas", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Tamás István", /*pozicio volt itt,*/2500, LocalDateTime.of(2014, 03, 13, 10, 25)), tipikMagyar, itEngineer, "tamasi", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Laci", /*pozicio volt itt,*/800, LocalDateTime.of(2015, 03, 13, 10, 25)), tipikMagyar, munkas, "laci", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Tóth Péter", /*pozicio volt itt,*/2000, LocalDateTime.of(2020, 03, 13, 10, 25)), tipikMagyar, belsoEllenor, "tothp", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Németh Endre", /*pozicio volt itt,*/2222, LocalDateTime.of(2022, 03, 13, 10, 25)), tipikMagyar, itEngineer, "nemethe", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Takács Benedek", /*pozicio volt itt,*/1111, LocalDateTime.of(2008, 03, 13, 10, 25)), tipikMagyar, logMunkatars, "takacsb", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Kiss Balázs", /*pozicio volt itt,*/1400, LocalDateTime.of(2010, 03, 13, 10, 25)), tipikMagyar, belsoEllenor, "kissb", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Pista", /*pozicio volt itt,*/987, LocalDateTime.of(1987, 03, 13, 10, 25)), tipikMagyar, munkas, "pista", "pass", manager3);
		setCompanyAndSaveToRepository(new Employee("Józsi", /*pozicio volt itt,*/887, LocalDateTime.of(1986, 03, 13, 10, 25)), tipikMagyar, munkas, "jozsi", "pass", manager3);
		
		PositionDetailsByCompany pd = new PositionDetailsByCompany();
		pd.setCompany(tipikMagyar);
		pd.setPosition(belsoEllenor);
		pd.setMinSalary(1500);
		positionDetailsByCompanyRepository.save(pd);
		
		PositionDetailsByCompany pd2 = new PositionDetailsByCompany();
		pd2.setCompany(tipikMagyar);
		pd2.setPosition(munkas);
		pd2.setMinSalary(1000);
		positionDetailsByCompanyRepository.save(pd2);
	}
	
	private Employee setCompanyAndSaveToRepository(Employee employee, Company company, Position position, String username, String password, Employee manager) {
		employee.setCompany(company);
		employee.setPosition(position);
		employee.setUsername(username);
		employee.setPassword(passwordEncoder.encode(password));
		if (manager != null)
			employee.setManager(manager);
		return employeeRepository.save(employee);
	}
	
	private void setCompanyTypeAndSaveCompanyToRepository(Company company, CompanyType companyType) {
		company.setCompanyType(companyType);
		companyRepository.save(company);
	}
	
}
