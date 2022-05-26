package hu.webuni.hr.vargyasb.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.service.EmployeeService;
import hu.webuni.hr.vargyasb.service.SalaryService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	private Map<Long, EmployeeDto> employees = new HashMap<>();

	{
		employees.put(1L, new EmployeeDto(1L, "Steve", "Game Dev", 3000, LocalDateTime.of(2010, 10, 13, 10, 25)));
		employees.put(2L, new EmployeeDto(2L, "Peter", "HR Associate", 2000, LocalDateTime.of(2015, 10, 13, 10, 25)));
		employees.put(3L, new EmployeeDto(3L, "Anna", "Scrum Master", 2800, LocalDateTime.of(2020, 05, 10, 10, 25)));
		employees.put(4L, new EmployeeDto(4L, "David", "Project Manager", 3200, LocalDateTime.of(2021, 10, 13, 10, 25)));
	}

	// Osszes alkalmazott visszaadasa
	@GetMapping
	public List<EmployeeDto> getAll() {
		return new ArrayList<>(employees.values());
	}

	// Adott id-ju alkalmazott visszaadasa
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
		EmployeeDto employeeDto = employees.get(id);

		if (employeeDto == null)
			return ResponseEntity.notFound().build();
		else
			return ResponseEntity.ok(employeeDto);
	}

	// Uj alkalmazott felvetele
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody EmployeeDto employeeDto) {
		employees.put(employeeDto.getId(), employeeDto);
		return employeeDto;
	}

	// Meglevo alkalmazott modositasa
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		if (!employees.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}

		employeeDto.setId(id);
		employees.put(id, employeeDto);
		return ResponseEntity.ok(employeeDto);
	}

	// Meglevo alkalmazott torlese
	@DeleteMapping("/{id}")
	public void deleteEmpolyee(@PathVariable long id) {
		employees.remove(id);
	}

	//Egy query parameterben megkapott erteknel magasabb havi fizetesu alkalmazottak
	@GetMapping("/filter")
	public List<EmployeeDto> getEmployeesWhoseSalaryIsGreaterThan(@RequestParam int salary) {
		return employees.values()
				.stream()
				.filter(e -> e.getSalary() > salary)
				.collect(Collectors.toList());
	}
	
	@PostMapping("/payRaise")
	public int getPayRaisePercent(@RequestBody Employee employee) {
		return employeeService.getPayRaisePercent(employee);
	}

}
