package hu.webuni.hr.vargyasb.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.vargyasb.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	List<Employee> findBySalaryGreaterThan(int salary);

	List<Employee> findByPosition(String position);
	
	List<Employee> findByNameStartingWithIgnoreCase(String keyword);
	
	List<Employee> findByStartOfEmploymentBetween(LocalDateTime from, LocalDateTime to);
}
