package hu.webuni.hr.vargyasb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.repository.CompanyRepository;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Transactional
	public Company save(Company company) {
		return companyRepository.save(company);
	}
	
	public List<Company> findAll() {
		return companyRepository.findAll();
	}
	
	public Optional<Company> findById(Long id) {
		return companyRepository.findById(id);
	}
	
	@Transactional
	public void delete(Long id) {
		companyRepository.deleteById(id);
	}
	
	public Company addEmployee(long id, Employee employee) {
		Company company = companyRepository.findById(id).get();
		company.addEmployee(employee);
		
		employeeRepository.save(employee);
		return company;
	}
	
	public Company deleteEmployee(long id, long employeeId) {
		Company company = companyRepository.findById(id).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeRepository.save(employee);
		return company;
	}
	
	public Company replaceEmployees(long id, List<Employee> employees) {
		Company company = companyRepository.findById(id).get();
		for (Employee employee : company.getEmployees()) {
			employee.setCompany(null);
		}
		company.getEmployees().clear();
		for (Employee employee : employees) {
			company.addEmployee(employee);
			employeeRepository.save(employee);
		}
		return company;
	}
	
	//CompanyMappert updateli hogy employee-t is mappeljen
	//mapperbe megirjuk azt is hogy mappelje a companykat ugy hogy kihagy employee-kat ha az isfull bevan kapcs
}
