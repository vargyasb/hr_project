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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.vargyasb.dto.HolidayRequestDto;
import hu.webuni.hr.vargyasb.dto.HolidayRequestFilterDto;
import hu.webuni.hr.vargyasb.dto.LoginDto;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.HolidayRequestStatus;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.HolidayRequestRepository;
import hu.webuni.hr.vargyasb.service.EmployeeService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HolidayRequestControllerIT {

	private static final String PASSWORD = "pass";
	private static final String TESTUSER_NAME = "testuser";
	private static final String TESTUSER2_NAME = "apapapapa";
	private static final String BASE_URI = "api/holidayrequests";

	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	HolidayRequestRepository holidayRequestRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	String testUser1Jwt;
	String testUser2Jwt;
	
	@BeforeEach
	public void init() {
		holidayRequestRepository.deleteAll();
		employeeRepository.deleteAll();
		
		Employee testUser1 = new Employee();
		testUser1.setName(TESTUSER_NAME);
		testUser1.setUsername(TESTUSER_NAME);
		testUser1.setPassword(passwordEncoder.encode(PASSWORD));
		
		Employee testUser2 = new Employee();
		testUser2.setName(TESTUSER2_NAME);
		testUser2.setUsername(TESTUSER2_NAME);
		testUser2.setPassword(passwordEncoder.encode(PASSWORD));
		employeeRepository.save(testUser2);
		
		testUser1.setManager(testUser2);
		employeeRepository.save(testUser1);
		
		LoginDto testUser1LoginDto = createLoginDto(TESTUSER_NAME, PASSWORD);
		testUser1Jwt = login(testUser1LoginDto);
		
		LoginDto testUser2LoginDto = createLoginDto(TESTUSER2_NAME, PASSWORD);
		testUser2Jwt = login(testUser2LoginDto);
	}
	
	@Test
	void testThatHolidayRequestIsCreated() throws Exception {

		List<HolidayRequestDto> requestsBefore = getHolidayRequests(testUser1Jwt);

		HolidayRequestDto holidayRequest = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));

		holidayRequest = createHolidayRequest(holidayRequest, testUser1Jwt);

		List<HolidayRequestDto> requestsAfter = getHolidayRequests(testUser1Jwt);

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
		HolidayRequestDto holidayRequest = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));

		holidayRequest = createHolidayRequest(holidayRequest, testUser1Jwt);
		long holidayRequestId = holidayRequest.getId();
		
		long requesterId = holidayRequest.getRequesterId();
		LocalDate startDate = holidayRequest.getStartDate();
		LocalDate endDate = holidayRequest.getEndDate();
		
		HolidayRequestDto newHolidayRequest = new HolidayRequestDto();
		newHolidayRequest.setStartDate(startDate.plusDays(1));
		newHolidayRequest.setEndDate(endDate.plusDays(1));
		newHolidayRequest.setRequesterId(requesterId);
		newHolidayRequest.setStatus(HolidayRequestStatus.NEW);
		HolidayRequestDto modifiedHolidayRequest = modifyHolidayRequest(holidayRequestId, newHolidayRequest, testUser1Jwt);
		
		assertThat(modifiedHolidayRequest)
		.usingRecursiveComparison()
		.ignoringFields("id", "requestDate")
		.isEqualTo(newHolidayRequest);
	}

	@Test
	void testThatHolidayRequestIsApproved() throws Exception {
		HolidayRequestDto holidayRequest = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		
		holidayRequest = createHolidayRequest(holidayRequest, testUser1Jwt);
		long holidayRequestId = holidayRequest.getId();
		
		assertThat(holidayRequest.getStatus()).isEqualTo(HolidayRequestStatus.NEW);
		assertThat(holidayRequest.getApproverId()).isNull();
		
		holidayRequest = approveHolidayRequest(holidayRequestId, HolidayRequestStatus.ACCEPTED, testUser2Jwt);
		
		assertThat(holidayRequest.getStatus()).isEqualTo(HolidayRequestStatus.ACCEPTED);
	}

	@Test
	void testThatHolidayRequestIsDeleted() throws Exception {
		HolidayRequestDto holidayRequest = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		
		holidayRequest = createHolidayRequest(holidayRequest, testUser1Jwt);
		long holidayRequestId = holidayRequest.getId();
		
		List<HolidayRequestDto> requestsBefore = getHolidayRequests(testUser1Jwt);
		
		assertThat(requestsBefore).hasSize(1);
		
		deleteHolidayRequest(holidayRequestId, testUser1Jwt);
		
		List<HolidayRequestDto> requestsAfter = getHolidayRequests(testUser1Jwt);
		assertThat(requestsAfter).isEmpty();
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleStatus() throws Exception {
		HolidayRequestDto holidayRequestWithNewStatus = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequestWithNewStatus = createHolidayRequest(holidayRequestWithNewStatus, testUser1Jwt);
		long holidayRequestWithNewStatusId = holidayRequestWithNewStatus.getId();
		
		HolidayRequestDto holidayRequestAccepted = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequestAccepted = createHolidayRequest(holidayRequestAccepted, testUser1Jwt);
		long holidayRequestId = holidayRequestAccepted.getId();
		holidayRequestAccepted = approveHolidayRequest(holidayRequestId, HolidayRequestStatus.ACCEPTED, testUser2Jwt);
		
		List<HolidayRequestDto> resultAll = getHolidayRequests(testUser1Jwt);
		
		assertThat(resultAll).hasSize(2);
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setStatus(HolidayRequestStatus.NEW);
		
		List<HolidayRequestDto> resultsFiltered = findHolidayRequestsByExample(example, null, testUser1Jwt);
		
		assertThat(resultsFiltered).hasSize(1);
		assertThat(resultsFiltered.get(0).getId()).isEqualTo(holidayRequestWithNewStatusId);
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleRequesterName() throws Exception {
		HolidayRequestDto holidayRequest1 = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequest1 = createHolidayRequest(holidayRequest1, testUser1Jwt);
		
		long holidayRequesterId = holidayRequest1.getRequesterId();
		Employee requester = employeeRepository.findById(holidayRequesterId).get();

		HolidayRequestDto holidayRequest2 = createHolidayRequestDtoObject(TESTUSER2_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequest2 = createHolidayRequest(holidayRequest2, testUser2Jwt);
		
		String requesterNameToBeFound = TESTUSER_NAME;
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setRequesterName(requesterNameToBeFound);
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null, testUser1Jwt);
		
		assertThat(results).hasSize(1);
		assertThat(results.get(0).getRequesterId()).isEqualTo(requester.getId());
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleApproverName() throws Exception {
		HolidayRequestDto holidayRequest1 = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequest1 = createHolidayRequest(holidayRequest1, testUser1Jwt);
		long holidayRequest1Id = holidayRequest1.getId();
		holidayRequest1 = approveHolidayRequest(holidayRequest1Id, HolidayRequestStatus.ACCEPTED, testUser2Jwt);

		HolidayRequestDto holidayRequest2 = createHolidayRequestDtoObject(TESTUSER2_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequest2 = createHolidayRequest(holidayRequest2, testUser2Jwt);
				
		Employee approver = employeeRepository.findByUsername(TESTUSER2_NAME).get();
		String approverName = approver.getName();
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setApproverName(approverName);
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null, testUser1Jwt);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getApproverId()).isEqualTo(approver.getId());
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleRequestDate() throws Exception {
		HolidayRequestDto holidayRequest = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequest = createHolidayRequest(holidayRequest, testUser1Jwt);

		LocalDateTime requestDate = holidayRequest.getRequestDate();
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setRequestDateTimeStart(requestDate.minusDays(1));
		example.setRequestDateTimeEnd(requestDate.plusDays(1));
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null, testUser1Jwt);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getRequestDate()).isEqualToIgnoringSeconds(requestDate);
	}

	@Test
	void testThatHolidayRequestIsFoundByExampleStartAndEndDates() throws Exception {
		HolidayRequestDto holidayRequest1 = createHolidayRequestDtoObject(TESTUSER_NAME, 
				LocalDate.of(2022, 11, 12), LocalDate.of(2022, 11, 13));
		holidayRequest1 = createHolidayRequest(holidayRequest1, testUser1Jwt);
		
		HolidayRequestDto holidayRequest2 = createHolidayRequestDtoObject(TESTUSER2_NAME, 
				LocalDate.of(2022, 10, 12), LocalDate.of(2022, 10, 13));
		holidayRequest2 = createHolidayRequest(holidayRequest2, testUser2Jwt);
		
		LocalDate requestStartDate = holidayRequest1.getStartDate();
		LocalDate requestEndDate = holidayRequest1.getEndDate();
		
		HolidayRequestFilterDto example = new HolidayRequestFilterDto();
		example.setStartDate(requestStartDate.minusDays(1));
		example.setEndDate(requestEndDate.plusDays(1));
		
		List<HolidayRequestDto> results = findHolidayRequestsByExample(example, null, testUser1Jwt);
		
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getStartDate().isEqual(requestStartDate));
		assertThat(results.get(0).getEndDate().isEqual(requestEndDate));
	}

	private List<HolidayRequestDto> getHolidayRequests(String jwt) {
		List<HolidayRequestDto> holidayRequests = webTestClient
				.get()
				.uri(BASE_URI)
				.headers(headers -> headers.setBearerAuth(jwt))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();

		Collections.sort(holidayRequests, (o1, o2) -> Long.compare(o1.getId(), o2.getId()));
		return holidayRequests;
	}

	private HolidayRequestDto createHolidayRequest(HolidayRequestDto holidayRequest, String jwt) {
		return webTestClient
				.post()
				.uri(BASE_URI)
				.headers(headers -> headers.setBearerAuth(jwt))
				.bodyValue(holidayRequest)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void deleteHolidayRequest(Long requestId, String jwt) {
			webTestClient
				.delete()
				.uri(BASE_URI + "/" + requestId)
				.headers(headers -> headers.setBearerAuth(jwt))
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	private HolidayRequestDto modifyHolidayRequest(Long requestId, HolidayRequestDto holidayRequest, String jwt) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/" + requestId)
				.headers(headers -> headers.setBearerAuth(jwt))
				.bodyValue(holidayRequest)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayRequestDto approveHolidayRequest(long id, HolidayRequestStatus status, String jwt) {
		return webTestClient
				.put()
				.uri(uriBuilder -> uriBuilder
						.path(BASE_URI + "/" + id + "/approve")
						.queryParam("status", status)
						.build())
				.headers(headers -> headers.setBearerAuth(jwt))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
				
	}
	
	private List<HolidayRequestDto> findHolidayRequestsByExample(HolidayRequestFilterDto filter, Pageable pageable, String jwt) {
		return webTestClient
				.post()
				.uri(BASE_URI + "/find")
				.headers(headers -> headers.setBearerAuth(jwt))
				.bodyValue(filter)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private String login(LoginDto loginDto) {
		return webTestClient
				.post()
				.uri("/api/login")
				.bodyValue(loginDto)
				.exchange()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
	}
	
	private LoginDto createLoginDto(String username, String password) {
		LoginDto loginDto = new LoginDto();
		loginDto.setUsername(username);
		loginDto.setPassword(password);
		return loginDto;
	}
	
	private HolidayRequestDto createHolidayRequestDtoObject(String employeeName, LocalDate start, LocalDate end) {
		HolidayRequestDto holidayRequest = new HolidayRequestDto();
		holidayRequest.setStartDate(start);
		holidayRequest.setEndDate(end);
		holidayRequest.setRequesterId(employeeRepository.findByUsername(employeeName).get().getId());
		return holidayRequest;
	}
}
