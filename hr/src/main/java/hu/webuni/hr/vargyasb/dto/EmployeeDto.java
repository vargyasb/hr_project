package hu.webuni.hr.vargyasb.dto;

import java.time.LocalDateTime;

public class EmployeeDto {
	private Long id;
	private String name;
	private String position;
	private int salary;
	private LocalDateTime startOfEmployment;

	public EmployeeDto() {

	}

	public EmployeeDto(Long id, String name, String position, int salary, LocalDateTime localDateTime) {
		this.id = id;
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
}
