package hu.webuni.hr.vargyasb.service;

import java.util.List;

import hu.webuni.hr.vargyasb.model.Employee;

public interface EmployeeService {
	int getPayRaisePercent(Employee employee);

	Employee save(Employee employee);

	List<Employee> findAll();

	Employee findById(Long id);

	void delete(Long id);
	
	List<Employee> getEmployeesWhoseSalaryIsGreaterThan(int salary);
}
