package hu.webuni.hr.vargyasb.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);
	
	EmployeeDto employeeToDto(Employee employee);
	
	Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
