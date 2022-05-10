package hu.webuni.hr.vargyasb.service;

import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.model.Employee;

@Service
public class SalaryService {

	private EmployeeService employeeService;

	public SalaryService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public int calculateNewSalaryFor(Employee employee) {
		return employeeService.getPayRaisePercent(employee) == 0 
				? employee.getSalary() 
				: (employee.getSalary() / 100 * (100 + employeeService.getPayRaisePercent(employee)));
	}
	
}
