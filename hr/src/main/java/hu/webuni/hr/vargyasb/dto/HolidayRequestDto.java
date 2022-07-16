package hu.webuni.hr.vargyasb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import hu.webuni.hr.vargyasb.model.HolidayRequestStatus;

public class HolidayRequestDto {
	
	private Long id;
	@NotNull
	private LocalDate startDate;
	@NotNull
	private LocalDate endDate;
	@NotNull
	private Long requesterId;
	private Long approverId;

	private LocalDateTime requestDate;
	private HolidayRequestStatus status;

	public HolidayRequestDto() {

	}

//	public HolidayRequestDto(LocalDate startDate, LocalDate endDate, Long requesterId, Long approverId) {
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.requesterId = requesterId;
//		this.approverId = approverId;
//		this.requestDate = LocalDateTime.now();
//		this.status = HolidayRequestStatus.NEW;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

	public HolidayRequestStatus getStatus() {
		return status;
	}

	public void setStatus(HolidayRequestStatus status) {
		this.status = status;
	}

}
