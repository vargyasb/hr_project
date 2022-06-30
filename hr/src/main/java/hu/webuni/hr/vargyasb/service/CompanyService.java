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
	
	@Autowired
	EmployeeService employeeService;
	
	@Transactional
	public Company save(Company company) {
		return companyRepository.save(company);
	}
	
	public List<Company> findAll() {
		return companyRepository.findAll();
	}
	
	public List<Company> findAllWithEmployees() {
		return companyRepository.findAllWithEmployees();
	}
	
	public Optional<Company> findById(Long id) {
		return companyRepository.findById(id);
	}
	
	public Optional<Company> findByIdWithEmployees(Long id) {
		return companyRepository.findByIdWithEmployees(id);
	}
	
	@Transactional
	public void delete(Long id) {
		companyRepository.deleteById(id);
	}
	
	@Transactional
	public Company addEmployee(long id, Employee employee) {
		Company company = companyRepository.findByIdWithEmployees(id).get();
		company.addEmployee(employee);
		
		employeeService.save(employee);
		return company;
	}
	
	public Company deleteEmployee(long id, long employeeId) {
		Company company = companyRepository.findByIdWithEmployees(id).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		employee.setCompany(null);
		employee.setPosition(null);
		company.getEmployees().remove(employee);
		employeeService.save(employee);
		return company;
	}
	
	@Transactional
	public Company replaceEmployees(long id, List<Employee> employees) {
		Company company = companyRepository.findByIdWithEmployees(id).get();
		for (Employee employee : company.getEmployees()) {
			employee.setCompany(null);
			employee.setPosition(null);
			employeeService.save(employee);
		}
		company.getEmployees().clear();
		for (Employee employee : employees) {
			company.addEmployee(employee);
			employeeService.save(employee);
		}
		return company;
	}
	
	public List<Company> findCompanyByEmployeeSalaryGreaterThan(int salary) {
		return companyRepository.findDistinctCompanyByEmployeesSalaryGreaterThan(salary);
	}
	
	public List<Company> findCompaniesWhereMoreEmployeeWorksThan(long limit) {
		return companyRepository.findCompaniesWhereMoreEmployeeWorksThan(limit);
	}
	
	public List<IAvgSalaryByPosition> averageDescSalaryByPositionInACompany(long companyId) {
		return companyRepository.averageDescSalaryByPositionInACompany(companyId);
	}
}
