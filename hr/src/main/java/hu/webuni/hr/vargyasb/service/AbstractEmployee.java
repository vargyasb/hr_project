package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hu.webuni.hr.vargyasb.model.Employee;

public abstract class AbstractEmployee implements EmployeeService {

	private Map<Long, Employee> employees = new HashMap<>();

	{
		employees.put(1L, new Employee(1L, "Steve", "Game Dev", 3000, LocalDateTime.of(2010, 10, 13, 10, 25)));
		employees.put(2L, new Employee(2L, "Peter", "HR Associate", 2000, LocalDateTime.of(2015, 10, 13, 10, 25)));
		employees.put(3L, new Employee(3L, "Anna", "Scrum Master", 2800, LocalDateTime.of(2020, 05, 10, 10, 25)));
		employees.put(4L, new Employee(4L, "David", "Project Manager", 3200, LocalDateTime.of(2021, 10, 13, 10, 25)));
		employees.put(5L, new Employee(5L, "Michael", "Branch Manager", 1600, LocalDateTime.of(1995, 10, 13, 10, 25)));
	}
	
	public Employee save(Employee employee) {
		employees.put(employee.getId(), employee);
		return employee;
	}
	
	public List<Employee> findAll() {
		return new ArrayList<>(employees.values());
	}
	
	public Employee findById(Long id) {
		return employees.get(id);
	}
	
	public void delete(Long id) {
		employees.remove(id);
	}
	
	public List<Employee> getEmployeesWhoseSalaryIsGreaterThan(int salary) {
		return employees.values()
				.stream()
				.filter(e -> e.getSalary() > salary)
				.collect(Collectors.toList());
	}
}
