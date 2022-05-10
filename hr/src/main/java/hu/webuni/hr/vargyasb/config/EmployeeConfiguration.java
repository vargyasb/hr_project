package hu.webuni.hr.vargyasb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.vargyasb.service.DefaultEmployeeService;
import hu.webuni.hr.vargyasb.service.EmployeeService;

@Configuration
@Profile("!smart")
public class EmployeeConfiguration {

	@Bean
	public EmployeeService employeeService() {
		return new DefaultEmployeeService();
	}
}
