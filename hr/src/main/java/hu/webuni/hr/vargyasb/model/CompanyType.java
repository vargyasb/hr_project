package hu.webuni.hr.vargyasb.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CompanyType {

	@Id
	@GeneratedValue
	private Long id;
	private String type;
	
	@OneToMany(mappedBy = "companyType")
	private List<Company> companies;

	public CompanyType() {
		
	}
	
	public CompanyType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

}
