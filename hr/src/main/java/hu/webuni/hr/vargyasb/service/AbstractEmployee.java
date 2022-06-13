package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;

public abstract class AbstractEmployee implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Transactional
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}
	
//	@Transactional
//	public Employee update(Employee employee) {
//		if (employeeRepository.existsById(employee.getId())) {
//			return employeeRepository.save(employee);
//		} else {
//			throw new NoSuchElementException();
//		}
//	}
	
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}
	
	public Optional<Employee> findById(Long id) {
		return employeeRepository.findById(id);
	}
	
	@Transactional
	public void delete(Long id) {
		employeeRepository.deleteById(id);
	}
	
	public List<Employee> getEmployeesWhoseSalaryIsGreaterThan(int salary, Integer pageNr, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNr, pageSize);
		Page<Employee> pagedResult = employeeRepository.findBySalaryGreaterThan(salary, paging);
		
//		return employeeRepository.findBySalaryGreaterThan(salary, paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Employee>();
		}
	}

	public List<Employee> findByPosition(String position) {
		return employeeRepository.findByPosition(position);
	}

	public List<Employee> findByNameStartingWithIgnoreCase(String keyword) {
		return employeeRepository.findByNameStartingWithIgnoreCase(keyword);
	}

	public List<Employee> findByStartOfEmploymentBetween(LocalDateTime from, LocalDateTime to) {
		return employeeRepository.findByStartOfEmploymentBetween(from, to);
	}
	
	public List<IAvgSalaryByPosition> averageDescSalaryByPositionInACompany(long companyId) {
		return employeeRepository.averageDescSalaryByPositionInACompany(companyId);
	}
}
