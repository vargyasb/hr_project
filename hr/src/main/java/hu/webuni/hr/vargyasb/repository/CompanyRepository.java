package hu.webuni.hr.vargyasb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.webuni.hr.vargyasb.model.Company;
import hu.webuni.hr.vargyasb.service.IAvgSalaryByPosition;

public interface CompanyRepository extends JpaRepository<Company, Long>{

	List<Company> findDistinctCompanyByEmployeesSalaryGreaterThan(int salary);
	
	@Query(value = "SELECT distinct c, COUNT(e.id) FROM Company c "
			+ "JOIN Employee e ON e.company = c.id GROUP BY c.id HAVING COUNT(e.id) > :limit")
	List<Company> findCompaniesWhereMoreEmployeeWorksThan(@Param("limit") long limit);
	
	@Query(value = "SELECT e.position.name AS position, AVG(e.salary) AS averageSalary FROM Employee e JOIN Company c ON e.company = c.id"
			+ " WHERE c.id = :companyId GROUP BY e.position.name ORDER BY AVG(e.salary) DESC")
	List<IAvgSalaryByPosition> averageDescSalaryByPositionInACompany(long companyId);
}
