package hu.webuni.hr.vargyasb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.config.HRConfigProperties;
import hu.webuni.hr.vargyasb.model.Employee;

@Service
public class DefaultEmployeeService implements EmployeeService{
	
	@Autowired
	HRConfigProperties hrConfig;

	@Override
	public int getPayRaisePercent(Employee employee) {
		return hrConfig.getSalary().getDef().getPercent();
	}

}
