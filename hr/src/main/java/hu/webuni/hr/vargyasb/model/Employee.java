package hu.webuni.hr.vargyasb.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@ManyToOne
	private Position position;
	
	private int salary;
	private LocalDateTime startOfEmployment;
	
	@ManyToOne
	private Company company;

	public Employee() {

	}

	public Employee(String name, int salary, LocalDateTime startOfEmployment) {
		this.name = name;
		this.salary = salary;
		this.startOfEmployment = startOfEmployment;
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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
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
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id == null) 
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
