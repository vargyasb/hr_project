package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import hu.webuni.hr.vargyasb.model.Employee;

public interface EmployeeService {
	int getPayRaisePercent(Employee employee);

	Employee save(Employee employee);
	
	Employee update(Employee employee);

	List<Employee> findAll();

	Optional<Employee> findById(Long id);

	void delete(Long id);
	
	List<Employee> getEmployeesWhoseSalaryIsGreaterThan(int salary);
	
	List<Employee> findByPosition(String position);
	
	List<Employee> findByNameStartingWithIgnoreCase(String keyword);
	
	List<Employee> findByStartOfEmploymentBetween(LocalDateTime from, LocalDateTime to);
}
