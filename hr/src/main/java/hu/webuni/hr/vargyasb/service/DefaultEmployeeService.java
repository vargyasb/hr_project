package hu.webuni.hr.vargyasb.service;

import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.model.Employee;

@Service
public class DefaultEmployeeService implements EmployeeService{

	@Override
	public int getPayRaisePercent(Employee employee) {
		return 5;
	}

}
