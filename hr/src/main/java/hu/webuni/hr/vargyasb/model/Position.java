package hu.webuni.hr.vargyasb.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.lang.Nullable;

@Entity
public class Position {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	@Nullable
	private String education;
	private int minSalary;
	
	@OneToMany(mappedBy = "position")
	private List<Employee> employees;
	
	public Position() {
		
	}
	
	public Position(String name, String education, int minSalary) {
		this.name = name;
		this.education = education;
		this.minSalary = minSalary;
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
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public int getMinSalary() {
		return minSalary;
	}
	public void setMinSalary(int minSalary) {
		this.minSalary = minSalary;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
}
