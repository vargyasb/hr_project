package hu.webuni.hr.vargyasb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

	public Company update(Company company) {
		if (companyRepository.existsById(company.getId()))
			return companyRepository.save(company);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
		if (companyRepository.existsById(id)) {
			replaceEmployees(id, new ArrayList<>());
			companyRepository.deleteById(id);
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	@Transactional
	public Company addEmployee(long id, Employee employee) {
		Optional<Company> foundCompany = companyRepository.findByIdWithEmployees(id);
		if (foundCompany.isPresent()) {
			Company company = foundCompany.get();
			company.addEmployee(employee);
			employeeService.save(employee);
			return company;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	public Company deleteEmployee(long id, long employeeId) {
		Optional<Company> foundCompany = companyRepository.findByIdWithEmployees(id);
		Optional<Employee> foundEmployee = employeeRepository.findById(employeeId);
		if (foundCompany.isPresent() && foundEmployee.isPresent()) {
			Company company = foundCompany.get();
			Employee employee = foundEmployee.get();
			if (company.getEmployees().contains(employee)) {
				employee.setCompany(null);
				employee.setPosition(null);
				company.getEmployees().remove(employee);
				// employeeService.save(employee);
				return company;
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	public Company replaceEmployees(long id, List<Employee> employees) {
		Optional<Company> foundCompany = companyRepository.findByIdWithEmployees(id);
		if (foundCompany.isPresent()) {
			Company company = foundCompany.get();
			for (Employee employee : company.getEmployees()) {
				employee.setCompany(null);
				employee.setPosition(null);
				// employeeService.save(employee);
			}
			company.getEmployees().clear();
			for (Employee employee : employees) {
				company.addEmployee(employee);
				employeeService.save(employee);
			}
			return company;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
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
