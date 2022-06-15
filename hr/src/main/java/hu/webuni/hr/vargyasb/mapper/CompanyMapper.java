package hu.webuni.hr.vargyasb.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.vargyasb.dto.CompanyDto;
import hu.webuni.hr.vargyasb.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	CompanyDto companyToCompanyDto(Company company);
	List<CompanyDto> companiesToCompanyDtos(List<Company> companies);
	
	Company companyDtoToCompany(CompanyDto companyDto);
	List<Company> companyDtosToCompanies(List<CompanyDto> companyDtos);
	
	@Named("summary")
	@Mapping(target = "employees", ignore = true)
	CompanyDto companyToCompanyDtoWithNoEmployees(Company company);
	
	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companiesToCompanyDtosWithNoEmployees(List<Company> companies);
	
}
