package hu.webuni.hr.vargyasb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class HolidayRequest {

	@Id
	@GeneratedValue
	private Long id;
	
	private LocalDate startDate;
	private LocalDate endDate;

	@ManyToOne
	private Employee requester;
	
	@ManyToOne
	private Employee approver;

	private LocalDateTime requestDate;
	private HolidayRequestStatus status;

	public HolidayRequest() {

	}

//	public HolidayRequest(LocalDate startDate, LocalDate endDate, Employee requester) {
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.requester = requester;
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

	public Employee getRequester() {
		return requester;
	}

	public void setRequester(Employee requester) {
		this.requester = requester;
	}

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
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
