package hu.webuni.hr.vargyasb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.vargyasb.dto.CompanyDto;
import hu.webuni.hr.vargyasb.dto.EmployeeDto;

@RestController
@RequestMapping("api/companies")
public class CompanyController {

	private Map<Long, CompanyDto> companies = new HashMap<>();

	{
		companies.put(1L, new CompanyDto(1L, "123-456", "Webuni", "First str. 10"));
		companies.put(2L, new CompanyDto(2L, "789-123", "Udemy", "Second str. 30"));
		companies.put(3L, new CompanyDto(3L, "456-555", "Coursera", "Third str. 40"));
		companies.put(4L, new CompanyDto(4L, "111-456", "Codeacademy", "Fourt str. 20"));
	}

	@GetMapping
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full) {
		if (full != null && full) {
			return new ArrayList<>(companies.values());
		}

		return companies.values().stream()
				.map(c -> new CompanyDto(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress()))
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> findById(@PathVariable long id) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);
		return ResponseEntity.ok(companyDto);
	}

	@PostMapping
	public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
		companies.put(companyDto.getId(), companyDto);
		return companyDto;
	}

	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		if (!companies.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}

		companyDto.setId(id);
		companies.put(id, companyDto);
		return ResponseEntity.ok(companyDto);
	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companies.remove(id);
	}

	@PostMapping("/{id}/addEmployee")
	public ResponseEntity<CompanyDto> addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);
		
		companyDto.getEmployees().add(employeeDto);
		return ResponseEntity.ok(companyDto);
	}

	@DeleteMapping("/{id}/deleteEmployee/{employeeId}")
	public ResponseEntity<CompanyDto> deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);

		companyDto.getEmployees().removeIf(e -> e.getId() == employeeId);
		return ResponseEntity.ok(companyDto);
	}

	@PutMapping("/{id}/addEmployee")
	public ResponseEntity<CompanyDto> changeEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);

		companyDto.setEmployees(employees);
		return ResponseEntity.ok(companyDto);
	}

	private CompanyDto findByIdOrThrowNotFound(long id) {
		if (!companies.containsKey(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return companies.get(id);
	}

}
