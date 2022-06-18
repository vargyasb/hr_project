package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import hu.webuni.hr.vargyasb.model.Employee;

public interface EmployeeService {
	int getPayRaisePercent(Employee employee);

	Employee save(Employee employee);
	
	//Employee update(Employee employee);

	Page<Employee> findAll(Pageable pageable);

	Optional<Employee> findById(Long id);

	void delete(Long id);
	
	List<Employee> getEmployeesWhoseSalaryIsGreaterThan(int salary, Integer pageNr, Integer pageSize);
	
	List<Employee> findByPosition(String position);
	
	List<Employee> findByNameStartingWithIgnoreCase(String keyword);
	
	List<Employee> findByStartOfEmploymentBetween(LocalDateTime from, LocalDateTime to);

}
