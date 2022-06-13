package hu.webuni.hr.vargyasb.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.service.IAvgSalaryByPosition;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	Page<Employee> findBySalaryGreaterThan(int salary, Pageable pageable);

	List<Employee> findByPosition(String position);
	
	List<Employee> findByNameStartingWithIgnoreCase(String keyword);
	
	List<Employee> findByStartOfEmploymentBetween(LocalDateTime from, LocalDateTime to);

	@Query(value = "SELECT e.position AS position, AVG(e.salary) AS averageSalary FROM Employee e JOIN Company c ON e.company = c.id"
			+ " WHERE c.id = :companyId GROUP BY e.position ORDER BY AVG(e.salary) DESC")
	List<IAvgSalaryByPosition> averageDescSalaryByPositionInACompany(long companyId);
}
