package hu.webuni.hr.vargyasb.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.vargyasb.dto.CompanyDto;
import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	@Mapping(target = "companyType.companies", ignore = true)
	CompanyDto companyToCompanyDto(Company company);
	List<CompanyDto> companiesToCompanyDtos(List<Company> companies);
	
	Company companyDtoToCompany(CompanyDto companyDto);
	List<Company> companyDtosToCompanies(List<CompanyDto> companyDtos);
	
	@Named("summary")
	@Mapping(target = "employees", ignore = true)
	@Mapping(target = "companyType.companies", ignore = true)
	CompanyDto companyToCompanyDtoWithNoEmployees(Company company);
	
	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companiesToCompanyDtosWithNoEmployees(List<Company> companies);

	@Mapping(target = "company", ignore = true)
	@Mapping(target = "position", source = "position.name")
	EmployeeDto employeeToDto(Employee employee);
	
	@InheritInverseConfiguration
	Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
