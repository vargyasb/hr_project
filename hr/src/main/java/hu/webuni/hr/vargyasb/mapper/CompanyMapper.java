package hu.webuni.hr.vargyasb.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.vargyasb.dto.CompanyDto;
import hu.webuni.hr.vargyasb.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	List<CompanyDto> companiesToCompanyDtos(List<Company> companies);
	
	List<Company> companyDtosToCompanies(List<CompanyDto> companyDtos);
	
	CompanyDto companyToCompanyDto(Company company);
	
	Company companyDtoToCompany(CompanyDto companyDto);
}
