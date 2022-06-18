package hu.webuni.hr.vargyasb.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.service.IAvgSalaryByPosition;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	Page<Employee> findBySalaryGreaterThan(int salary, Pageable pageable);

	List<Employee> findByPositionName(String position);
	
	List<Employee> findByNameStartingWithIgnoreCase(String keyword);
	
	List<Employee> findByStartOfEmploymentBetween(LocalDateTime from, LocalDateTime to);
	
	@Modifying
	@Transactional
	//1. megoldás: nem müködik (hibernate cross join)
//	@Query("UPDATE Employee e"
//			+ "SET e.salary = :minSalary"
//			+ "WHERE e.position.name = :position"
//			+ "AND e.salary < :minSalary"
//			+ "AND e.company.id = :companyId")
	
	@Query("UPDATE Employee e "
			+ "SET e.salary = :minSalary "
			+ "WHERE e.id IN "
			+ "(SELECT e2.id "
			+ "FROM Employee e2 "
			+ "WHERE e2.position.name = :position "
			+ "AND e2.salary < :minSalary "
			+ "AND e2.company.id = :companyId"
			+ ")")
	int updateSalaries(String position, int minSalary, long companyId);

}
