package hu.webuni.hr.vargyasb.dto;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {
	private String registrationNumber;
	private String name;
	private String address;
	private List<EmployeeDto> employees;

	public CompanyDto(String registrationNumber, String name, String address) {
		super();
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.employees = new ArrayList<>();
	}

	public CompanyDto() {
		super();
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

}
