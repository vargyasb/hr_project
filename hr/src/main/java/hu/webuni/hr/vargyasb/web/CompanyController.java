package hu.webuni.hr.vargyasb.web;

import java.util.List;

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
import hu.webuni.hr.vargyasb.mapper.EmployeeMapper;
import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.service.CompanyService;
import hu.webuni.hr.vargyasb.service.IAvgSalaryByPosition;

@RestController
@RequestMapping("api/companies")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;

	@GetMapping
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full) {
		List<Company> companies = companyService.findAll();
		return mapCompanies(companies, full);
	}

	@GetMapping("/{id}")
	public CompanyDto findById(@PathVariable long id, @RequestParam(required = false) Boolean full) {
		Company company = companyMapper.companyDtoToCompany(findByIdOrThrowNotFound(id));
		if (isFull(full)) {
			return companyMapper.companyToCompanyDto(company);
		} else {
			return companyMapper.companyToCompanyDtoWithNoEmployees(company); 
		}
	}

	@PostMapping
	public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
		companyDto.setId(null);
		Company company = companyService.save(companyMapper.companyDtoToCompany(companyDto));
		return companyMapper.companyToCompanyDto(company);
	}

	@PutMapping("/{id}")
	public CompanyDto modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		findByIdOrThrowNotFound(id);
		
		companyDto.setId(id);
		Company company = companyService.save(companyMapper.companyDtoToCompany(companyDto));
		return companyMapper.companyToCompanyDto(company);
	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		findByIdOrThrowNotFound(id); 
		companyService.delete(id);
	}

	@PostMapping("/{id}/addEmployee")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		findByIdOrThrowNotFound(id);
		return companyMapper.companyToCompanyDto(companyService.addEmployee(id, employeeMapper.employeeDtoToEmployee(employeeDto)));
	}

	@DeleteMapping("/{id}/deleteEmployee/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		findByIdOrThrowNotFound(id);
		return companyMapper.companyToCompanyDto(companyService.deleteEmployee(id, employeeId));
	}

	@PutMapping("/{id}/addEmployee")
	public CompanyDto changeEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
		findByIdOrThrowNotFound(id);
		return companyMapper.companyToCompanyDto(companyService.replaceEmployees(id, employeeMapper.employeeDtosToemployees(employees)));
	}
	
	@GetMapping(params = "minSalary")
	public List<CompanyDto> findCompanyByEmployeeSalaryGreaterThan(@RequestParam int minSalary, @RequestParam(required = false) boolean full) {
		List<Company> companies = companyService.findCompanyByEmployeeSalaryGreaterThan(minSalary);
		return mapCompanies(companies, full);
	}
	
	@GetMapping(params = "employeeLimit")
	public List<CompanyDto> findCompaniesWhereMoreEmployeeWorksThan(@RequestParam long employeeLimit, @RequestParam(required = false) boolean full) {
		List<Company> companies = companyService.findCompaniesWhereMoreEmployeeWorksThan(employeeLimit);
		return mapCompanies(companies, full);
	}
	
	@GetMapping("/{id}/avgsalarybyposition")
	public List<IAvgSalaryByPosition> averageDescSalaryByPositionInACompany(@PathVariable long id) {
		return companyService.averageDescSalaryByPositionInACompany(id);
	}

	private CompanyDto findByIdOrThrowNotFound(long id) {
		Company company = companyService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return companyMapper.companyToCompanyDto(company);
	}

	private boolean isFull(Boolean full) {
		return full != null && full;
	}
	
	private List<CompanyDto> mapCompanies(List<Company> companies, Boolean full) {
		if (isFull(full)) {
			return companyMapper.companiesToCompanyDtos(companies);
		} else {
			return companyMapper.companiesToCompanyDtosWithNoEmployees(companies);
		}
	}
	
}
