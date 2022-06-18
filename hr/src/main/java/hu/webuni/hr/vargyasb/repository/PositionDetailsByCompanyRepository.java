package hu.webuni.hr.vargyasb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.vargyasb.model.PositionDetailsByCompany;

public interface PositionDetailsByCompanyRepository extends JpaRepository<PositionDetailsByCompany, Long>{

	Optional<PositionDetailsByCompany> findByPositionNameAndCompanyId(String positionName, long companyId);
}
