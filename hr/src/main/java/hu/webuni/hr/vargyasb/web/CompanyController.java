package hu.webuni.hr.vargyasb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.vargyasb.dto.CompanyDto;

@RestController
@RequestMapping("api/companies")
public class CompanyController {

	private Map<String, CompanyDto> companies = new HashMap<>();

	{
		companies.put("123-456", new CompanyDto("123-456", "Webuni", "First str. 10"));
		companies.put("789-123", new CompanyDto("789-123", "Udemy", "Second str. 30"));
		companies.put("456-555", new CompanyDto("456-555", "Coursera", "Third str. 40"));
		companies.put("111-456", new CompanyDto("111-456", "Codeacademy", "Fourt str. 20"));
	}

	@GetMapping
	public List<CompanyDto> getAll() {
		return new ArrayList<>(companies.values());
	}

	@GetMapping("/{regNr}")
	public ResponseEntity<CompanyDto> findByRegNr(@PathVariable String regNr) {
		if (companies.get(regNr) != null) {
			return ResponseEntity.ok(companies.get(regNr));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
		companies.put(companyDto.getRegistrationNumber(), companyDto);
		return companyDto;
	}

	@PutMapping("/{regNr}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable String regNr, @RequestBody CompanyDto companyDto) {
		if (!companies.containsKey(regNr)) {
			return ResponseEntity.notFound().build();
		}

		companyDto.setRegistrationNumber(regNr);
		companies.put(regNr, companyDto);
		return ResponseEntity.ok(companyDto);
	}

	@DeleteMapping("/{regNr}")
	public void deleteCompany(@PathVariable String regNr) {
		companies.remove(regNr);
	}

}
