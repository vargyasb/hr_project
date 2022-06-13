package hu.webuni.hr.vargyasb.mapper;

import java.lang.annotation.Target;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);
	
	List<Employee> employeeDtosToemployees(List<EmployeeDto> employeesDtos);
	
	@Mapping(target = "company", ignore = true)
	EmployeeDto employeeToDto(Employee employee);
	
	Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
