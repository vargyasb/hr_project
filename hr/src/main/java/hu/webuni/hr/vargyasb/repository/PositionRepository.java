package hu.webuni.hr.vargyasb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.vargyasb.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long>{

	Optional<Position> findByName(String name);
	
	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = {"employees", "employees.position"})
	@Query("SELECT p FROM Position p")
	List<Position> findAllWithEmployees();
	
	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = {"employees", "employees.position"})
	@Query("SELECT p FROM Company p WHERE p.id = :positionId")
	Optional<Position> findByIdWithEmployees(long positionId);
}
