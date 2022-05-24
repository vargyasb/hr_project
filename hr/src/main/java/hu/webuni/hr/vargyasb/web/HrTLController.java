package hu.webuni.hr.vargyasb.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hu.webuni.hr.vargyasb.model.Employee;

@Controller
public class HrTLController {

	private List<Employee> employees = new ArrayList<>();
	
	{
		employees.add(new Employee(1L, "Steve", "Game Dev", 3000, LocalDateTime.of(2010, 10, 13, 10, 25)));
		employees.add(new Employee(2L, "Peter", "HR Associate", 2000, LocalDateTime.of(2015, 10, 13, 10, 25)));
		employees.add(new Employee(3L, "Anna", "Scrum Master", 2800, LocalDateTime.of(2020, 05, 10, 10, 25)));
		employees.add(new Employee(4L, "David", "Project Manager", 3200, LocalDateTime.of(2021, 10, 13, 10, 25)));
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/employees")
	public String listEmployees(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new Employee());
		return "employees";
	}
	
	@PostMapping("/employees")
	public String addEmployee(Employee employee) {
		employees.add(employee);
		return "redirect:employees";
	}
	
	@GetMapping("/employee")
	public String selectEmployee(@RequestParam(name = "id") long id, Map<String, Object> model) {
		//ide valahogy egy Employeenak kell bejonnie parameternek, amit berakok az employees Listába
		//es redirect-elek
		for (Employee employee : employees) {
			if (employee.getId() == id) {
				model.put("newEmployee", employee);
			}
		}
		return "employee";
	}
	
}
