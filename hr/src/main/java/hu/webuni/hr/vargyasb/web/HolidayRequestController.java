package hu.webuni.hr.vargyasb.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.vargyasb.dto.HolidayRequestDto;
import hu.webuni.hr.vargyasb.dto.HolidayRequestFilterDto;
import hu.webuni.hr.vargyasb.mapper.HolidayRequestMapper;
import hu.webuni.hr.vargyasb.model.HolidayRequest;
import hu.webuni.hr.vargyasb.model.HolidayRequestStatus;
import hu.webuni.hr.vargyasb.service.HolidayRequestService;

@RestController
@RequestMapping("api/holidayrequests")
public class HolidayRequestController {

	@Autowired
	HolidayRequestService holidayRequestService;
	
	@Autowired
	HolidayRequestMapper holidayRequestMapper;
	
	@GetMapping
	public List<HolidayRequestDto> getAll(Pageable pageable) {
		return holidayRequestMapper.holidayRequestListToDtoList(holidayRequestService.findAll(pageable).getContent());
	}
	
	@GetMapping("/{id}")
	public HolidayRequestDto getById(@PathVariable long id) {
		return holidayRequestMapper.holidayRequestToDto(holidayRequestService.findById(id));
	}

	@PostMapping
	public HolidayRequestDto createNewHolidayRequest(@RequestBody HolidayRequestDto holidayRequestDto) {
		HolidayRequest holidayRequest = holidayRequestService.addHolidayRequest(holidayRequestMapper.holidayRequestDtoToHolidayRequest(holidayRequestDto), holidayRequestDto.getRequesterId());
		return holidayRequestMapper.holidayRequestToDto(holidayRequest);
	}
	
	@PutMapping("/{id}")
	public HolidayRequestDto modifyHolidayRequest(@PathVariable long id, @RequestBody HolidayRequestDto holidayRequestDto) {
		holidayRequestDto.setId(id);
		HolidayRequest holidayRequest = holidayRequestService.update(holidayRequestMapper.holidayRequestDtoToHolidayRequest(holidayRequestDto));
		return holidayRequestMapper.holidayRequestToDto(holidayRequest);
	}
	
	@DeleteMapping("/{id}")
	public void deleteHolidayRequest(@PathVariable long id) {
		holidayRequestService.deleteHolidayRequest(id);
	}
	
	@PutMapping(value = "/{id}/approve", params = {"status", "approverId" })
	public HolidayRequestDto approveHolidayRequest(@PathVariable long id, @RequestParam HolidayRequestStatus status, @RequestParam long approverId) {
		return holidayRequestMapper.holidayRequestToDto(holidayRequestService.approveHolidayRequest(id, approverId, status));
	}
	
	@PostMapping("/find")
	public List<HolidayRequestDto> findHolidayRequestsByExample(@RequestBody HolidayRequestFilterDto example, Pageable pageable) {
		Page<HolidayRequest> page = holidayRequestService.findByExample(example, pageable);
		return holidayRequestMapper.holidayRequestListToDtoList(page.getContent());
	}
	
	
}

