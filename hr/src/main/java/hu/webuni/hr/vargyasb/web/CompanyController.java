package hu.webuni.hr.vargyasb.web;

import java.util.List;
import java.util.stream.Collectors;

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

import hu.webuni.hr.vargyasb.dto.CompanyDto;
import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.mapper.CompanyMapper;
import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.service.CompanyService;

@RestController
@RequestMapping("api/companies")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CompanyMapper companyMapper;

	@GetMapping
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full) {
		if (full != null && full) {
			return companyMapper.companiesToCompanyDtos(companyService.findAll());
		}

		return companyMapper.companiesToCompanyDtos(companyService.findAll()).stream()
				.map(c -> new CompanyDto(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress()))
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public CompanyDto findById(@PathVariable long id) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);
		return companyDto;
	}

	@PostMapping
	public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
		Company company = companyService.save(companyMapper.companyDtoToCompany(companyDto));
		return companyMapper.companyToCompanyDto(company);
	}

	@PutMapping("/{id}")
	public CompanyDto modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		if (companyService.findById(id) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		companyDto.setId(id);
		Company company = companyService.save(companyMapper.companyDtoToCompany(companyDto));
		return companyMapper.companyToCompanyDto(company);
	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companyService.delete(id);
	}

	@PostMapping("/{id}/addEmployee")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);
		
		companyDto.getEmployees().add(employeeDto);
		return companyDto;
	}

	@DeleteMapping("/{id}/deleteEmployee/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);

		companyDto.getEmployees().removeIf(e -> e.getId() == employeeId);
		return companyDto;
	}

	@PutMapping("/{id}/addEmployee")
	public CompanyDto changeEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
		CompanyDto companyDto = findByIdOrThrowNotFound(id);

		companyDto.setEmployees(employees);
		return companyDto;
	}

	private CompanyDto findByIdOrThrowNotFound(long id) {
		if (companyService.findById(id) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return companyMapper.companyToCompanyDto(companyService.findById(id));
	}

}
