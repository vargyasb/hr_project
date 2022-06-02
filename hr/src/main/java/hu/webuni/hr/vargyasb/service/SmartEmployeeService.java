package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.config.HRConfigProperties;
import hu.webuni.hr.vargyasb.model.Employee;

@Service
public class SmartEmployeeService extends AbstractEmployee {
	
//	@Value("${hr.employee.smart.limitlow}")
//	private int limitLow;
//	
//	@Value("${hr.employee.smart.limitmid}")
//	private int limitMid;
//	
//	@Value("${hr.employee.smart.limithigh}")
//	private int limitHigh;
//		
//	@Value("${hr.employee.smart.percentlow}")
//	private int percentLow;
//	
//	@Value("${hr.employee.smart.percentmid}")
//	private int percentMid;
//	
//	@Value("${hr.employee.smart.percenthigh}")
//	private int percentHigh;

	@Autowired
	HRConfigProperties config;
		
	@Override
	public int getPayRaisePercent(Employee employee) {
		long years = ChronoUnit.YEARS.between(employee.getStartOfEmployment(), LocalDateTime.now());
		int percent = 0;

		if (years >= config.getSalary().getSmart().getLimitHigh()) {
			percent = config.getSalary().getSmart().getPercentHigh();
		} else if (years >= config.getSalary().getSmart().getLimitMid()) {
			percent = config.getSalary().getSmart().getPercentMid();
		} else if (years >= config.getSalary().getSmart().getLimitLow()) {
			percent = config.getSalary().getSmart().getPercentLow();
		}
//		if (years >= limitHigh) {
//			percent = percentHigh;
//		} else if (years >= limitMid
//				&& years < limitHigh) {
//			percent = percentMid;
//		} else if (years >= limitLow
//				&& years < limitMid) {
//			percent = percentLow;
//		}

		return percent;
	}

}
