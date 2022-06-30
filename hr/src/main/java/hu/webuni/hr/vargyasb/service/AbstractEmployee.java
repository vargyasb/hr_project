package hu.webuni.hr.vargyasb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.model.Position;
import hu.webuni.hr.vargyasb.repository.EmployeeRepository;
import hu.webuni.hr.vargyasb.repository.PositionRepository;

public abstract class AbstractEmployee implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Transactional
	public Employee save(Employee employee) {
		Position position = employee.getPosition();
		if (position != null) {
			String positionName = position.getName();
			if (!ObjectUtils.isEmpty(positionName)) {
				Position positionInDb = null;
				Optional<Position> foundPosition = positionRepository.findByName(positionName);
				if (foundPosition.isPresent()) {
					positionInDb = foundPosition.get();
				} else {
					positionInDb = positionRepository.save(position);
				}
				employee.setPosition(positionInDb);
			} else {
				employee.setPosition(null);
			}
		}
		return employeeRepository.save(employee);
	}
	
//	@Transactional
//	public Employee update(Employee employee) {
//		if (employeeRepository.existsById(employee.getId())) {
//			return employeeRepository.save(employee);
//		} else {
//			throw new NoSuchElementException();
//		}
//	}
	
	public Page<Employee> findAll(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}
	
	public Optional<Employee> findById(Long id) {
		return employeeRepository.findById(id);
	}
	
	@Transactional
	public void delete(Long id) {
		employeeRepository.deleteById(id);
	}
	
	public List<Employee> getEmployeesWhoseSalaryIsGreaterThan(int salary, Integer pageNr, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNr, pageSize);
		Page<Employee> pagedResult = employeeRepository.findBySalaryGreaterThan(salary, paging);
		
//		return employeeRepository.findBySalaryGreaterThan(salary, paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Employee>();
		}
	}

	public List<Employee> findByPosition(String position) {
		return employeeRepository.findByPositionName(position);
	}

	public List<Employee> findByNameStartingWithIgnoreCase(String keyword) {
		return employeeRepository.findByNameStartingWithIgnoreCase(keyword);
	}

	public List<Employee> findByStartOfEmploymentBetween(LocalDateTime from, LocalDateTime to) {
		return employeeRepository.findByStartOfEmploymentBetween(from, to);
	}
	
	public List<Employee> findEmployeesByExample(Employee example) {
		Long id = example.getId();
		if(id == null)
			id = (long) 0;
		String name = example.getName();
		String positionName = null;
		Position position = example.getPosition();
		if(position != null)
			positionName = position.getName();
		int salary = example.getSalary();
		LocalDateTime startOfEmployment = example.getStartOfEmployment();
		String companyName = null;
		Company company = example.getCompany();
		if(company != null)
			companyName = company.getName();
		
		Specification<Employee> spec = Specification.where(null);
		
		if(id > 0)
			spec = spec.and(EmployeeSpecifications.hasId(id));
		if(StringUtils.hasText(name))
			spec = spec.and(EmployeeSpecifications.hasName(name));
		if(StringUtils.hasText(positionName))
			spec = spec.and(EmployeeSpecifications.hasPosition(positionName));
		if(salary > 0)
			spec = spec.and(EmployeeSpecifications.hasSalary(salary));
		if(startOfEmployment != null)
			spec = spec.and(EmployeeSpecifications.hasstartOfEmployment(startOfEmployment));
		if(StringUtils.hasText(companyName))
			spec = spec.and(EmployeeSpecifications.hasCompany(companyName));
			
		return employeeRepository.findAll(spec, Sort.by("id"));
	}
	
}
