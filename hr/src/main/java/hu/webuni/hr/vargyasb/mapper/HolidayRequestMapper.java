package hu.webuni.hr.vargyasb.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.vargyasb.dto.EmployeeDto;
import hu.webuni.hr.vargyasb.dto.HolidayRequestDto;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.HolidayRequest;

@Mapper(componentModel = "spring")
public interface HolidayRequestMapper {

	List<HolidayRequestDto> holidayRequestListToDtoList(List<HolidayRequest> holidayRequestList);
	
	List<HolidayRequest> holidayRequestDtoListToList(List<HolidayRequestDto> holidayRequestDtoList);
	
	@Mapping(target = "requesterId", source = "requester.id")
	@Mapping(target = "approverId", source = "approver.id")
	HolidayRequestDto holidayRequestToDto(HolidayRequest holidayRequest);
	
	@InheritInverseConfiguration
	HolidayRequest holidayRequestDtoToHolidayRequest(HolidayRequestDto holidayRequestDto);
	
	@Mapping(target = "company.employees", ignore = true)
	@Mapping(target = "company.companyType.companies", ignore = true)
	@Mapping(target = "position", source="position.name")
	EmployeeDto employeeToDto(Employee employee);
}
