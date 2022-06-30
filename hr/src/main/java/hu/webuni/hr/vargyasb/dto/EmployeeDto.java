package hu.webuni.hr.vargyasb.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import hu.webuni.hr.vargyasb.model.Position;

public class EmployeeDto {
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String position;
	@Min(value = 0, message = "Value must be positive")
	private int salary;
	@Past(message = "Start of Employment must be in the past")
	private LocalDateTime startOfEmployment;
	
	private CompanyDto company;

	public EmployeeDto() {

	}

	public EmployeeDto(Long id, String name, int salary, LocalDateTime localDateTime) {
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.startOfEmployment = localDateTime;
	}
	
	public EmployeeDto(String name, int salary, String position, LocalDateTime localDateTime) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startOfEmployment = localDateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDateTime getStartOfEmployment() {
		return startOfEmployment;
	}

	public void setStartOfEmployment(LocalDateTime startOfEmployment) {
		this.startOfEmployment = startOfEmployment;
	}

	public CompanyDto getCompany() {
		return company;
	}

	public void setCompany(CompanyDto company) {
		this.company = company;
	}
}
