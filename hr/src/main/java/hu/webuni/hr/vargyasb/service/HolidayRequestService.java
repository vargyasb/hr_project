package hu.webuni.hr.vargyasb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.vargyasb.dto.HolidayRequestFilterDto;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.HolidayRequest;
import hu.webuni.hr.vargyasb.model.HolidayRequestStatus;
import hu.webuni.hr.vargyasb.model.HrUser;
import hu.webuni.hr.vargyasb.repository.HolidayRequestRepository;

@Service
public class HolidayRequestService {

	@Autowired
	HolidayRequestRepository holidayRequestRepository;
	
	@Autowired
	EmployeeService employeeService;
	
	@Transactional
	public HolidayRequest save(HolidayRequest holidayRequest) {
		return holidayRequestRepository.save(holidayRequest);
	}	
	
	public Page<HolidayRequest> findAll(Pageable pageable) {
		return holidayRequestRepository.findAll(pageable); 
	}
	
	public HolidayRequest findById(long id) {
		return holidayRequestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@Transactional
	public HolidayRequest addHolidayRequest(HolidayRequest holidayRequest, long requesterId) {
		Employee requester = employeeService.findById(requesterId).get();
		requester.addHolidayRequest(holidayRequest);
		holidayRequest.setRequestDate(LocalDateTime.now());
		holidayRequest.setStatus(HolidayRequestStatus.NEW);
		holidayRequest.setApprover(null);
		return holidayRequestRepository.save(holidayRequest);
	}
	
	@Transactional
	public HolidayRequest approveHolidayRequest(long id, HolidayRequestStatus status) {
		HolidayRequest holidayRequest = holidayRequestRepository.findById(id).get();
		Employee manager = holidayRequest.getRequester().getManager();
		HrUser authenticatedUser = (HrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long authenticatedUserId = authenticatedUser.getEmployee().getId();
		if (manager != null) {
			if (!manager.getId().equals(authenticatedUserId))
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		} else if (!holidayRequest.getRequester().getId().equals(authenticatedUserId))
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		
		holidayRequest.setApprover(employeeService.findById(authenticatedUserId).get());
		holidayRequest.setStatus(status);
		return holidayRequest;
	}

	@Transactional
	public HolidayRequest update(HolidayRequest holidayRequest) {
		Optional<HolidayRequest> optionalHolidayRequest = holidayRequestRepository.findById(holidayRequest.getId());
		if (optionalHolidayRequest.isPresent()) {
			HolidayRequest foundHolidayRequest = optionalHolidayRequest.get();
			if (foundHolidayRequest.getStatus() == HolidayRequestStatus.NEW) {
				foundHolidayRequest.setStartDate(holidayRequest.getStartDate());
				foundHolidayRequest.setEndDate(holidayRequest.getEndDate());
				return foundHolidayRequest;
			} else
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
	
	@Transactional
	public void deleteHolidayRequest(long id) {
		HolidayRequest holidayRequest = holidayRequestRepository.findById(id).get();
		if (holidayRequest != null) {
			Long requesterId = holidayRequest.getRequester().getId();
			HrUser authenticatedUser = (HrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (requesterId != authenticatedUser.getEmployee().getId())
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			
			if (holidayRequest.getStatus() == HolidayRequestStatus.NEW) {
				holidayRequest.getRequester().getHolidayRequests().remove(holidayRequest);
				holidayRequestRepository.deleteById(id);
			} else 
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
		} else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			
	}
	
	public Page<HolidayRequest> findByExample(HolidayRequestFilterDto example, Pageable pageable) {
		LocalDate startDate = example.getStartDate();
		LocalDate endDate = example.getEndDate();
		String requesterName = example.getRequesterName();
		String approverName = example.getApproverName();
		HolidayRequestStatus status = example.getStatus();
		LocalDateTime requestDateTimeStart = example.getRequestDateTimeStart();
		LocalDateTime requestDateTimeEnd = example.getRequestDateTimeEnd();
		
		Specification<HolidayRequest> spec = Specification.where(null);
		
		if (status != null)
			spec = spec.and(HolidayRequestSpecifications.hasStatus(status));
		if (requesterName != null)
			spec = spec.and(HolidayRequestSpecifications.hasRequesterName(requesterName));
		if (approverName != null)
			spec = spec.and(HolidayRequestSpecifications.hasApproverName(approverName));
		if (startDate != null)
			spec = spec.and(HolidayRequestSpecifications.isEndDateGreaterThan(startDate));
		if (endDate != null)
			spec = spec.and(HolidayRequestSpecifications.isStartDateLessThan(endDate));
		if (requestDateTimeEnd != null && requestDateTimeStart != null)
			spec = spec.and(HolidayRequestSpecifications.requestDateIsBetween(requestDateTimeStart, requestDateTimeEnd));
		
		return holidayRequestRepository.findAll(spec, pageable);
	}
}
