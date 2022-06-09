package hu.webuni.hr.vargyasb.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<EmployeeDto> getAll() {
		return employeeMapper.employeesToDtos(employeeService.findAll());
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
		findByIdOrThrowNotFound(id);
		
		employeeDto.setId(id);
		Employee employee = employeeService.save(employeeMapper.employeeDtoToEmployee(employeeDto));
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
		findByIdOrThrowNotFound(id);
		employeeService.delete(id);
	}

	@GetMapping("/filter")
	public List<EmployeeDto> getEmployeesWhoseSalaryIsGreaterThan(@RequestParam int salary) {
		return employeeMapper.employeesToDtos(employeeService.getEmployeesWhoseSalaryIsGreaterThan(salary));
	}
	
	@PostMapping("/payRaise")
	public int getPayRaisePercent(@RequestBody EmployeeDto employeeDto) {
		return employeeService.getPayRaisePercent(employeeMapper.employeeDtoToEmployee(employeeDto));
	}
	
	@GetMapping("/filterbyposition")
	public List<EmployeeDto> getEmployeesByPosition(@RequestParam String position) {
		return employeeMapper.employeesToDtos(employeeService.findByPosition(position));
	}
	
	@GetMapping("/filterbyname")
	public List<EmployeeDto> getEmployeesNameStartingWith(@RequestParam String keyword) {
		return employeeMapper.employeesToDtos(employeeService.findByNameStartingWithIgnoreCase(keyword));
	}
	
	@GetMapping("/filterbystartdate")
	public List<EmployeeDto> getEmployeesByStartOfEmployment(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		return employeeMapper.employeesToDtos(employeeService.findByStartOfEmploymentBetween(from, to));
	}
	
	private EmployeeDto findByIdOrThrowNotFound(long id) {
		Employee employee = employeeService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return employeeMapper.employeeToDto(employee);
	}

}
