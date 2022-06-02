package hu.webuni.hr.vargyasb.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.mapper.EmployeeMapper;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeMapper employeeMapper;
	
	// Osszes alkalmazott visszaadasa
	@GetMapping
	public List<EmployeeDto> getAll() {
		return employeeMapper.employeesToDtos(employeeService.findAll());
	}

	// Adott id-ju alkalmazott visszaadasa
	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable Long id) {
		Employee employee = employeeService.findById(id);

		if (employee != null)
			return employeeMapper.employeeToDto(employee);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	// Uj alkalmazott felvetele
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		Employee employee = employeeService.save(employeeMapper.employeeDtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}

	// Meglevo alkalmazott modositasa
	@PutMapping("/{id}")
	public EmployeeDto modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		if (employeeService.findById(id) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		employeeDto.setId(id);
		Employee employee = employeeService.save(employeeMapper.employeeDtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}

	// Meglevo alkalmazott torlese
	@DeleteMapping("/{id}")
	public void deleteEmpolyee(@PathVariable long id) {
		employeeService.delete(id);
	}

	//Egy query parameterben megkapott erteknel magasabb havi fizetesu alkalmazottak
	@GetMapping("/filter")
	public List<EmployeeDto> getEmployeesWhoseSalaryIsGreaterThan(@RequestParam int salary) {
		return employeeMapper.employeesToDtos(employeeService.getEmployeesWhoseSalaryIsGreaterThan(salary));
	}
	
	@PostMapping("/payRaise")
	public int getPayRaisePercent(@RequestBody EmployeeDto employeeDto) {
		return employeeService.getPayRaisePercent(employeeMapper.employeeDtoToEmployee(employeeDto));
	}

}
