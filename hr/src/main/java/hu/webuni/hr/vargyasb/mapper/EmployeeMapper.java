package hu.webuni.hr.vargyasb.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	EmployeeDto employeeToDto(Employee employee);
	List<EmployeeDto> employeesToDtos(List<Employee> employees);
	
	Employee employeeDtoToEmployee(EmployeeDto employeeDto);
	List<Employee> employeeDtosToemployees(List<EmployeeDto> employeesDtos);
	
	@Named("summary")
	@Mapping(target = "company", ignore = true)
	EmployeeDto employeeToDtoWithoutCompany(Employee employee);
	
	@IterableMapping(qualifiedByName = "summary")
	List<EmployeeDto> employeestoDtosWithoutCompany(List<Employee> employees);
}
