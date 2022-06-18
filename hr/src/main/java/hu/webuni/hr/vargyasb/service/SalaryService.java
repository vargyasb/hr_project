package hu.webuni.hr.vargyasb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.PositionDetailsByCompany;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.PositionDetailsByCompanyRepository;

@Service
public class SalaryService {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

//	public SalaryService(EmployeeService employeeService) {
//		this.employeeService = employeeService;
//	}
	
	public int calculateNewSalaryFor(Employee employee) {
		return employeeService.getPayRaisePercent(employee) == 0 
				? employee.getSalary() 
				: (employee.getSalary() / 100 * (100 + employeeService.getPayRaisePercent(employee)));
	}
	
	public void raiseMinSalary(long companyId, String positionName, int minSalary) {
		PositionDetailsByCompany pd = positionDetailsByCompanyRepository.findByPositionNameAndCompanyId(positionName, companyId).get();
		
		pd.setMinSalary(minSalary);
		
		//1. megoldás, nem hatékony, mert annyi SQL UPDATE utasítás lesz, ahány employee-nek átállítottam a fizetését
//		pd.getCompany().getEmployees().forEach(e -> {
//			if (e.getPosition().getName().equals(positionName) && e.getSalary() < minSalary) 
//				e.setSalary(minSalary);
//		});
		
		//2. megoldás: UPDATE QUERY
		employeeRepository.updateSalaries(positionName, minSalary, companyId);
	}
	
}
