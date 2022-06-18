package hu.webuni.hr.vargyasb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.vargyasb.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long>{

	Optional<Position> findByName(String name);
}
