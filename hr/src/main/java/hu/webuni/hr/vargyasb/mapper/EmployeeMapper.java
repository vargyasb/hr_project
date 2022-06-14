package hu.webuni.hr.vargyasb.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);
	
	List<Employee> employeeDtosToemployees(List<EmployeeDto> employeesDtos);
	
	@Mapping(target = "company.employees", ignore = true)
	@Mapping(target = "company.companyType.companies", ignore = true)
	@Mapping(target = "position", source="position.name")
	EmployeeDto employeeToDto(Employee employee);
	
	@InheritInverseConfiguration
	Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
