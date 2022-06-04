package hu.webuni.hr.vargyasb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hu.webuni.hr.vargyasb.model.Company;

@Service
public class CompanyService {

	private Map<Long, Company> companies = new HashMap<>();

	{
		companies.put(1L, new Company(1L, "123-456", "Webuni", "First str. 10"));
		companies.put(2L, new Company(2L, "789-123", "Udemy", "Second str. 30"));
		companies.put(3L, new Company(3L, "456-555", "Coursera", "Third str. 40"));
		companies.put(4L, new Company(4L, "111-456", "Codeacademy", "Fourt str. 20"));
	}
	
	public Company save(Company company) {
		companies.put(company.getId(), company);
		return company;
	}
	
	public List<Company> findAll() {
		return new ArrayList<>(companies.values());
	}
	
	public Company findById(Long id) {
		return companies.get(id);
	}
	
	public void delete(Long id) {
		companies.remove(id);
	}
	
}
