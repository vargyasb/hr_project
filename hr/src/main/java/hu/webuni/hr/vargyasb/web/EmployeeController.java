package hu.webuni.hr.vargyasb.web;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
	
	@GetMapping
	public List<EmployeeDto> getAll(Pageable pageable) {
		return employeeMapper.employeesToDtos(employeeService.findAll(pageable).getContent());
	}

	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable Long id) {
		return findByIdOrThrowNotFound(id);
	}

	@PostMapping
	public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		employeeDto.setId(null);
		Employee employee = employeeService.save(employeeMapper.employeeDtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}

	@PutMapping("/{id}")
	public EmployeeDto modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		employeeDto.setId(id);
		Employee employee = employeeService.update(employeeMapper.employeeDtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
//		try {
//			Employee employee = employeeService.update(employeeMapper.employeeDtoToEmployee(employeeDto));
//			return employeeMapper.employeeToDto(employee);
//		} catch (NoSuchElementException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//		}
	}

	@DeleteMapping("/{id}")
	public void deleteEmpolyee(@PathVariable long id) {
		employeeService.delete(id);
	}

	@GetMapping(params = "minSalary")
	public List<EmployeeDto> getEmployeesWhoseSalaryIsGreaterThan(@RequestParam int salary,
			@RequestParam(defaultValue = "0") Integer pageNr, @RequestParam(defaultValue = "5") Integer pageSize) {
		return employeeMapper.employeesToDtos(employeeService.getEmployeesWhoseSalaryIsGreaterThan(salary, pageNr, pageSize));
	}
	
	@PostMapping("/payRaise")
	public int getPayRaisePercent(@RequestBody EmployeeDto employeeDto) {
		return employeeService.getPayRaisePercent(employeeMapper.employeeDtoToEmployee(employeeDto));
	}
	
	@GetMapping(params = "position")
	public List<EmployeeDto> getEmployeesByPosition(@RequestParam String position) {
		return employeeMapper.employeesToDtos(employeeService.findByPosition(position));
	}
	
	@GetMapping(params = "name")
	public List<EmployeeDto> getEmployeesNameStartingWith(@RequestParam String name) {
		return employeeMapper.employeesToDtos(employeeService.findByNameStartingWithIgnoreCase(name));
	}
	
	@GetMapping(params = {"from", "to"})
	public List<EmployeeDto> getEmployeesByStartOfEmployment(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		return employeeMapper.employeesToDtos(employeeService.findByStartOfEmploymentBetween(from, to));
	}
	
	@PostMapping(params = "example")
	public List<EmployeeDto> getEmployeesByExample(@RequestBody EmployeeDto employeeDto) {
		return employeeMapper.employeesToDtos(employeeService.findEmployeesByExample(employeeMapper.employeeDtoToEmployee(employeeDto)));
	}
	
	private EmployeeDto findByIdOrThrowNotFound(long id) {
		Employee employee = employeeService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return employeeMapper.employeeToDto(employee);
	}

}
