package hu.webuni.hr.vargyasb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.vargyasb.model.Employee_;
import hu.webuni.hr.vargyasb.model.HolidayRequest;
import hu.webuni.hr.vargyasb.model.HolidayRequestStatus;
import hu.webuni.hr.vargyasb.model.HolidayRequest_;

public class HolidayRequestSpecifications {

	public static Specification<HolidayRequest> hasStatus(HolidayRequestStatus status) {
		return (root, cq, cb) -> cb.equal(root.get(HolidayRequest_.status), status);
	}

	public static Specification<HolidayRequest> hasRequesterName(String requesterName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(HolidayRequest_.requester).get(Employee_.name)), (requesterName + "%").toLowerCase());
	}

	public static Specification<HolidayRequest> hasApproverName(String approverName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(HolidayRequest_.approver).get(Employee_.name)), (approverName + "%").toLowerCase());
	}

	public static Specification<HolidayRequest> isEndDateGreaterThan(LocalDate startDate) {
		return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get(HolidayRequest_.endDate), startDate);
	}

	public static Specification<HolidayRequest> isStartDateLessThan(LocalDate endDate) {
		return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get(HolidayRequest_.startDate), endDate);
	}

	public static Specification<HolidayRequest> requestDateIsBetween(LocalDateTime requestDateTimeStart,
			LocalDateTime requestDateTimeEnd) {
		return (root, cq, cb) -> cb.between(root.get(HolidayRequest_.requestDate), requestDateTimeStart, requestDateTimeEnd);
	}

}
