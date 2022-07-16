package hu.webuni.hr.vargyasb;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.vargyasb.dto.HolidayRequestDto;
import hu.webuni.hr.vargyasb.dto.HolidayRequestFilterDto;
import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.HolidayRequestStatus;
import hu.webuni.hr.vargyasb.model.Position;
import hu.webuni.hr.vargyasb.repository.CompanyRepository;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.HolidayRequestRepository;
import hu.webuni.hr.vargyasb.repository.PositionDetailsByCompanyRepository;
import hu.webuni.hr.vargyasb.repository.PositionRepository;
import hu.webuni.hr.vargyasb.service.EmployeeService;
import hu.webuni.hr.vargyasb.service.InitDbService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HolidayRequestControllerIT {

	private static final String BASE_URI = "api/holidayrequests";

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	InitDbService initDbService;
	
	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

	@Autowired
	PositionRepository positionRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	HolidayRequestRepository holidayRequestRepository;
	
	@BeforeEach
	public void init() {
		holidayRequestRepository.deleteAll();
		employeeRepository.deleteAll();
		positionDetailsByCompanyRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
	}

	@Test
	void testThatHolidayRequestIsCreated() throws Exception {

		List<HolidayRequestDto> requestsBefore = getHolidayRequests();

		HolidayRequestDto holidayRequest = createTestData();

		createHolidayRequest(holidayRequest);

		List<HolidayRequestDto> requestsAfter = getHolidayRequests();

		assertThat(requestsAfter.subList(0, requestsBefore.size()))
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactlyElementsOf(requestsBefore);

		assertThat(requestsAfter.get(requestsAfter.size() - 1))
		.usingRecursiveComparison()
		.ignoringFields("id", "requestDate", "status")
		.isEqualTo(holidayRequest);
		
		assertThat(requestsAfter.get(requestsAfter.size() - 1)
		.getStatus())
		.isEqualTo(HolidayRequestStatus.NEW);
	}

	@Test
	void testThatHolidayRequestIsModified() throws Exception {
		HolidayRequestDto holidayRequest = createTestData();

		holidayRequest = createHolidayRequest(holidayRequest);
		long holidayRequestId = holidayRequest.getId();
		
		long requesterId = holidayRequest.getRequesterId();
		long approverId = holidayRequest.getApproverId();
		LocalDate startDate = holidayRequest.getStartDate();
		LocalDate endDate = holidayRequest.getEndDate();
		
		HolidayRequestDto newHolidayRequest = new HolidayRequestDto();
		newHolidayRequest.setStartDate(startDate.plusDays(1));
		newHolidayRequest.setEndDate(endDate.plusDays(1));
		newHolidayRequest.setRequesterId(requesterId);
		newHolidayRequest.setApproverId(approverId);
		newHolidayRequest.setStatus(HolidayRequestStatus.NEW);
		HolidayRequestDto modifiedHolidayRequest = modifyHolidayRequest(holidayRequestId, newHolidayRequest);
		
		assertThat(modifiedHolidayRequest)
		.usingRecursiveComparison()
		.ignoringFields("id", "requestDate")
		.isEqualTo(newHolidayRequest);
	}

	@Test
	void testThatHolidayRequestIsApproved() throws Exception {
		HolidayRequestDto holidayRequest = createTestData();
		
		holidayRequest = createHolidayRequest(holidayRequest);
		long holidayRequestId = holidayRequest.getId();
		long approverId = holidayRequest.getApproverId();
		
		assertThat(holidayRequest.getStatus()).isEqualTo(HolidayRequestStatus.NEW);
		
		holidayRequest = approveHolidayRequest(holidayRequestId, HolidayRequestStatus.ACCEPTED, approverId);
		
		assertThat(holidayRequest.getStatus()).isEqualTo(HolidayRequestStatus.ACCEPTED);
	}

	@Test
	void testThatHolidayRequestIsDeleted() throws Exception {
		
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleStatus() throws Exception {
		HolidayRequestDto holidayRequestWithNewStatus = createTestData();
		holidayRequestWithNewStatus = createHolidayRequest(holidayRequestWithNewStatus);
		long holidayRequestIdNew = holidayRequestWithNewStatus.getId();
		
		HolidayRequestDto holidayRequestAccepted = createTestData();
		holidayRequestAccepted = createHolidayRequest(holidayRequestAccepted);
		long holidayRequestId = holidayRequestAccepted.getId();
		long approverId = holidayRequestAccepted.getApproverId();
		holidayRequestAccepted = approveHolidayRequest(holidayRequestId, HolidayRequestStatus.ACCEPTED, approverId);
		
		List<HolidayRequestDto> resultAll = getHolidayRequests();
		
		assertThat(resultAll.size()).isEqualTo(2);
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setStatus(HolidayRequestStatus.NEW);
		
		List<HolidayRequestDto> resultsFiltered = findHolidayRequestsByExample(example, null);
		
		assertThat(resultsFiltered.size()).isEqualTo(1);
		assertThat(resultsFiltered.get(0).getId()).isEqualTo(holidayRequestIdNew);
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleRequesterName() throws Exception {
		HolidayRequestDto holidayRequest = createTestData();
		holidayRequest = createHolidayRequest(holidayRequest);
		long requesterId = holidayRequest.getRequesterId();
		Employee requester = employeeRepository.findById(requesterId).get();
		String requesterName = requester.getName();
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setRequesterName(requesterName);
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getRequesterId()).isEqualTo(requester.getId());
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleApproverName() throws Exception {
		HolidayRequestDto holidayRequest = createTestData();
		holidayRequest = createHolidayRequest(holidayRequest);
		long approverId = holidayRequest.getApproverId();
		Employee approver = employeeRepository.findById(approverId).get();
		String approverName = approver.getName();
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setApproverName(approverName);
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getApproverId()).isEqualTo(approver.getId());
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleRequestDate() throws Exception {
		HolidayRequestDto holidayRequest = createTestData();
		holidayRequest = createHolidayRequest(holidayRequest);
		LocalDateTime requestDate = holidayRequest.getRequestDate();
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setRequestDateTimeStart(requestDate.minusDays(1));
		example.setRequestDateTimeEnd(requestDate.plusDays(1));
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getRequestDate()).isEqualToIgnoringSeconds(requestDate);
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleStartAndEndDates() throws Exception {
		HolidayRequestDto holidayRequest = createTestData();
		holidayRequest = createHolidayRequest(holidayRequest);
		LocalDate requestStartDate = holidayRequest.getStartDate();
		LocalDate requestEndDate = holidayRequest.getEndDate();
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setStartDate(requestStartDate.minusDays(1));
		example.setEndDate(requestEndDate.plusDays(1));
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getStartDate().isEqual(requestStartDate));
		assertThat(results.get(0).getEndDate().isEqual(requestEndDate));
	}

	private List<HolidayRequestDto> getHolidayRequests() {
		List<HolidayRequestDto> holidayRequests = webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();

		Collections.sort(holidayRequests, (o1, o2) -> Long.compare(o1.getId(), o2.getId()));
		return holidayRequests;
	}

	private HolidayRequestDto createHolidayRequest(HolidayRequestDto holidayRequest) {
		return webTestClient
				.post()
				.uri(BASE_URI)
				.bodyValue(holidayRequest)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayRequestDto modifyHolidayRequest(Long requestId, HolidayRequestDto holidayRequest) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/" + requestId)
				.bodyValue(holidayRequest)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayRequestDto approveHolidayRequest(long id, HolidayRequestStatus status, long approverId) {
		return webTestClient
				.put()
				.uri(uriBuilder -> uriBuilder
						.path(BASE_URI + "/" + id + "/approve")
						.queryParam("status", status)
						.queryParam("approverId", approverId)
						.build())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
				
	}
	
	private List<HolidayRequestDto> findHolidayRequestsByExample(HolidayRequestFilterDto filter, Pageable pageable) {
		return webTestClient
				.post()
				.uri(BASE_URI + "/find")
				.bodyValue(filter)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayRequestDto createTestData() {
		Employee employee1 = new Employee("Teszt Elek", 9999, LocalDateTime.of(2010, 10, 13, 10, 25));
		Company company = new Company("1234", "Udemy", "Something str 99");
		Position position = new Position("Developer", "BSC", 10000);
		position = positionRepository.save(position);
		company = companyRepository.save(company);
		employee1.setCompany(company);
		employee1.setPosition(position);
		employee1 = employeeRepository.save(employee1);
		long employee1Id = employee1.getId();

		Employee employee2 = new Employee("Teszt2 Elek", 1234, LocalDateTime.of(2015, 10, 13, 10, 25));
		company = new Company("4444", "Webuni", "Something str 12");
		position = new Position("Tester", "BSC", 1000);
		position = positionRepository.save(position);
		company = companyRepository.save(company);
		employee2.setCompany(company);
		employee2.setPosition(position);
		employee2 = employeeRepository.save(employee2);
		long employee2Id = employee2.getId();

		HolidayRequestDto holidayRequest = new HolidayRequestDto();
		holidayRequest.setStartDate(LocalDate.of(2022, 11, 12));
		holidayRequest.setEndDate(LocalDate.of(2022, 11, 13));
		holidayRequest.setRequesterId(employee1Id);
		holidayRequest.setApproverId(employee2Id);

		return holidayRequest;
	}
}
