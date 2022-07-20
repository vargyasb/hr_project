package hu.webuni.hr.vargyasb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import hu.webuni.hr.vargyasb.model.HolidayRequestStatus;

public class HolidayRequestFilterDto {

	private LocalDateTime requestDateTimeStart;
	private LocalDateTime requestDateTimeEnd;	
	private String requesterName;
	private String approverName;
	private HolidayRequestStatus status;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public HolidayRequestFilterDto() {
		
	}

	public LocalDateTime getRequestDateTimeStart() {
		return requestDateTimeStart;
	}

	public void setRequestDateTimeStart(LocalDateTime requestDateTimeStart) {
		this.requestDateTimeStart = requestDateTimeStart;
	}

	public LocalDateTime getRequestDateTimeEnd() {
		return requestDateTimeEnd;
	}

	public void setRequestDateTimeEnd(LocalDateTime requestDateTimeEnd) {
		this.requestDateTimeEnd = requestDateTimeEnd;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public HolidayRequestStatus getStatus() {
		return status;
	}

	public void setStatus(HolidayRequestStatus status) {
		this.status = status;
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
	
}
