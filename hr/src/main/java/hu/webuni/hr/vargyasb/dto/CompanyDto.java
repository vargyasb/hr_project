package hu.webuni.hr.vargyasb.dto;

import java.util.ArrayList;
import java.util.List;

import hu.webuni.hr.vargyasb.model.CompanyType;

public class CompanyDto {
	private Long id;
	private String registrationNumber;
	private String name;
	private String address;
	private List<EmployeeDto> employees = new ArrayList<>();
	
	private CompanyType companyType;

	public CompanyDto(Long id, String registrationNumber, String name, String address, CompanyType companyType) {
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.companyType = companyType;
	}

	public CompanyDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<EmployeeDto> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}

	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

}
